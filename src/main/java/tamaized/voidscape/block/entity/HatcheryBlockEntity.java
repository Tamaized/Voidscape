package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.beanification.BeanContext;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.FilteredFluidStacksResourceHandler;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;

@Configurable
public class HatcheryBlockEntity extends TickableBlockEntity {

	private static final Lazy<ModBlockEntities> blockEntities = BeanContext.injectLazy(ModBlockEntities.class);

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.get().HATCHERY.get(), (object, _) -> object.fluids);
	}

	public final FluidStacksResourceHandler fluids = new FilteredFluidStacksResourceHandler(1, 100000, (_, resource) -> resource.is(modFluids.VOIDIC_SOURCE.get()));

	public HatcheryBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.get().HATCHERY.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		fluids.deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		fluids.serialize(output);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		int fluid = singleResourceCapabilityUtil.amount(fluids);
		if (fluid >= 100000) {
			level.playSound(null, blockPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4F, (1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F) * 0.7F);
			if (level instanceof ServerLevel serverLevel) {
				ClientPacketSendParticles particles = new ClientPacketSendParticles();
				particles.queueParticle(ParticleTypes.EXPLOSION_EMITTER, Vec3.atCenterOf(blockPos), Vec3.ZERO);
				PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(blockPos), particles);
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
						particles.queueParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1F), Vec3.atCenterOf(blockPos), new Vec3(level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F, level.getRandom().nextFloat() * 0.0625F - 0.03125F));
				}
				PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(blockPos), particles);
			}
		}
	}

}
