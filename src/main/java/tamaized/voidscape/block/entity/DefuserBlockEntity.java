package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.FilteredFluidStacksResourceHandler;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.concurrent.atomic.AtomicBoolean;

@Configurable
public class DefuserBlockEntity extends TickableBlockEntity {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(ModBlockEntities blockEntities, RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.DEFUSER.get(), (object, _) -> object.fluids);
	}

	public final FluidStacksResourceHandler fluids = new FilteredFluidStacksResourceHandler(1, 10000, (_, resource) -> resource.is(modFluids.VOIDIC_SOURCE.get()));

	private int processTick;

	public DefuserBlockEntity(ModBlockEntities blockEntities, BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.DEFUSER.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTick = input.getIntOr("processTick", 0);
		fluids.deserialize(input.childOrEmpty("fluids"));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		fluids.serialize(output.child("fluids"));
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		if (processTick <= 0 && singleResourceCapabilityUtil.amount(fluids) > 0) {
			transactionUtil.execute(transaction -> singleResourceCapabilityUtil.extract(fluids, FluidResource.of(modFluids.VOIDIC_SOURCE.get()), 1, transaction));
			processTick = 100;
		} else if (processTick > 0) {
			ClientPacketSendParticles packet = new ClientPacketSendParticles();
			AtomicBoolean process = new AtomicBoolean(false);
			AtomicBoolean particle = new AtomicBoolean(false);
			level.getEntities(null, new AABB(blockPos).inflate(32D)).forEach(e -> {
				Insanity data = e.getData(dataAttachments.INSANITY);
				if (data.getInfusion() > 0) {
					data.decrementInfusion(1);
					if (e instanceof ServerPlayer player)
						advancementTriggers.DEFUSER_TRIGGER.get().trigger(player);
					process.set(true);
					Vec3 dir = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() - 0.5D, blockPos.getZ() + 0.5D).subtract(e.position()).normalize().scale(0.15D);
					if (level.getRandom().nextInt(100) == 0) {
						packet.queueParticle(ParticleTypes.END_ROD, e.getX(), e.getY() + e.getBbHeight() / 2F, e.getZ(), dir.x(), dir.y(), dir.z());
						particle.set(true);
					}
				}
			});
			if (process.get()) {
				processTick--;
				if (particle.get() && level instanceof ServerLevel serverLevel)
					PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(blockPos), packet);
			}
		}
	}

}
