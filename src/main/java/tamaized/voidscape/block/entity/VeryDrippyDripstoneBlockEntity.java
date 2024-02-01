package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModBlockEntities;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class VeryDrippyDripstoneBlockEntity extends BlockEntity {

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	private static final MethodHandle handle_method_AbstractCauldronBlock_receiveStalactiteDrip;

	static {
		Method tmp_method_AbstractCauldronBlock_receiveStalactiteDrip;
		MethodHandle tmp_handle_method_AbstractCauldronBlock_receiveStalactiteDrip = null;
		try {
			tmp_method_AbstractCauldronBlock_receiveStalactiteDrip = ObfuscationReflectionHelper.findMethod(Class.forName("net.minecraft.world.level.block.AbstractCauldronBlock"), "receiveStalactiteDrip", BlockState.class, Level.class, BlockPos.class, Fluid.class);
			tmp_handle_method_AbstractCauldronBlock_receiveStalactiteDrip = LOOKUP.unreflect(tmp_method_AbstractCauldronBlock_receiveStalactiteDrip);
		} catch (Throwable e) {
			Voidscape.LOGGER.error("Exception", e);
		}
		handle_method_AbstractCauldronBlock_receiveStalactiteDrip = tmp_handle_method_AbstractCauldronBlock_receiveStalactiteDrip;
	}

	private int tick;

	public VeryDrippyDripstoneBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.VERY_DRIPPY_DRIPSTONE.get(), pPos, pBlockState);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		tick = pTag.getInt("tick");
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.putInt("tick", tick);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof VeryDrippyDripstoneBlockEntity entity))
			return;
		entity.tick++;
		if (entity.tick % 15 == 0 && level.getRandom().nextBoolean()) {
			BlockPos fluidPos = blockPos.above(2);
			if (!level.isOutsideBuildHeight(fluidPos) && !level.getBlockState(blockPos.above()).isAir()) {
				BlockState fluidBlockState = level.getBlockState(fluidPos);
				if (fluidBlockState.is(Blocks.MUD) && !level.dimensionType().ultraWarm()) {
					spawnDripParticles(level, blockPos, blockState, Fluids.WATER);
					if (level.random.nextInt(4) == 0) {
						BlockState clay = Blocks.CLAY.defaultBlockState();
						level.setBlockAndUpdate(fluidPos, clay);
						Block.pushEntitiesUp(fluidBlockState, clay, level, fluidPos);
						level.gameEvent(GameEvent.BLOCK_CHANGE, fluidPos, GameEvent.Context.of(clay));
					}
				} else {
					FluidState fluidState = level.getFluidState(fluidPos);
					if (!fluidState.isEmpty()) {
						Fluid fluid = fluidState.getType();
						spawnDripParticles(level, blockPos, blockState, fluid);
						if (handle_method_AbstractCauldronBlock_receiveStalactiteDrip != null && level.random.nextInt(4) == 0) {
							for (int i = 1; i <= 11; i++) {
								BlockPos cauldronPos = blockPos.below(i);
								if (!level.isOutsideBuildHeight(cauldronPos)) {
									BlockState cauldron = level.getBlockState(cauldronPos);
									if (cauldron.getBlock() instanceof AbstractCauldronBlock cauldronBlock) {
										try {
											handle_method_AbstractCauldronBlock_receiveStalactiteDrip.invokeExact(cauldronBlock, cauldron, level, cauldronPos, fluid);
										} catch (Throwable e) {
											throw new RuntimeException(e);
										}
									}
									if (!cauldron.isAir())
										break;
								} else {
									break;
								}
							}
						}
					}
				}

			}
		}
	}

	private static void spawnDripParticles(Level level, BlockPos pos, BlockState state, Fluid fluid) {
		Vec3 vec3 = state.getOffset(level, pos);
		ClientPacketSendParticles packet = new ClientPacketSendParticles();
		double d1 = (double) pos.getX() + 0.5 + vec3.x;
		double d2 = (double) ((float) (pos.getY() + 1) - 0.6875F) - 0.0625;
		double d3 = (double) pos.getZ() + 0.5 + vec3.z;
		ParticleOptions particleoptions = fluid.getFluidType().getDripInfo() != null ? fluid.getFluidType().getDripInfo().dripParticle() : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
		if (particleoptions != null)
			packet.queueParticle(particleoptions, false, d1, d2, d3, 0, 0, 0);
		PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(pos)).send(packet);
	}

}
