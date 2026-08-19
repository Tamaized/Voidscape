package tamaized.voidscape.block.entity;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import tamaized.beanification.Autowired;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CollectorBlockEntity extends TickableBlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.COLLECTOR.get(), (object, _) -> object.fluids.get());
	}

	public final Supplier<FluidStacksResourceHandler> fluids = Suppliers.memoize(() -> new FluidStacksResourceHandler(NonNullList.of(
		new FluidStack(modFluids.VOIDIC_SOURCE.get(), 0)
	), 10000));

	private final BlockPosDirectionCapabilityCacher<ResourceHandler<ItemResource>> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int processTick;

	public CollectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.COLLECTOR.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTick = input.getIntOr("processTick", 0);
		fluids.get().deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		fluids.get().serialize(output);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		if (processTick <= 0 && singleResourceCapabilityUtil.amount(fluids) > 0) {
			transactionUtil.execute(transaction -> singleResourceCapabilityUtil.extract(fluids, 1, transaction));
			processTick = 40;
		} else if (processTick > 0) {
			processTick--;
			if (processTick <= 0) {
				ClientPacketSendParticles packet = new ClientPacketSendParticles();
				AtomicBoolean process = new AtomicBoolean(false);
				List<ServerPlayer> players = new ArrayList<>();
				level.getEntities((Entity) null, new AABB(blockPos).inflate(6D), e -> true).forEach(e -> {
					if (e instanceof ServerPlayer serverPlayer)
						players.add(serverPlayer);
					if (!(e instanceof ItemEntity item) || !(level instanceof ServerLevel serverLevel))
						return;
					for (Direction face : Direction.values()) {
						ResourceHandler<ItemResource> other = capabilityCache.get(Capabilities.Item.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
						if (other != null) {
							int consumed = transactionUtil.execute(transaction -> other.insert(ItemResource.of(item.getItem()), item.getItem().count(), transaction)).orElse(0);
							if (consumed == 0) {
								continue;
							}
							item.playSound(SoundEvents.ENDERMAN_TELEPORT, 0.25F, 1F);
							if (consumed == item.getItem().count())
								item.discard();
							else
								item.getItem().shrink(consumed);
							process.set(true);
							for (int i = 0; i < 20; i++) {
								Vec3 pos = new Vec3(0.5D, 0, 0)
									.yRot((float) Math.toRadians(level.getRandom().nextInt(360)))
									.scale(0.2F + level.getRandom().nextFloat() * 0.8F)
									.add(e.position());
								packet.queueParticle(ParticleTypes.PORTAL, pos.x(), pos.y(), pos.z(), 0, 0, 0);
							}
							break;
						}
					}
				});
				if (process.get()) {
					players.forEach(advancementTriggers.COLLECTOR_TRIGGER.get()::trigger);
					if (level instanceof ServerLevel serverLevel)
						PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(blockPos), packet);
				} else {
					processTick = 80;
				}
			}
		}
	}

}
