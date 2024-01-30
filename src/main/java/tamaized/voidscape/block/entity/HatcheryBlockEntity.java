package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModBlockEntities;
import tamaized.voidscape.registry.ModFluids;

public class HatcheryBlockEntity extends BlockEntity {

	public final FluidTank fluids = new FluidTank(100000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	public HatcheryBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.HATCHERY.get(), pPos, pBlockState);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		fluids.readFromNBT(pTag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("tank", fluids.writeToNBT(new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof HatcheryBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		int fluid = entity.fluids.getFluidInTank(0).getAmount();
		if (fluid >= 100000) {
			level.playSound(null, blockPos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4F, (1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F) * 0.7F);
			ClientPacketSendParticles particles = new ClientPacketSendParticles();
			particles.queueParticle(ParticleTypes.EXPLOSION_EMITTER, false, Vec3.atCenterOf(blockPos), Vec3.ZERO);
			PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(particles);
			level.setBlockAndUpdate(blockPos, Blocks.DRAGON_EGG.defaultBlockState());
		} else if (fluid >= 5000 && level.getGameTime() % 10 == 0) {
			ClientPacketSendParticles particles = new ClientPacketSendParticles();
			for (int i = 0; i < (fluid / 5000); i++) {
				if (level.getRandom().nextBoolean())
					particles.queueParticle(ParticleTypes.DRAGON_BREATH, false, Vec3.atCenterOf(blockPos), new Vec3(level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F));
			}
			PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(particles);
		}
	}

}
