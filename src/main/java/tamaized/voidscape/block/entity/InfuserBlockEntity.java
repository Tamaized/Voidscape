package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.beanification.BeanContext;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.FilteredFluidStacksResourceHandler;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.concurrent.atomic.AtomicBoolean;

@Configurable
public class InfuserBlockEntity extends TickableBlockEntity {

	private static final Lazy<ModBlockEntities> blockEntities = BeanContext.injectLazy(ModBlockEntities.class);

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModDamageSource damageSource;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private FakePlayers fakePlayers;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.get().INFUSER.get(), (object, _) -> object.fluids);
	}

	public final FluidStacksResourceHandler fluids = new FilteredFluidStacksResourceHandler(1, 10000, (_, resource) -> resource.is(modFluids.VOIDIC_SOURCE.get()));

	private int processTick;

	public InfuserBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.get().INFUSER.get(), pPos, pBlockState);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTick = input.getIntOr("processTick", 0);
		fluids.deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		fluids.serialize(output);
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
				level.getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(blockPos).inflate(6D), _ -> true).forEach(e -> {
					Insanity data = e.getData(dataAttachments.INSANITY);
					if (data.getInfusion() < 200) {
						data.addInfusion(250, e);
					}
					if (level instanceof ServerLevel serverLevel) {
						FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, fakePlayers.INFUSER);
						fakePlayer.snapTo(blockPos, 0, 0);
						e.hurtServer(serverLevel, damageSource.getEntityDamageSource(level, DamageTypes.GENERIC, fakePlayer), 3);
						e.hurtServer(serverLevel, damageSource.getEntityDamageSource(level, damageSource.VOIDIC, fakePlayer), 3);
					}
					if (e instanceof ServerPlayer player)
						advancementTriggers.INFUSER_TRIGGER.get().trigger(player);
					process.set(true);
					for (int i = 0; i < 15; i++) {
						Vec3 pos = new Vec3(1.0D, 0, 0)
							.yRot((float) Math.toRadians(level.getRandom().nextInt(360)))
							.scale(0.2F + level.getRandom().nextFloat() * 0.8F)
							.add(e.position().add(0, e.getBbHeight() / 2F, 0));
						packet.queueParticle(new ParticleTypeSpellCloud.Options(0x7700FF), pos.x(), pos.y(), pos.z(), 0, 0, 0);
					}
				});
				if (process.get()) {
					if (level instanceof ServerLevel serverLevel)
						PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(blockPos), packet);
				} else {
					processTick = 80;
				}
			}
		}
	}

}
