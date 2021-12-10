package tamaized.voidscape.asm;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.EffectRenderer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModelBakeListener;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.HackyWorldGen;
import tamaized.voidscape.world.InstanceChunkGenerator;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static float PlayerEntity_getAttackStrengthScale;

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.entity.LivingEntity#LivingEntity(EntityType, Level)}<br>
	 * [AFTER] PUTFIELD : attributes
	 */
	public static void handleEntityAttributes(LivingEntity entity) {
		AttributeSupplier.Builder n = AttributeSupplier.builder();
		n.builder.putAll(entity.attributes.supplier.instances);
		ModAttributes.assignAttributes(n);
		entity.attributes = new AttributeMap(n.build());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.CapeLayer#render(PoseStack, MultiBufferSource, int, AbstractClientPlayer, float, float, float, float, float, float)}<br>
	 * [AFTER] INVOKEVIRTUAL {@link ItemStack#is(Item)}
	 */
	public static boolean capeLayer(boolean o, ItemStack stack) {
		return o || stack.is(ModArmors.CORRUPT_CHEST.get()) || ModArmors.elytra(stack);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.ElytraLayer#shouldRender(ItemStack, LivingEntity)}<br>
	 * [BEFORE] IRETURN
	 */
	public static boolean elytraLayer(boolean o, ItemStack stack) {
		return o || ModArmors.elytra(stack);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.LivingEntityRenderer#render(LivingEntity, float, float, PoseStack, MultiBufferSource, int)}<br>
	 * [BEFORE] INVOKEVIRTUAL : {@link net.minecraft.client.model.EntityModel#renderToBuffer(PoseStack, VertexConsumer, int, int, float, float, float, float)}
	 */
	public static float handleEntityTransparency(float alpha, LivingEntity entity) {
		return Math.min(entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> Mth.clamp(1F - data.getInfusion() / 600F, 0F, 1F)).orElse(1F)).orElse(1F), alpha);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.LivingEntityRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 * [AFTER] INVOKEVIRTUAL : {@link net.minecraft.client.model.EntityModel#renderType(ResourceLocation)}
	 */
	@OnlyIn(Dist.CLIENT)
	public static RenderType handleEntityTransparencyRenderType(RenderType type, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity entity) {
		return entity.level != null && Voidscape.checkForVoidDimension(entity.level) && !(entity instanceof IEthereal) ? RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) : type;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerChunkCache#ServerChunkCache(ServerLevel, LevelStorageSource.LevelStorageAccess, DataFixer, StructureManager, Executor, ChunkGenerator, int, boolean, ChunkProgressListener, ChunkStatusUpdateListener, Supplier)}<br>
	 * [AFTER] INVOKESPECIAL : {@link ChunkMap#ChunkMap(ServerLevel, LevelStorageSource.LevelStorageAccess, DataFixer, StructureManager, Executor, BlockableEventLoop, LightChunkGetter, ChunkGenerator, ChunkProgressListener, ChunkStatusUpdateListener, Supplier, int, boolean)}
	 */
	public static ChunkMap chunkManager(ChunkMap old, ServerLevel serverWorld_, LevelStorageSource.LevelStorageAccess levelSave_, DataFixer dataFixer_, StructureManager templateManager_, Executor executor_, BlockableEventLoop<Runnable> threadTaskExecutor_, LightChunkGetter chunkLightProvider_, ChunkGenerator chunkGenerator_, ChunkProgressListener chunkProgressListener, ChunkStatusUpdateListener chunkStatusListener_, Supplier<DimensionDataStorage> supplier_, int int_, boolean boolean_) {
		return chunkGenerator_ instanceof InstanceChunkGenerator ? new HackyWorldGen.DeepFreezeChunkManager(serverWorld_, levelSave_, dataFixer_, templateManager_, executor_, threadTaskExecutor_, chunkLightProvider_, chunkGenerator_, chunkProgressListener, chunkStatusListener_, supplier_, int_, boolean_) : old;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel#ServerLevel(MinecraftServer, Executor, LevelStorageSource.LevelStorageAccess, ServerLevelData, ResourceKey, DimensionType, ChunkProgressListener, ChunkGenerator, boolean, long, List, boolean)}<br>
	 * [AFTER] INVOKESPECIAL : {@link net.minecraft.world.level.chunk.storage.EntityStorage#EntityStorage(ServerLevel, File, DataFixer, boolean, Executor)}
	 */
	public static EntityStorage entityStorage(EntityStorage o, ChunkGenerator chunkGenerator_, ServerLevel level, LevelStorageSource.LevelStorageAccess file) {
		return chunkGenerator_ instanceof InstanceChunkGenerator ? new EntityStorage(level, new File(file.getDimensionPath(level.dimension()), "entities"), level.getServer().getFixerUpper(), level.getServer().forceSynchronousWrites(), level.getServer()) {
			@Override
			public CompletableFuture<ChunkEntities<Entity>> loadEntities(ChunkPos pos) {
				return CompletableFuture.completedFuture(new ChunkEntities<>(pos, ImmutableList.of()));
			}

			@Override
			public void storeEntities(ChunkEntities<Entity> p_156559_) {
				// NO-OP
			}
		} : o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.WorldGenSettings#WorldGenSettings(long, boolean, boolean, MappedRegistry, Optional)} <br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		HackyWorldGen.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.storage.LevelStorageSource#readWorldGenSettings(Dynamic, DataFixer, int)}<br>
	 * [BEFORE FIRST ASTORE]
	 */
	public static Dynamic<Tag> seed(Dynamic<Tag> seed) {
		HackyWorldGen.seed = ((CompoundTag) seed.getValue()).getLong("seed");
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraftforge.common.ForgeHooks#onLivingDeath(LivingEntity, DamageSource)}<br>
	 * [BEFORE FIRST GETSTATIC]
	 */
	public static boolean death(LivingEntity entity, DamageSource source) {
		if (entity instanceof Player) {
			if (Voidscape.checkForVoidDimension(entity.level)) {
				entity.setHealth(entity.getMaxHealth() * 0.1F);
				if (!entity.level.isClientSide())
					entity.getCapability(SubCapability.CAPABILITY).ifPresent(c -> c.get(Voidscape.subCapTurmoilData).
							ifPresent(data -> data.setState(Turmoil.State.TELEPORT)));
				return true;
			} else if (entity.level.dimension().location().getNamespace().equals(Voidscape.MODID) && entity.level.dimension().location().getPath().contains("instance")) {
				entity.setHealth(0.5F);
				if (entity.getY() > 0) {
					entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> {
						data.incapacitated = true;
					}));
				} else if (!entity.level.isClientSide())
					entity.getCapability(SubCapability.CAPABILITY).ifPresent(c -> c.get(Voidscape.subCapTurmoilData).
							ifPresent(data -> data.setState(Turmoil.State.TELEPORT)));
				return true;
			}
		} else {
			if ((source.getDirectEntity() instanceof Player || source.getEntity() instanceof Player) && Voidscape.checkForVoidDimension(entity.level) && entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> data.
					getInfusion() > 200).orElse(false)).orElse(false) && entity.getRandom().nextInt(3) == 0) {
				Containers.dropItemStack(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.ETHEREAL_ESSENCE.get()));
			}
		}
		return MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entity, source));
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraftforge.common.ForgeHooks#enhanceBiome(ResourceLocation, Biome.ClimateSettings, Biome.BiomeCategory, Float, Float, BiomeSpecialEffects, BiomeGenerationSettings, MobSpawnSettings, RecordCodecBuilder.Instance, ForgeHooks.BiomeCallbackFunction)}<br>
	 * [AFTER GETSTATIC {@link MinecraftForge.EVENT_BUS}]
	 */
	public static IEventBus fukUrBiomeEdits(IEventBus o, BiomeLoadingEvent event) {
		return event.getName() != null && event.getName().getNamespace().equals(Voidscape.MODID) ? NoOpEventBus.INSTANCE : o;
	}

	/**
	 * Injection Point:<br>
	 * {@link Player#attack(Entity)}<br>
	 * [AFTER INVOKEVIRTUAL {@link Player#getAttackStrengthScale(float)}]
	 */
	public static synchronized float getAttackStrengthScale(float o) {
		PlayerEntity_getAttackStrengthScale = o;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(LevelReader, BlockPos)}<br>
	 * [AFTER ICONST_1]
	 */
	public static boolean shouldSnow(boolean o, Biome biome) {
		if (biome.getRegistryName() != null && biome.getRegistryName().getNamespace().equals(Voidscape.MODID))
			return false;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.enchantment.EnchantmentCategory#canEnchant(Item)}<br>
	 * [BEFORE IRETURN]
	 */
	public static boolean axesRWeps(boolean o, Item i) {
		return o || i instanceof RegUtil.ToolAndArmorHelper.LootingAxe;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#getBrightness(Level, int)}<br>
	 * [BEFORE FRETURN]
	 */
	public static float visibility(float o, Level level, int light) {
		if (level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return VoidVisibilityCache.value(o, light);
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 * [AFTER FIRST FLOAD 6]
	 */
	public static float cancelNightVision(float o, Level level) {
		if (o > 0 && level.isClientSide() && (Voidscape.checkForVoidDimension(level) || Voidscape.checkForPawnInstance(level)))
			return 0;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.resources.model.ModelBakery#processLoading(ProfilerFiller, int)}<br>
	 * [BEFORE GETSTATIC {@link net.minecraft.core.Registry#ITEM)]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void redirectModels() {
		try {
			ModelBakeListener.redirectModels();
		} catch (NullPointerException e) {
			// Another mod crashed earlier on, this will throw a NPE when the registry isnt populated, just fail silently and let the game error properly later
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.resources.model.ModelBakery#processLoading(ProfilerFiller, int)}<br>
	 * [BEFORE INVOKESTATIC {@link com.google.common.collect.Sets#newLinkedHashSet()}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void cleanModels() {
		ModelBakeListener.clearOldModels();
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.GameRenderer#bobHurt(PoseStack, float)}<br>
	 * [BEFORE FIRST IFEQ]
	 */
	@OnlyIn(Dist.CLIENT)
	public static boolean cancelBobHurt(boolean o) {
		if (!o) // Short-Circuit
			return false;
		Entity camera = Objects.requireNonNull(Minecraft.getInstance().getCameraEntity());
		return camera.canUpdate() || camera.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapBind).map(bind -> !bind.isBound()).orElse(true)).orElse(true);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.Gui#renderEffects(PoseStack)}<br>
	 * [AFTER INVOKEVIRTUAL {@link EffectRenderer#renderHUDEffect}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void renderEffectHUD(List<Runnable> list, EffectRenderer renderer, MobEffectInstance effect, GuiComponent gui, PoseStack mStack, int x, int y, float z, float alpha) {
		if (effect.getEffect() instanceof ModEffects.StandardEffect) {
			list.remove(list.size() - 1);
			list.add(() -> {
				ModEffects.StandardEffect.hackyRenderPerformanceSkip = false;
				renderer.renderHUDEffect(effect, gui, mStack, x, y, z, alpha);
				ModEffects.StandardEffect.hackyRenderPerformanceSkip = true;
			});
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer) all potential methods}<br>
	 * [AFTER ALL INVOKESTATIC {@link ItemStack#is(Item)}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static boolean isMyBow(boolean o, ItemStack stack, Item item) {
		return o || RegUtil.isMyBow(stack, item);
	}

}
