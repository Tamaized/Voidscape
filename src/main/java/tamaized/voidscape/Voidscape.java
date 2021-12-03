package tamaized.voidscape;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.client.ClientInitiator;
import tamaized.voidscape.client.ConfigScreen;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.BindData;
import tamaized.voidscape.turmoil.DonatorData;
import tamaized.voidscape.turmoil.Insanity;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.TrackedTurmoilData;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.turmoil.abilities.MageAbilities;
import tamaized.voidscape.turmoil.caps.IVoidicArrow;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;
import tamaized.voidscape.world.InstanceChunkGenerator;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

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
	public static final ResourceKey<Level> WORLD_KEY_VOID = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MODID, "void"));
	public static final SubCapability.ISubCap.SubCapKey<Turmoil> subCapTurmoilData = SubCapability.AttachedSubCap.register(Turmoil.class, Turmoil::new);
	public static final SubCapability.ISubCap.SubCapKey<Insanity> subCapInsanity = SubCapability.AttachedSubCap.register(Insanity.class, Insanity::new);
	public static final SubCapability.ISubCap.SubCapKey<TurmoilStats> subCapTurmoilStats = SubCapability.AttachedSubCap.register(TurmoilStats.class, TurmoilStats::new);
	public static final SubCapability.ISubCap.SubCapKey<TrackedTurmoilData> subCapTurmoilTracked = SubCapability.AttachedSubCap.register(TrackedTurmoilData.class, TrackedTurmoilData::new);
	public static final SubCapability.ISubCap.SubCapKey<BindData> subCapBind = SubCapability.AttachedSubCap.register(BindData.class, BindData::new);
	public static final SubCapability.ISubCap.SubCapKey<DonatorData> subCapDonatorData = SubCapability.AttachedSubCap.register(DonatorData.class, DonatorData::new);

	public Voidscape() {
		StartupMessageManager.addModMessage("Loading Turmoil");
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInitiator::call);
		DonatorHandler.start();
		TurmoilSkill.classload();
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(ConfigScreen::new));
		{
			final Pair<Config.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
			Config.CLIENT_CONFIG = specPair.getLeft();
		}
		RegUtil.register(busMod);
		Registry.register(Registry.BIOME_SOURCE, MODID + ":biomeprovider", VoidscapeSeededBiomeProvider.CODEC);
		SubCapability.init(busMod);
		busMod.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			NetworkMessages.register(NETWORK);
			Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, "void"), VoidChunkGenerator.codec);
			Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, "instance"), InstanceChunkGenerator.codec);
		});
		busForge.addListener((Consumer<ServerStartingEvent>) event ->

				event.getServer().getCommands().getDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("voidscape").
						then(VoidCommands.Debug.register()))

		);
		busForge.addListener((Consumer<TickEvent.WorldTickEvent>) event -> {
			if (event.phase == TickEvent.Phase.START)
				return;
			if (event.world instanceof ServerLevel)
				((ServerLevel) event.world).getAllEntities().forEach(e -> e.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(data -> data.tick(e))));
		});
		busForge.addListener((Consumer<AttackEntityEvent>) event -> event.getPlayer().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapBind).ifPresent(data -> {
			if (data.isBound())
				event.setCanceled(true);
		})));
		busForge.addListener((Consumer<TickEvent.PlayerTickEvent>) event -> {
			if (event.player.level != null && !event.player.isSpectator() && checkForVoidDimension(event.player.level)) {
				if ((!event.player.level.isClientSide() || event.player.getCapability(SubCapability.CAPABILITY).
						map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> data.getParanoia() / 600F > 0.25F).orElse(false)).orElse(false)) &&

						event.player.tickCount % 30 == 0 &&

						event.player.getRandom().nextFloat() <= 0.20F) {
					final int dist = 64;
					final int rad = dist / 2;
					final Supplier<Integer> exec = () -> event.player.getRandom().nextInt(dist) - rad;
					BlockPos dest = event.player.blockPosition().offset(exec.get(), exec.get(), exec.get());
					if (event.player.level.getBlockState(dest).equals(Blocks.BEDROCK.defaultBlockState()))
						event.player.level.setBlockAndUpdate(dest, ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
				}
				if (!event.player.level.isClientSide() && event.player.tickCount % 15 == 0 && event.player.getRandom().nextFloat() <= 0.15F) {
					final int dist = 64;
					final int rad = dist / 2;
					final Supplier<Integer> exec = () -> event.player.getRandom().nextInt(dist) - rad;
					BlockPos dest = event.player.blockPosition().offset(exec.get(), exec.get(), exec.get());
					if (event.player.level.getBlockState(dest).equals(Blocks.BEDROCK.defaultBlockState()) && event.player.level.getBlockState(dest.above()).isAir())
						event.player.level.setBlockAndUpdate(dest.above(), BlockEtherealPlant.biomeState(ModBlocks.PLANT.get().defaultBlockState(), event.player.level.getBiome(dest).getRegistryName()));
				}
			}
		});
		busForge.addListener((Consumer<LivingKnockBackEvent>) event -> {
			if (event.getEntity().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
				event.setCanceled(true);
			}
		});
		busForge.addListener((Consumer<LivingAttackEvent>) event -> {
			if (event.getEntityLiving().isAlive() && ModDamageSource.check(ModDamageSource.ID_VOIDIC, event.getSource()))
				event.getEntityLiving().invulnerableTime = 0;
		});
		busForge.addListener((Consumer<LivingHurtEvent>) event -> {
			if (event.getEntity().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
				event.setCanceled(true);
				return;
			}
			Entity e = event.getSource() instanceof IndirectEntityDamageSource ? event.getSource().getEntity() : event.getSource().getDirectEntity();
			if (e instanceof LivingEntity && event.getEntityLiving() instanceof Mob)
				event.getEntityLiving().getCapability(SubCapability.CAPABILITY_AGGRO).ifPresent(cap -> cap.
						addHate((LivingEntity) e, calculateHate(event.getAmount(), (LivingEntity) e) * (((LivingEntity) e).
								hasEffect(ModEffects.TUNNEL_VISION.get()) && e.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
								map(context -> context.context(ModEffects.TUNNEL_VISION.get()).map(c -> c.source() == event.getEntity()).orElse(false)).orElse(false) ? 1.1F : 1F), false));
			Boolean arrow;
			if (ModDamageSource.check(ModDamageSource.ID_VOIDIC, event.getSource())) {
				event.getEntityLiving().getCapability(SubCapability.CAPABILITY).
						ifPresent(cap -> {
							if (event.getEntityLiving().getMainHandItem().getItem() instanceof AxeItem && cap.
									get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.TANK_SKILLS.INSANE_BEAST_1)).orElse(false))
								cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.
										setInsanePower(Math.min(1000, stats.getInsanePower() + (int) event.getAmount() * (event.getEntityLiving().
												hasEffect(ModEffects.TUNNEL_VISION.get()) ? event.getEntity().getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
												map(context -> context.context(ModEffects.TUNNEL_VISION.get()).map(c -> c.source() == e ? 2 : 1).orElse(1)).orElse(1) : 1))));
							if (event.getEntityLiving().getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) && event.getEntityLiving().isBlocking() && cap.
									get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.TANK_SKILLS.TACTICIAN_1)).orElse(false)) {
								cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
									stats.setNullPower(Math.min(1000, stats.getNullPower() + (int) event.getAmount() * (event.getEntityLiving().
											hasEffect(ModEffects.EMPOWER_SHIELD_2X_NULL.get()) ? 2 : 1)));
									event.getEntityLiving().removeEffect(ModEffects.EMPOWER_SHIELD_2X_NULL.get());
								});
								event.setAmount(event.getAmount() * 0.9F);
							}
						});
				event.setAmount((float) Math.max(0, event.getAmount() - event.getEntityLiving().getAttributeValue(ModAttributes.VOIDIC_RES.get())));
			} else if ((arrow = meleeOrArrowSource(event.getSource())) != null) {
				if (event.getEntityLiving().getHealth() <= event.getAmount())
					return;
				if (e instanceof LivingEntity) {
					LivingEntity attacker = (LivingEntity) e;
					if (!arrow) {
						final float dmg = (float) (attacker.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ModAttributes.VOIDIC_DMG.get()) ?
								attacker.getAttributeValue(ModAttributes.VOIDIC_DMG.get()) : 0) *
								(attacker instanceof Player ? ASMHooks.PlayerEntity_getAttackStrengthScale : 1F) * (attacker.
								hasEffect(ModEffects.SENSE_WEAKNESS.get()) && attacker.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
								map(cap -> cap.context(ModEffects.SENSE_WEAKNESS.get()).map(context -> context.source() == event.getEntity()).orElse(false)).orElse(false) ? 1.5F : 1F);
						if (dmg > 0) {
							event.getEntity().invulnerableTime = 0;
							event.getEntity().hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(attacker), dmg);
							attacker.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> {
								if (cap.get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.TANK_SKILLS.INSANE_BEAST_1) || data.hasSkill(TurmoilSkills.MELEE_SKILLS.CHAOS_BLADE_1)).
										orElse(false) && attacker.getMainHandItem().getItem() instanceof AxeItem)
									cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.setInsanePower(Math.min(1000, stats.getInsanePower() + (int) dmg * (attacker.
											hasEffect(ModEffects.TUNNEL_VISION.get()) ? attacker.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
											map(context -> context.context(ModEffects.TUNNEL_VISION.get()).map(c -> c.source() == event.getEntity() ? 2 : 1).orElse(1)).orElse(1) : 1))));
								if (cap.get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.HEALER_SKILLS.VOIDS_FAVOR_1)).
										orElse(false) && attacker.getMainHandItem().getItem() instanceof SwordItem)
									cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
										stats.setNullPower(Math.min(1000, stats.getNullPower() + (int) (dmg + stats.stats().spellpower * (attacker instanceof Player ? ASMHooks.PlayerEntity_getAttackStrengthScale : 1F))));
										if (attacker.hasEffect(ModEffects.EMPOWER_SWORD_OSMOSIS.get())) {
											attacker.removeEffect(ModEffects.EMPOWER_SWORD_OSMOSIS.get());
											stats.setVoidicPower(Math.min(1000, stats.getVoidicPower() + (int) (150F * (stats.stats().spellpower / 100F))));
										}
									});
							});
							applyEffects(event.getEntityLiving(), attacker);
						}
					}
				}
				if (event.getSource().getDirectEntity() instanceof AbstractArrow) {
					AbstractArrow arrowEntity = (AbstractArrow) event.getSource().getDirectEntity();
					arrowEntity.getCapability(SubCapability.CAPABILITY_VOIDICARROW).ifPresent(data -> {
						float voidic = data.active(IVoidicArrow.ID_VOIDIC);
						if (voidic > 0) {
							if (event.getEntityLiving().getHealth() <= event.getAmount())
								return;
							event.getEntity().invulnerableTime = 0;
							event.getEntity().hurt(arrowEntity.getOwner() instanceof LivingEntity ? ModDamageSource.
									VOIDIC_WITH_ENTITY.apply((LivingEntity) arrowEntity.getOwner()) : ModDamageSource.VOIDIC, voidic);
						}
						float fire = data.active(IVoidicArrow.ID_FIRE);
						if (fire > 0) {
							if (event.getEntityLiving().getHealth() <= event.getAmount())
								return;
							float dmg = arrowEntity.isOnFire() ? fire : fire * 0.25F;
							event.getEntity().invulnerableTime = 0;
							event.getEntity().hurt(DamageSource.ON_FIRE, dmg);
						}
					});
				}
			}
		});
		busForge.addListener((Consumer<LivingHealEvent>) event -> {
			event.getEntity().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> {
				cap.get(subCapTurmoilTracked).ifPresent(data -> {
					if (data.incapacitated)
						event.setCanceled(true);
				});
				cap.get(subCapTurmoilStats).ifPresent(stats -> {
					if (stats.stats().healAmp > 0)
						event.setAmount(event.getAmount() * (1F + stats.stats().healAmp));
				});
			});
		});
		busForge.addListener((Consumer<EntityJoinWorldEvent>) event -> {
			if (event.getEntity() instanceof AbstractArrow) {
				AbstractArrow arrow = (AbstractArrow) event.getEntity();
				Entity entity = arrow.getOwner();
				if (entity instanceof LivingEntity) {
					LivingEntity shooter = (LivingEntity) entity;
					if (shooter.getMainHandItem().isEmpty() || !shooter.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ModAttributes.VOIDIC_ARROW_DMG.get()))
						return;
					LazyOptional<SubCapability.ISubCap> c = shooter.getCapability(SubCapability.CAPABILITY);
					float voidic = (float) (((shooter.getAttributeValue(ModAttributes.VOIDIC_ARROW_DMG.get()) + arrow.getBaseDamage()) * c.
							map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> (
									data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_5) ? 2.00F :

											data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_4) ? 1.75F :

													data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_3) ? 1.50F :

															data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_2) ? 1.25F :

																	data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_1) ? 1.10F :

																			1F)).orElse(1F)).orElse(1F)) * c.map(cap -> cap.get(Voidscape.subCapTurmoilStats).
							map(stats -> stats.isActive(MageAbilities.ARROW_IMBUE_SPELLLIKE) ? 1F + (stats.stats().spellpower / 100F) : 1F).
							orElse(1F)).orElse(1F) - arrow.getBaseDamage());
					if (voidic > 0)
						arrow.getCapability(SubCapability.CAPABILITY_VOIDICARROW).ifPresent(data -> data.mark(IVoidicArrow.ID_VOIDIC, voidic));
					if (shooter.hasEffect(ModEffects.FIRE_ARROW.get())) {
						if (event.getWorld().random.nextInt(4) == 0)
							arrow.setSecondsOnFire(100);
						arrow.getCapability(SubCapability.CAPABILITY_VOIDICARROW).ifPresent(data -> data.mark(IVoidicArrow.ID_FIRE, (float) arrow.getBaseDamage()));
						shooter.removeEffect(ModEffects.FIRE_ARROW.get());
					}
				}
			}
		});
		busForge.addListener((Consumer<PotionEvent.PotionRemoveEvent>) event -> {
			if (ModEffects.hasContext(event.getPotion()))
				event.getEntity().getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).ifPresent(cap -> cap.remove(event.getPotion()));
		});
		busForge.addListener((Consumer<BlockEvent.BreakEvent>) event -> {
			if (event.getPlayer().level.dimension().location().getNamespace().equals(Voidscape.MODID) && event.getPlayer().level.dimension().location().getPath().contains("instance"))
				event.setCanceled(true);
		});
		busForge.addListener((Consumer<LivingSpawnEvent.CheckSpawn>) event -> {
			if (event.getSpawnReason() == MobSpawnType.NATURAL && event.getWorld() instanceof ServerLevel && event.getEntity() instanceof Mob && Voidscape.
					checkForVoidDimension(event.getEntity().level)) {
				Player player = event.getWorld().getNearestPlayer(event.getX(), event.getY(), event.getZ(), -1.0D, false);
				if (player != null && Voidscape.isValidPositionForMob((ServerLevel) event.getWorld(), (Mob) event.getEntity(), player.
						distanceToSqr(event.getX(), event.getY(), event.getZ()), new BlockPos(event.getX(), event.getY(), event.getZ())))
					event.setResult(Event.Result.ALLOW);
				else
					event.setResult(Event.Result.DENY);
			}
		});
		busForge.addListener((Consumer<LivingSpawnEvent.SpecialSpawn>) event -> {
			if (event.getSpawnReason() == MobSpawnType.NATURAL && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof IEthereal) && Voidscape.
					checkForVoidDimension(event.getEntity().level)) {
				event.getEntity().getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> data.
						setInfusion(((LivingEntity) event.getEntity()).getRandom().nextInt(200) + 100)));
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
		if (!(source instanceof EntityDamageSource))
			return null;
		switch (source.getMsgId()) {
			case "player":
			case "mob":
				return false;
			case "arrow":
				return true;
			default:
				return null;
		}
	}

	public static void applyEffects(LivingEntity entity, LivingEntity attacker) {
		if (attacker.hasEffect(ModEffects.EMPOWER_ATTACK_SLICING.get())) {
			attacker.removeEffect(ModEffects.EMPOWER_ATTACK_SLICING.get());
			ModEffects.dot(attacker, entity, ModEffects.EMPOWER_ATTACK_SLICING_DOT.get(), 10 * 20, 0, 1);
		}
		if (attacker.hasEffect(ModEffects.EMPOWER_ATTACK_BLEED.get())) {
			attacker.removeEffect(ModEffects.EMPOWER_ATTACK_BLEED.get());
			ModEffects.dot(attacker, entity, ModEffects.EMPOWER_ATTACK_BLEED_DOT.get(), 15 * 20, 0, 1);
		}
	}

	public static boolean healTargetAndAggro(LivingEntity target, LivingEntity caster, float heal) {
		if (target.level instanceof ServerLevel && ((ServerChunkCache) target.level.getChunkSource()).getGenerator() instanceof InstanceChunkGenerator && !(target instanceof Player))
			return false;
		float val = target.getHealth();
		target.heal(heal);
		if (val == target.getHealth())
			return false;
		caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> {
			if (cap.get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.HEALER_SKILLS.MAD_PRIEST_1)).orElse(false))
				cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.setInsanePower(Math.min(1000, stats.getInsanePower() + (int) (target.getHealth() - val))));

		});
		for (int i = 0; i < 10; i++) {
			Vec3 pos = new Vec3(0.25F + target.getRandom().nextFloat() * 0.75F, 0, 0).
					yRot((float) Math.toRadians(target.getRandom().nextInt(360))).add(target.getX(), target.getEyeY() - 0.5F + target.getRandom().nextFloat(), target.getZ());
			((ServerLevel) caster.level).sendParticles(ParticleTypes.HEART, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
		}
		target.level.getEntities(caster, target.getBoundingBox().inflate(10F), e -> e instanceof Mob && e != target).forEach(e -> e.getCapability(SubCapability.CAPABILITY_AGGRO).
				ifPresent(cap -> cap.addHate(caster, calculateHate(heal, caster), true)));
		return true;
	}

	public static double calculateHate(double input, LivingEntity attacker) {
		return input * attacker.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(stats -> stats.stats().threat / 100D).orElse(0.0D)).orElse(0.0D);
	}

	public static boolean checkForVoidDimension(Level world) {
		return world.dimension().location().equals(WORLD_KEY_VOID.location());
	}

	public static boolean checkForPawnInstance(Level world) {
		ResourceLocation loc = world.dimension().location();
		return loc.getNamespace().equals(Voidscape.MODID) && loc.getPath().contains("_pawn_");
	}

	public static ServerLevel getWorld(Level world, ResourceKey<Level> dest) {
		return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getLevel(dest));
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range) {
		return getHitResultFromEyes(entity, predicate, range, 0, 0);
	}

	public static HitResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range, double inflateXZ, double inflateY) {
		Vec3 vector3d = entity.getEyePosition(1F);
		Vec3 vector3d1 = entity.getViewVector(1.0F);
		Vec3 vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
		AABB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(range)).inflate(1D, 1D, 1D);
		HitResult raytraceresult = entity.level.clip(new ClipContext(vector3d, vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
		if (raytraceresult.getType() != HitResult.Type.MISS) {
			vector3d2 = raytraceresult.getLocation();
		}
		HitResult ray = getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, predicate, range * range, inflateXZ, inflateY);
		return ray == null ? raytraceresult : ray;
	}

	@Nullable
	private static EntityHitResult getEntityHitResult(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, double inflateXZ, double inflateY) {
		Level world = shooter.level;
		double d0 = distance;
		Entity entity = null;
		Vec3 vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius()).inflate(inflateXZ, inflateY, inflateXZ);
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
