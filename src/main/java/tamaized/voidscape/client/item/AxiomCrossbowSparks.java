package tamaized.voidscape.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleGroupsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.particle.AxiomSparkRenderer;
import tamaized.voidscape.client.particle.ParticleAxiomSpark;
import tamaized.voidscape.client.particle.ParticleGroupAxiomSpark;
import tamaized.voidscape.data.AxiomBurst;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModParticles;
import tamaized.voidscape.registry.ModSounds;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiresDist(Dist.CLIENT)
public class AxiomCrossbowSparks {

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModToolSetComponentDirectory toolSets;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModItemComponents itemComponents;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModParticles particles;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModSounds sounds;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private AxiomSparkRenderer sparkRenderer;

	private final ParticleRenderType renderType = new ParticleRenderType(Identifier.fromNamespaceAndPath(Voidscape.MODID, "axiom_spark").toString());
	private final List<ViewSpark> viewSparks = new ArrayList<>();

	@EventBusPostConstruct
	private void setup(IEventBus modBus, IEventBus gameBus) {
		modBus.addListener(this::registerParticleProviders);
		modBus.addListener(this::registerParticleGroups);
		gameBus.addListener(this::tick);
		gameBus.addListener(this::renderHand);
	}

	private void registerParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpecial(particles.AXIOM_SPARK.get(), new ParticleAxiomSpark.Provider(renderType));
	}

	private void registerParticleGroups(RegisterParticleGroupsEvent event) {
		event.register(renderType, ParticleGroupAxiomSpark::new);
	}

	private void tick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (!(player.level() instanceof ClientLevel level))
			return;
		ItemStack stack = player.getMainHandItem();
		AxiomBurst burst = stack.get(itemComponents.AXIOM_BURST.get());
		if (!stack.is(toolSets.axiomToolSet().AXIOM_XBOW) || burst == null || !burst.isActive(level.getGameTime()))
			return;
		RandomSource random = player.getRandom();
		if (random.nextFloat() >= 0.25F)
			return;
		Minecraft minecraft = Minecraft.getInstance();
		float armSign = player.getMainArm() == HumanoidArm.RIGHT ? 1F : -1F;
		if (player == minecraft.getCameraEntity() && minecraft.options.getCameraType().isFirstPerson()) {
			if (minecraft.options.particles().get() != ParticleStatus.MINIMAL) {
				float along = random.nextFloat();
				Vector3f position = new Vector3f(
					armSign * (0.02F + (random.nextFloat() * 2F - 1F) * 0.05F),
					Mth.lerp(along, -0.17F, -0.1F) + (random.nextFloat() * 2F - 1F) * 0.02F,
					-Mth.lerp(along, 0.3F, 0.45F)
				);
				viewSparks.add(new ViewSpark(position, randomOffset(random, 0.05F + random.nextFloat() * 0.05F), random.nextLong(), level.getGameTime()));
			}
		} else {
			Vec3 side = Vec3.directionFromRotation(0F, player.getYRot() + armSign * 90F);
			Vec3 origin = player.getEyePosition()
				.add(player.getLookAngle().scale(0.9D + (random.nextFloat() * 2F - 1F) * 0.1D))
				.add(side.scale(0.3D + (random.nextFloat() * 2F - 1F) * 0.06D))
				.add(0D, -0.3D + (random.nextFloat() * 2F - 1F) * 0.06D, 0D);
			Vector3f offset = randomOffset(random, 0.15F + random.nextFloat() * 0.15F);
			level.addParticle(particles.AXIOM_SPARK.get(), origin.x(), origin.y(), origin.z(), offset.x(), offset.y(), offset.z());
		}
		if (random.nextFloat() < 0.3F)
			level.playLocalSound(player, sounds.AXIOM_SPARK.value(), SoundSource.PLAYERS, 0.15F, 0.5F + random.nextFloat() * 0.5F);
	}

	private void renderHand(RenderHandEvent event) {
		ClientLevel level = Minecraft.getInstance().level;
		if (event.getHand() != InteractionHand.MAIN_HAND || level == null)
			return;
		long gameTime = level.getGameTime();
		viewSparks.removeIf(spark -> gameTime < spark.spawnedAt() || gameTime - spark.spawnedAt() > 3);
		for (ViewSpark spark : viewSparks)
			sparkRenderer.submit(event.getPoseStack(), event.getSubmitNodeCollector(), spark.position(), spark.offset(), spark.seed() + gameTime - spark.spawnedAt());
	}

	private Vector3f randomOffset(RandomSource random, float length) {
		return new Vector3f(random.nextFloat() * 2F - 1F, random.nextFloat() * 2F - 1F, random.nextFloat() * 2F - 1F).normalize(length);
	}

	private record ViewSpark(Vector3fc position, Vector3fc offset, long seed, long spawnedAt) {

	}

}
