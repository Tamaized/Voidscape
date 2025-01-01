package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;

public class HatcheryBlockEntity extends BlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static ModFluids modFluids;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntities.HATCHERY.get(), (object, context) -> object.fluids);
	}

	public final FluidTank fluids = new FluidTank(100000, fluidStack -> fluidStack.getFluid() == modFluids.VOIDIC_SOURCE.get());

	public HatcheryBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.HATCHERY.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		fluids.readFromNBT(registries, tag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("tank", fluids.writeToNBT(registries, new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof HatcheryBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		int fluid = entity.fluids.getFluidInTank(0).getAmount();
		if (fluid >= 100000) {
			level.playSound(null, blockPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4F, (1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F) * 0.7F);
			if (level instanceof ServerLevel serverLevel) {
				ClientPacketSendParticles particles = new ClientPacketSendParticles();
				particles.queueParticle(ParticleTypes.EXPLOSION_EMITTER, false, Vec3.atCenterOf(blockPos), Vec3.ZERO);
				PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(blockPos), particles);
			}
			level.setBlockAndUpdate(blockPos, Blocks.DRAGON_EGG.defaultBlockState());
			level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
					.filter(e -> e instanceof ServerPlayer)
					.map(ServerPlayer.class::cast)
					.forEach(advancementTriggers.HATCHERY_TRIGGER.get()::trigger);
		} else if (fluid >= 5000 && level.getGameTime() % 10 == 0) {
			if (level instanceof ServerLevel serverLevel) {
				ClientPacketSendParticles particles = new ClientPacketSendParticles();
				for (int i = 0; i < (fluid / 5000); i++) {
					if (level.getRandom().nextBoolean())
						particles.queueParticle(ParticleTypes.DRAGON_BREATH, false, Vec3.atCenterOf(blockPos), new Vec3(level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F));
				}
				PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(blockPos), particles);
			}
		}
	}

}
