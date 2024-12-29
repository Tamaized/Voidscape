package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CollectorBlockEntity extends BlockEntity {

	@Autowired
	private static ModFluids modFluids;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntities.COLLECTOR.get(), (object, context) -> object.fluids);
	}

	private final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid().isSame(modFluids.VOIDIC_SOURCE.get()));

	private final BlockPosDirectionCapabilityCacher<IItemHandler> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int processTick;

	public CollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.COLLECTOR.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		processTick = tag.getInt("processTick");
		fluids.readFromNBT(registries, tag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("processTick", processTick);
		tag.put("tank", fluids.writeToNBT(registries, new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof CollectorBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		IFluidHandler fluid = entity.fluids;
		if (entity.processTick <= 0 && fluid.getFluidInTank(0).getAmount() > 0) {
			fluid.drain(1, IFluidHandler.FluidAction.EXECUTE);
			entity.processTick = 40;
		} else if (entity.processTick > 0) {
			entity.processTick--;
			if (entity.processTick <= 0) {
				ClientPacketSendParticles packet = new ClientPacketSendParticles();
				AtomicBoolean process = new AtomicBoolean(false);
				List<ServerPlayer> players = new ArrayList<>();
				level.getEntities((Entity) null, new AABB(blockPos).inflate(6D), e -> true).forEach(e -> {
					if (e instanceof ServerPlayer serverPlayer)
						players.add(serverPlayer);
					if (!(e instanceof ItemEntity item) || !(level instanceof ServerLevel serverLevel))
						return;
					for (Direction face : Direction.values()) {
						IItemHandler other = entity.capabilityCache.get(Capabilities.ItemHandler.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
						if (other != null) {
							int count = item.getItem().getCount();
							ItemStack consumed = ItemHandlerHelper.insertItemStacked(other, item.getItem(), false);
							if (!consumed.isEmpty() && count == consumed.getCount())
								continue;
							item.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.25F, 1F);
							if (consumed.isEmpty())
								item.discard();
							else
								item.setItem(consumed);
							process.set(true);
							for (int i = 0; i < 20; i++) {
								Vec3 pos = new Vec3(0.5D, 0, 0)
									.yRot((float) Math.toRadians(level.getRandom().nextInt(360)))
									.scale(0.2F + level.getRandom().nextFloat() * 0.8F)
									.add(e.position());
								packet.queueParticle(ParticleTypes.PORTAL, false, pos.x(), pos.y(), pos.z(), 0, 0, 0);
							}
							break;
						}
					}
				});
				if (process.get()) {
					players.forEach(advancementTriggers.COLLECTOR_TRIGGER.get()::trigger);
					if (level instanceof ServerLevel serverLevel)
						PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(blockPos), packet);
				} else {
					entity.processTick = 80;
				}
			}
		}
	}

}
