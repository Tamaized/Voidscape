package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;

import java.util.concurrent.atomic.AtomicBoolean;

public class InfuserBlockEntity extends BlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static ModDamageSource damageSource;

	@Autowired
	private static ModDataAttachments dataAttachments;

	@Autowired
	private static FakePlayers fakePlayers;

	@Autowired
	private static ModFluids modFluids;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntities.INFUSER.get(), (object, context) -> object.fluids);
	}

	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == modFluids.VOIDIC_SOURCE.get());

	private int processTick;

	public InfuserBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.INFUSER.get(), pPos, pBlockState);
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
		if (!(be instanceof InfuserBlockEntity entity) || level.hasNeighborSignal(blockPos))
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
				level.getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(blockPos).inflate(6D), e -> true).forEach(e -> {
					Insanity data = e.getData(dataAttachments.INSANITY);
					if (data.getInfusion() < 200) {
						data.addInfusion(250, e);
					}
					if (level instanceof ServerLevel serverLevel) {
						FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, fakePlayers.INFUSER);
						fakePlayer.moveTo(blockPos, 0, 0);
						e.hurt(damageSource.getEntityDamageSource(level, damageSource.VOIDIC, fakePlayer), 6);
					}
					if (e instanceof ServerPlayer player)
						advancementTriggers.INFUSER_TRIGGER.get().trigger(player);
					process.set(true);
					for (int i = 0; i < 15; i++) {
						Vec3 pos = new Vec3(1.0D, 0, 0)
							.yRot((float) Math.toRadians(level.getRandom().nextInt(360)))
							.scale(0.2F + level.getRandom().nextFloat() * 0.8F)
							.add(e.position().add(0, e.getBbHeight() / 2F, 0));
						packet.queueParticle(new ParticleTypeSpellCloud.Options(0x7700FF), false, pos.x(), pos.y(), pos.z(), 0, 0, 0);
					}
				});
				if (process.get()) {
					if (level instanceof ServerLevel serverLevel)
						PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(blockPos), packet);
				} else {
					entity.processTick = 80;
				}
			}
		}
	}

}
