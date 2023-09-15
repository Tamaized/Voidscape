package tamaized.voidscape;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.capability.DonatorData;
import tamaized.voidscape.capability.Insanity;
import tamaized.voidscape.capability.SubCapability;
import tamaized.voidscape.client.ClientInitiator;
import tamaized.voidscape.client.ConfigScreen;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidscapeLayeredBiomeProvider;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.
			named(new ResourceLocation(MODID, MODID)).
			clientAcceptedVersions(s -> true).
			serverAcceptedVersions(s -> true).
			networkProtocolVersion(() -> "1").
			simpleChannel();

	public static final SubCapability.ISubCap.SubCapKey<Insanity> subCapInsanity = SubCapability.AttachedSubCap.register(Insanity.class, Insanity::new);
	public static final SubCapability.ISubCap.SubCapKey<DonatorData> subCapDonatorData = SubCapability.AttachedSubCap.register(DonatorData.class, DonatorData::new);

	public static final ResourceKey<Level> WORLD_KEY_VOID = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "void"));

	public Voidscape() {
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitiator::call);
		DonatorHandler.start();
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(ConfigScreen::new));
		{
			final Pair<Config.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
			Config.CLIENT_CONFIG = specPair.getLeft();
		}
		RegUtil.setup(MODID, busMod,
				ModArmors::new,
				ModAttributes::new,
				ModBiomes::new,
				ModBlocks::new,
				ModBlockEntities::new,
				ModCreativeTabs::new,
				ModDamageSource::new,
				ModDataSerializers::new,
//				ModEffects::new,
				ModEntities::new,
				ModFeatures::new,
				ModFluids::new,
				ModItems::new,
				ModNoiseGeneratorSettings::new,
				ModParticles::new,
				ModPOIs::new,
				ModSounds::new,
				ModStructures::new,
				ModSurfaceRules::new,
				ModTools::new);
		busMod.addListener((Consumer<RegisterEvent>) event -> {
			if (!Objects.equals(event.getForgeRegistry(), ForgeRegistries.RECIPE_SERIALIZERS))
				return;
			Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(MODID, "biomeprovider"), VoidscapeLayeredBiomeProvider.CODEC);
			Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(MODID, "void"), VoidChunkGenerator.codec);
		});
		SubCapability.init(busMod);
		busMod.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			NetworkMessages.register(NETWORK);
		});
		busForge.addListener((Consumer<ServerStartingEvent>) event ->

				event.getServer().getCommands().getDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("voidscape").
						then(VoidCommands.Debug.register()))

		);
		busForge.addListener((Consumer<TickEvent.PlayerTickEvent>) event -> {
			if (event.player.level() != null && !event.player.isSpectator() && checkForVoidDimension(event.player.level())) {
				if ((!event.player.level().isClientSide() || event.player.getCapability(SubCapability.CAPABILITY).
						map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> data.getParanoia() / 600F > 0.25F).orElse(false)).orElse(false)) &&

						event.player.tickCount % 30 == 0 &&

						event.player.getRandom().nextFloat() <= 0.20F) {
					final int dist = 64;
					final int rad = dist / 2;
					final Supplier<Integer> exec = () -> event.player.getRandom().nextInt(dist) - rad;
					BlockPos dest = event.player.blockPosition().offset(exec.get(), exec.get(), exec.get());
					if (event.player.level().getBlockState(dest).equals(Blocks.BEDROCK.defaultBlockState()))
						event.player.level().setBlockAndUpdate(dest, ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
				}
				if (!event.player.level().isClientSide() && event.player.tickCount % 15 == 0 && event.player.getRandom().nextFloat() <= 0.15F) {
					final int dist = 64;
					final int rad = dist / 2;
					final Supplier<Integer> exec = () -> event.player.getRandom().nextInt(dist) - rad;
					BlockPos dest = event.player.blockPosition().offset(exec.get(), exec.get(), exec.get());
					BlockState state = event.player.level().getBlockState(dest);
					if ((state.equals(Blocks.BEDROCK.defaultBlockState()) || state.equals(ModBlocks.NULL_BLACK.get().defaultBlockState())) && event.player.level().getBlockState(dest.above()).isAir())
						event.player.level().setBlockAndUpdate(dest.above(), BlockEtherealPlant.biomeState(ModBlocks.PLANT.get().defaultBlockState(), event.player.level().getBiome(dest).unwrapKey().map(ResourceKey::location).orElse(new ResourceLocation(""))));
				}
			}
		});
		busForge.addListener((Consumer<LivingAttackEvent>) event -> {
			if (event.getEntity().isAlive() && event.getSource().is(ModDamageSource.VOIDIC))
				event.getEntity().invulnerableTime = 0;
		});
		busForge.addListener((Consumer<LivingHurtEvent>) event -> {
			Boolean arrow;
			if (!event.getSource().is(ModDamageSource.VOIDIC) && (arrow = meleeOrArrowSource(event.getSource())) != null) {
				if (event.getEntity().getHealth() <= event.getAmount())
					return;
				Entity e = event.getSource().isIndirect() ? event.getSource().getEntity() : event.getSource().getDirectEntity();
				if (e instanceof LivingEntity attacker) {
					if (!arrow) {
						final float dmg = (float) (attacker.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ModAttributes.VOIDIC_DMG.get()) ?
								attacker.getAttributeValue(ModAttributes.VOIDIC_DMG.get()) : 0) *
								(attacker instanceof Player ? ASMHooks.PlayerEntity_getAttackStrengthScale : 1F);
						if (dmg > 0) {
							event.getEntity().invulnerableTime = 0;
							event.getEntity().hurt(ModDamageSource.getEntityDamageSource(event.getEntity().level(), ModDamageSource.VOIDIC, attacker), dmg);
						}
					}
				}
				if (event.getSource().getDirectEntity() instanceof AbstractArrow arrowEntity) {
					arrowEntity.getCapability(SubCapability.CAPABILITY_VOIDICARROW).ifPresent(data -> {
						float voidic = data.getDamage();
						if (voidic > 0) {
							if (event.getEntity().getHealth() <= event.getAmount())
								return;
							event.getEntity().invulnerableTime = 0;
							event.getEntity().hurt(ModDamageSource.getEntityDamageSource(arrowEntity.level(), ModDamageSource.VOIDIC, arrowEntity.getOwner()), voidic);
						}
					});
				}
			}
		});
		busForge.addListener((Consumer<EntityJoinLevelEvent>) event -> {
			if (event.getEntity() instanceof AbstractArrow arrow) {
				Entity entity = arrow.getOwner();
				if (entity instanceof LivingEntity shooter) {
					if (shooter.getMainHandItem().isEmpty() || !shooter.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ModAttributes.VOIDIC_ARROW_DMG.get()))
						return;
					float voidic = (float) shooter.getAttributeValue(ModAttributes.VOIDIC_ARROW_DMG.get());
					if (voidic > 0)
						arrow.getCapability(SubCapability.CAPABILITY_VOIDICARROW).ifPresent(data -> data.setDamage(voidic));
				}
			}
		});
		busForge.addListener((Consumer<MobSpawnEvent.PositionCheck>) event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL &&
					Voidscape.checkForVoidDimension(event.getLevel().getLevel())) {
				Player player = event.getLevel().getNearestPlayer(event.getX(), event.getY(), event.getZ(), -1.0D, false);
				if (player != null &&
						Voidscape.isValidPositionForMob(
								event.getLevel().getLevel(),
								event.getEntity(),
								player.distanceToSqr(event.getX(), event.getY(), event.getZ()),
								BlockPos.containing(event.getX(), event.getY(), event.getZ())))
					event.setResult(Event.Result.ALLOW);
				else
					event.setResult(Event.Result.DENY);
			}
		});
		busForge.addListener((Consumer<MobSpawnEvent.FinalizeSpawn>) event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL &&
					!(event.getEntity() instanceof IEthereal) &&
					Voidscape.checkForVoidDimension(event.getEntity().level())) {
				event.getEntity().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> data.
						setInfusion(event.getEntity().getRandom().nextInt(200) + 100)));
			}
		});
	}

	private static boolean isValidPositionForMob(ServerLevel serverWorld_, Mob mobEntity_, double double_, BlockPos pos) {
		if (double_ > (double) (mobEntity_.getType().getCategory().getDespawnDistance() * mobEntity_.getType().getCategory().getDespawnDistance()) && mobEntity_.removeWhenFarAway(double_)) {
			return false;
		} else {
			return mobEntity_.checkSpawnObstruction(serverWorld_) && (!(mobEntity_ instanceof Zoglin) || NaturalSpawner.
					canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, serverWorld_, pos, mobEntity_.getType()));
		}
	}

	@Nullable
	private static Boolean meleeOrArrowSource(DamageSource source) {
		if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))
			return false;
		if (source.is(DamageTypes.ARROW))
			return true;
		return null;
	}

	public static boolean checkForVoidDimension(@Nullable Level level) {
		if (level == null)
			return false;
		return level.dimension().location().equals(WORLD_KEY_VOID.location());
	}

	public static ServerLevel getLevel(Level level, ResourceKey<Level> dest) {
		return Objects.requireNonNull(Objects.requireNonNull(level.getServer()).getLevel(dest));
	}

	public static ServerLevel getPlayersSpawnLevel(ServerPlayer player) {
		return getLevel(player.level(), player.getRespawnDimension());
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range) {
		return getHitResultFromEyes(entity, predicate, range, 0, 0);
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range, double inflateXZ, double inflateY) {
		Vec3 vector3d = entity.getEyePosition(1F);
		Vec3 vector3d1 = entity.getViewVector(1.0F);
		Vec3 vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
		AABB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(range)).inflate(1D, 1D, 1D);
		HitResult raytraceresult = entity.level().clip(new ClipContext(vector3d, vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
		if (raytraceresult.getType() != HitResult.Type.MISS) {
			vector3d2 = raytraceresult.getLocation();
		}
		HitResult ray = getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, predicate, range * range, inflateXZ, inflateY);
		return ray == null ? raytraceresult : ray;
	}

	@Nullable
	private static EntityHitResult getEntityHitResult(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, double inflateXZ, double inflateY) {
		Level world = shooter.level();
		double d0 = distance;
		Entity entity = null;
		Vec3 vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius()).inflate(inflateXZ, inflateY, inflateXZ);
			Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 vector3d1 = optional.get();
				double d1 = startVec.distanceToSqr(vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vector3d = vector3d1;
						}
					} else {
						entity = entity1;
						vector3d = vector3d1;
						d0 = d1;
					}
				}
			}
		}

		return entity == null ? null : new EntityHitResult(entity, vector3d);
	}

}
