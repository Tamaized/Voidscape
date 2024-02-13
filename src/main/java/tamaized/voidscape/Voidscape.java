package tamaized.voidscape;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.client.ClientInitiator;
import tamaized.voidscape.client.ConfigScreen;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.*;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidscapeLayeredBiomeProvider;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod(Voidscape.MODID)
public class Voidscape {

	public static final String MODID = "voidscape";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ResourceKey<Level> WORLD_KEY_VOID = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "void"));

	public static final GameProfile FAKE_PLAYER = new GameProfile(UUID.fromString("4B63F35E-2AA1-4BC2-8D13-A3F32C9D8380"), "[Voidscape]");

	public Voidscape(IEventBus busMod) {
		if (FMLEnvironment.dist == Dist.CLIENT)
			ClientInitiator.call(busMod);

		DonatorHandler.start();

		IEventBus busForge = NeoForge.EVENT_BUS;

		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(ConfigScreen::new));
		{
			final Pair<Config.Client, ModConfigSpec> specPairClient = new ModConfigSpec.Builder().configure(Config.Client::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPairClient.getRight());
			Config.CLIENT_CONFIG = specPairClient.getLeft();

			final Pair<Config.Common, ModConfigSpec> specPairCommon = new ModConfigSpec.Builder().configure(Config.Common::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPairCommon.getRight());
			Config.COMMON_CONFIG = specPairCommon.getLeft();
		}

		RegUtil.setup(MODID, busMod,
				ModAdvancementTriggers::new,
				ModArmors::new,
				ModAttributes::new,
				ModBlocks::new,
				ModBlockEntities::new,
				ModCreativeTabs::new,
				ModDamageSource::new,
				ModDataAttachments::new,
				ModDataSerializers::new,
				ModEffects::new,
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

		NetworkMessages.register(busMod);

		busMod.addListener(AddPackFindersEvent.class, event -> {
			if (event.getPackType() == PackType.SERVER_DATA && ModList.get().isLoaded("aether")) {
				Path resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("data", "minecraft", "datapacks", "voidscape_aether_compat");
				Pack pack = Pack.readMetaAndCreate(
						"voidscape_aether_compat",
						Component.literal("Voidscape Aether Integration"),
						true,
						BuiltInPackSource.fromName(name -> new PathPackResources(name, resourcePath, false)),
						PackType.SERVER_DATA,
						Pack.Position.TOP,
						PackSource.FEATURE
				);
				event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
			}
		});

		busMod.addListener(RegisterEvent.class, event -> {
			if (!Objects.equals(event.getRegistryKey(), Registries.RECIPE_SERIALIZER))
				return;
			Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(MODID, "biomeprovider"), VoidscapeLayeredBiomeProvider.CODEC);
			Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(MODID, "void"), VoidChunkGenerator.codec);
		});

		busForge.addListener(ServerStartingEvent.class, event ->
				event.getServer().getCommands().getDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("voidscape").
						then(VoidCommands.Debug.register()))
		);

		busForge.addListener(TickEvent.PlayerTickEvent.class, event -> {
			if (event.player.level() != null && !event.player.isSpectator() && checkForVoidDimension(event.player.level())) {
				if ((!event.player.level().isClientSide() || event.player.getData(ModDataAttachments.INSANITY).getParanoia() / 600F > 0.25F) &&

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
					if (event.player.level().getBlockState(dest).isAir() && ModBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState().canSurvive(event.player.level(), dest))
						event.player.level().setBlockAndUpdate(dest, switch (event.player.level().getBiome(dest).unwrapKey().map(ResourceKey::location).orElse(new ResourceLocation("")).getPath()) {
							default -> ModBlocks.ETHEREAL_FRUIT_VOID.get().defaultBlockState();
							case "null" -> ModBlocks.ETHEREAL_FRUIT_NULL.get().defaultBlockState();
							case "overworld" -> ModBlocks.ETHEREAL_FRUIT_OVERWORLD.get().defaultBlockState();
							case "nether" -> ModBlocks.ETHEREAL_FRUIT_NETHER.get().defaultBlockState();
							case "end" -> ModBlocks.ETHEREAL_FRUIT_END.get().defaultBlockState();
					});
				}
			}
		});

		busForge.addListener(LivingHurtEvent.class, event -> {
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
					float voidic = arrowEntity.getData(ModDataAttachments.VOIDIC_ARROW);
					if (voidic > 0) {
						if (event.getEntity().getHealth() <= event.getAmount())
							return;
						event.getEntity().invulnerableTime = 0;
						event.getEntity().hurt(ModDamageSource.getEntityDamageSource(arrowEntity.level(), ModDamageSource.VOIDIC, arrowEntity.getOwner()), voidic);
					}
				}
			} else if (event.getSource().is(ModDamageSource.VOIDIC)) {
				if (event.getEntity().hasEffect(ModEffects.ICHOR.get())) {
					event.setAmount(event.getAmount() * 2F);
				}
				if (event.getEntity().hasEffect(ModEffects.FORTIFIED.get())) {
					event.setAmount(event.getAmount() * 0.25F);
					if (event.getEntity().getRandom().nextInt(4) == 0) {
						event.getEntity().removeEffect(ModEffects.FORTIFIED.get());
					}
				}
				AttributeInstance attributeInstance = event.getEntity().getAttribute(ModAttributes.VOIDIC_RES.get());
				if (attributeInstance != null) {
					float res = (float) attributeInstance.getValue();
					if (res != 0)
						event.setAmount(event.getAmount() - res);
				}
			}
		});

		busForge.addListener(EntityJoinLevelEvent.class, event -> {
			if (event.getEntity() instanceof AbstractArrow arrow) {
				Entity entity = arrow.getOwner();
				if (entity instanceof LivingEntity shooter) {
					if (shooter.getMainHandItem().isEmpty() || !shooter.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ModAttributes.VOIDIC_ARROW_DMG.get()))
						return;
					float voidic = (float) shooter.getAttributeValue(ModAttributes.VOIDIC_ARROW_DMG.get());
					if (voidic > 0)
						arrow.setData(ModDataAttachments.VOIDIC_ARROW, voidic);
				}
			}
		});

		busForge.addListener(MobSpawnEvent.SpawnPlacementCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && Voidscape.checkForVoidDimension(event.getLevel().getLevel()) && event.getLevel().getLightEmission(event.getPos()) <= 7) {
				event.setResult(Event.Result.ALLOW);
			}
		});

		busForge.addListener(MobSpawnEvent.PositionCheck.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL && Voidscape.checkForVoidDimension(event.getLevel().getLevel())) {
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

		busForge.addListener(MobSpawnEvent.FinalizeSpawn.class, event -> {
			if (event.getSpawnType() == MobSpawnType.NATURAL &&
					!(event.getEntity() instanceof IEthereal) &&
					Voidscape.checkForVoidDimension(event.getEntity().level())) {
				event.getEntity().getData(ModDataAttachments.INSANITY).addInfusion(event.getEntity().getRandom().nextInt(200) + 100, event.getEntity());
			}
		});
	}

	private static boolean isValidPositionForMob(ServerLevel serverWorld_, Mob mobEntity_, double double_, BlockPos pos) {
		if (double_ > (double) (mobEntity_.getType().getCategory().getDespawnDistance() * mobEntity_.getType().getCategory().getDespawnDistance()) && mobEntity_.removeWhenFarAway(double_)) {
			return false;
		} else {
			return mobEntity_.checkSpawnObstruction(serverWorld_) &&
					(!(mobEntity_ instanceof Zoglin || mobEntity_ instanceof IEthereal) || NaturalSpawner.canSpawnAtBody(SpawnPlacements.Type.ON_GROUND, serverWorld_, pos, mobEntity_.getType()));
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

	public static Optional<ServerLevel> getLevel(Level level, ResourceKey<Level> dest) {
		return Optional.ofNullable(Objects.requireNonNull(level.getServer()).getLevel(dest));
	}

	public static Optional<ServerLevel> getPlayersSpawnLevel(ServerPlayer player) {
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
