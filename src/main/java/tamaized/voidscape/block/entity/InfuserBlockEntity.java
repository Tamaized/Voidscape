package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class InfuserBlockEntity extends BlockEntity {

	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	private int processTick;

	public InfuserBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.INFUSER.get(), pPos, pBlockState);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		processTick = pTag.getInt("processTick");
		fluids.readFromNBT(pTag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.putInt("processTick", processTick);
		pTag.put("tank", fluids.writeToNBT(new CompoundTag()));
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
					Insanity data = e.getData(ModDataAttachments.INSANITY);
					if (data.getInfusion() < 200) {
						data.setInfusion(250);
					}
					if (level instanceof ServerLevel serverLevel) {
						FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, Voidscape.FAKE_PLAYER);
						fakePlayer.moveTo(blockPos, 0, 0);
						e.hurt(ModDamageSource.getEntityDamageSource(level, ModDamageSource.VOIDIC, fakePlayer), 6);
					}
					if (e instanceof ServerPlayer player)
						ModAdvancementTriggers.INFUSER_TRIGGER.get().trigger(player);
					process.set(true);
					for (int i = 0; i < 15; i++) {
						Vec3 pos = new Vec3(1.0D, 0, 0)
								.yRot((float) Math.toRadians(level.getRandom().nextInt(360)))
								.scale(0.2F + level.getRandom().nextFloat() * 0.8F)
								.add(e.position().add(0, e.getBbHeight() / 2F, 0));
						packet.queueParticle(new ModParticles.ParticleSpellCloudData(0x7700FF), false, pos.x(), pos.y(), pos.z(), 0, 0, 0);
					}
				});
				if (process.get()) {
					PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(packet);
				} else {
					entity.processTick = 80;
				}
			}
		}
	}

}
