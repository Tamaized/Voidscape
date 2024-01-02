package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModBlockEntities;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModFluids;

import java.util.concurrent.atomic.AtomicBoolean;

public class DefuserBlockEntity extends BlockEntity {

	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	private int processTick;

	public DefuserBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.DEFUSER.get(), pPos, pBlockState);
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
		if (!(be instanceof DefuserBlockEntity entity))
			return;
		IFluidHandler fluid = entity.fluids;
		if (entity.processTick <= 0 && fluid.getFluidInTank(0).getAmount() > 0) {
			fluid.drain(1, IFluidHandler.FluidAction.EXECUTE);
			entity.processTick = 100;
		} else if (entity.processTick > 0) {
			ClientPacketSendParticles packet = new ClientPacketSendParticles();
			AtomicBoolean process = new AtomicBoolean(false);
			AtomicBoolean particle = new AtomicBoolean(false);
			level.getEntities(null, new AABB(blockPos).inflate(32D)).forEach(e -> {
				Insanity data = e.getData(ModDataAttachments.INSANITY);
				if (data.getInfusion() > 0) {
					data.decrementInfusion(1);
					if (e instanceof ServerPlayer player)
						ModAdvancementTriggers.DEFUSER_TRIGGER.get().trigger(player);
					process.set(true);
					Vec3 dir = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() - 0.5D, blockPos.getZ() + 0.5D).subtract(e.position()).normalize().scale(0.15D);
					if (level.getRandom().nextInt(100) == 0) {
						packet.queueParticle(ParticleTypes.END_ROD, false, e.getX(), e.getY() + e.getBbHeight() / 2F, e.getZ(), dir.x(), dir.y(), dir.z());
						particle.set(true);
					}
				}
			});
			if (process.get()) {
				entity.processTick--;
				if (particle.get())
					PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(blockPos)).send(packet);
			}
		}
	}

}
