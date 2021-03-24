package tamaized.voidscape;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.Insanity;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.TrackedTurmoilData;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.turmoil.abilities.MageAbilities;
import tamaized.voidscape.turmoil.caps.AggroTable;
import tamaized.voidscape.turmoil.caps.EffectContextCapability;
import tamaized.voidscape.turmoil.caps.FireArrowCapability;
import tamaized.voidscape.turmoil.caps.IAggroTable;
import tamaized.voidscape.turmoil.caps.IEffectContext;
import tamaized.voidscape.turmoil.caps.IFireArrow;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;
import tamaized.voidscape.world.InstanceChunkGenerator;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

import javax.annotation.Nonnull;
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
	public static final RegistryKey<World> WORLD_KEY_VOID = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MODID, "void"));
	public static final SubCapability.ISubCap.SubCapKey<Turmoil> subCapTurmoilData = SubCapability.AttachedSubCap.register(Turmoil.class, Turmoil::new);
	public static final SubCapability.ISubCap.SubCapKey<Insanity> subCapInsanity = SubCapability.AttachedSubCap.register(Insanity.class, Insanity::new);
	public static final SubCapability.ISubCap.SubCapKey<TurmoilStats> subCapTurmoilStats = SubCapability.AttachedSubCap.register(TurmoilStats.class, TurmoilStats::new);
	public static final SubCapability.ISubCap.SubCapKey<TrackedTurmoilData> subCapTurmoilTracked = SubCapability.AttachedSubCap.register(TrackedTurmoilData.class, TrackedTurmoilData::new);

	public Voidscape() {
		StartupMessageManager.addModMessage("Loading Turmoil");
		TurmoilSkill.classload();
		IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus busForge = MinecraftForge.EVENT_BUS;
		RegUtil.register(busMod);
		Registry.register(Registry.BIOME_SOURCE, MODID + ":biomeprovider", VoidscapeSeededBiomeProvider.CODEC);
		busMod.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			NetworkMessages.register(NETWORK);
			Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, "void"), VoidChunkGenerator.codec);
			Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, "instance"), InstanceChunkGenerator.codec);
			CapabilityManager.INSTANCE.register(SubCapability.ISubCap.class, new SubCapability.ISubCap.Storage() {
			}, SubCapability.AttachedSubCap::new);
			CapabilityManager.INSTANCE.register(IFireArrow.class, new SubCapability.ISubCap.DummyStorage<>(), FireArrowCapability::new);
			CapabilityManager.INSTANCE.register(IEffectContext.class, new SubCapability.ISubCap.DummyStorage<>(), EffectContextCapability::new);
			CapabilityManager.INSTANCE.register(IAggroTable.class, new SubCapability.ISubCap.DummyStorage<>(), AggroTable::new);
		});
		busForge.addListener((Consumer<FMLServerStartingEvent>) event ->

				event.getServer().getCommands().getDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("voidscape").
						then(VoidCommands.Debug.register()))

		);
		busForge.addListener((Consumer<TickEvent.PlayerTickEvent>) event -> {
			if (event.player.level != null && !event.player.level.isClientSide() && checkForVoidDimension(event.player.level) && event.player.tickCount % 30 == 0 && event.player.getRandom().nextFloat() <= 0.20F) {
				final int dist = 64;
				final int rad = dist / 2;
				final Supplier<Integer> exec = () -> event.player.getRandom().nextInt(dist) - rad;
				BlockPos dest = event.player.blockPosition().offset(exec.get(), exec.get(), exec.get());
				if (event.player.level.getBlockState(dest).equals(Blocks.BEDROCK.defaultBlockState()))
					event.player.level.setBlockAndUpdate(dest, ModBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState());
			}
		});
		busForge.addListener((Consumer<LivingKnockBackEvent>) event -> {
			if (event.getEntity().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
				event.setCanceled(true);
			}
		});
		busForge.addListener((Consumer<LivingHurtEvent>) event -> {
			if (event.getEntity().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false)) {
				event.setCanceled(true);
				return;
			}
			Entity e = event.getSource() instanceof IndirectEntityDamageSource ? event.getSource().getEntity() : event.getSource().getDirectEntity();
			if (e instanceof LivingEntity && event.getEntityLiving() instanceof MobEntity)
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
							if (event.getEntityLiving().getOffhandItem().isShield(event.getEntityLiving()) && event.getEntityLiving().isBlocking() && cap.
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
				if (e instanceof LivingEntity) {
					LivingEntity attacker = (LivingEntity) e;
					final float dmgPrep = arrow ? attacker.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilData).map(data -> (event.getAmount() + (float) attacker.getAttributeValue(ModAttributes.VOIDIC_ARROW_DMG.get())) * (

							data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_5) ? 1.50F :

									data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_4) ? 1.00F :

											data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_3) ? 0.75F :

													data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_2) ? 0.50F :

															data.hasSkill(TurmoilSkills.MAGE_SKILLS.VOIDIC_ARCHER_1) ? 0.25F :

																	0F

					) * cap.get(Voidscape.subCapTurmoilStats).map(stats -> stats.isActive(MageAbilities.ARROW_IMBUE_SPELLLIKE) ? 1F + (stats.stats().spellpower / 100F) : 1F).
							orElse(1F)).orElse(0F)).orElse(0F) :

							(float) attacker.getAttributeValue(ModAttributes.VOIDIC_DMG.get()) * (attacker instanceof PlayerEntity ? ASMHooks.PlayerEntity_getAttackStrengthScale : 1F) * (attacker.
									hasEffect(ModEffects.SENSE_WEAKNESS.get()) && attacker.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
									map(cap -> cap.context(ModEffects.SENSE_WEAKNESS.get()).map(context -> context.source() == event.getEntity()).orElse(false)).orElse(false) ? 1.5F : 1F);
					final float dmg = dmgPrep == 0 && arrow ? (float) attacker.getAttributeValue(ModAttributes.VOIDIC_ARROW_DMG.get()) : dmgPrep;
					if (dmg > 0) {
						event.getEntity().invulnerableTime = 0;
						event.getEntity().hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(attacker), dmg);
						if (!arrow)
							attacker.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> {
								if (cap.get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.TANK_SKILLS.INSANE_BEAST_1) || data.hasSkill(TurmoilSkills.MELEE_SKILLS.CHAOS_BLADE_1)).
										orElse(false) && attacker.getMainHandItem().getItem() instanceof AxeItem)
									cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.setInsanePower(Math.min(1000, stats.getInsanePower() + (int) dmg * (attacker.
											hasEffect(ModEffects.TUNNEL_VISION.get()) ? attacker.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).
											map(context -> context.context(ModEffects.TUNNEL_VISION.get()).map(c -> c.source() == event.getEntity() ? 2 : 1).orElse(1)).orElse(1) : 1))));
								if (cap.get(Voidscape.subCapTurmoilData).map(data -> data.hasSkill(TurmoilSkills.HEALER_SKILLS.VOIDS_FAVOR_1)).
										orElse(false) && attacker.getMainHandItem().getItem() instanceof SwordItem)
									cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
										stats.setNullPower(Math.min(1000, stats.getNullPower() + (int) (dmg + stats.stats().spellpower * (attacker instanceof PlayerEntity ? ASMHooks.PlayerEntity_getAttackStrengthScale : 1F))));
										if (attacker.hasEffect(ModEffects.EMPOWER_SWORD_OSMOSIS.get())) {
											attacker.removeEffect(ModEffects.EMPOWER_SWORD_OSMOSIS.get());
											stats.setVoidicPower(Math.min(1000, stats.getVoidicPower() + (int) (150F * (stats.stats().spellpower / 100F))));
										}
									});
							});
					}
					applyEffects(event.getEntityLiving(), attacker);
				}
				if (event.getSource().getDirectEntity() instanceof AbstractArrowEntity && event.getSource().getDirectEntity().getCapability(SubCapability.CAPABILITY_FIREARROW).map(IFireArrow::active).orElse(false)) {
					float dmg = event.getEntity().isOnFire() ? event.getAmount() : event.getAmount() * 0.25F;
					event.getEntity().invulnerableTime = 0;
					event.getEntity().hurt(DamageSource.ON_FIRE, dmg);
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
			if (event.getEntity() instanceof AbstractArrowEntity) {
				Entity entity = ((AbstractArrowEntity) event.getEntity()).getOwner();
				if (entity instanceof LivingEntity) {
					LivingEntity shooter = (LivingEntity) entity;
					if (shooter.hasEffect(ModEffects.FIRE_ARROW.get())) {
						if (event.getWorld().random.nextInt(4) == 0)
							event.getEntity().setSecondsOnFire(100);
						event.getEntity().getCapability(SubCapability.CAPABILITY_FIREARROW).ifPresent(IFireArrow::mark);
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
		if (target.level instanceof ServerWorld && ((ServerChunkProvider) target.level.getChunkSource()).getGenerator() instanceof InstanceChunkGenerator && !(target instanceof PlayerEntity))
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
			Vector3d pos = new Vector3d(0.25F + target.getRandom().nextFloat() * 0.75F, 0, 0).
					yRot((float) Math.toRadians(target.getRandom().nextInt(360))).add(target.getX(), target.getEyeY() - 0.5F + target.getRandom().nextFloat(), target.getZ());
			((ServerWorld) caster.level).sendParticles(ParticleTypes.HEART, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
		}
		target.level.getEntities(caster, target.getBoundingBox().inflate(10F), e -> e instanceof MobEntity && e != target).forEach(e -> e.getCapability(SubCapability.CAPABILITY_AGGRO).
				ifPresent(cap -> cap.addHate(caster, calculateHate(heal, caster), true)));
		return true;
	}

	public static double calculateHate(double input, LivingEntity attacker) {
		return input * attacker.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilStats).map(stats -> stats.stats().threat / 100D).orElse(0.0D)).orElse(0.0D);
	}

	public static boolean checkForVoidDimension(World world) {
		return world.dimension().location().equals(WORLD_KEY_VOID.location());
	}

	public static ServerWorld getWorld(World world, RegistryKey<World> dest) {
		return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getLevel(dest));
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T getNull() {
		return null;
	}

	public static RayTraceResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range) {
		return getHitResultFromEyes(entity, predicate, range, 0, 0);
	}

	public static RayTraceResult getHitResultFromEyes(LivingEntity entity, Predicate<Entity> predicate, double range, double inflateXZ, double inflateY) {
		Vector3d vector3d = entity.getEyePosition(1F);
		Vector3d vector3d1 = entity.getViewVector(1.0F);
		Vector3d vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(range)).inflate(1D, 1D, 1D);
		RayTraceResult raytraceresult = entity.level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
		if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
			vector3d2 = raytraceresult.getLocation();
		}
		RayTraceResult ray = getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, predicate, range * range, inflateXZ, inflateY);
		return ray == null ? raytraceresult : ray;
	}

	@Nullable
	private static EntityRayTraceResult getEntityHitResult(Entity shooter, Vector3d startVec, Vector3d endVec, AxisAlignedBB boundingBox, Predicate<Entity> filter, double distance, double inflateXZ, double inflateY) {
		World world = shooter.level;
		double d0 = distance;
		Entity entity = null;
		Vector3d vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius()).inflate(inflateXZ, inflateY, inflateXZ);
			Optional<Vector3d> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d vector3d1 = optional.get();
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

		return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
	}

}
