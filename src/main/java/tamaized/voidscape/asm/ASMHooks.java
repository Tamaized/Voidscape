package tamaized.voidscape.asm;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.chunk.IChunkLightProvider;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModelBakeListener;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModItems;
import tamaized.voidscape.registry.RegUtil;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.HackyWorldGen;
import tamaized.voidscape.world.InstanceChunkGenerator;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static float PlayerEntity_getAttackStrengthScale;

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.entity.LivingEntity#LivingEntity(EntityType, World)}<br>
	 * [AFTER] PUTFIELD : attributes
	 */
	public static void handleEntityAttributes(LivingEntity entity) {
		AttributeModifierMap.MutableAttribute n = AttributeModifierMap.builder();
		n.builder.putAll(entity.attributes.supplier.instances);
		n.add(ModAttributes.VOIDIC_VISIBILITY.get(), 1F);
		n.add(ModAttributes.VOIDIC_INFUSION_RES.get(), 1F);
		n.add(ModAttributes.VOIDIC_RES.get(), 0F);
		n.add(ModAttributes.VOIDIC_DMG.get(), 0F);
		n.add(ModAttributes.VOIDIC_ARROW_DMG.get(), 0F);
		entity.attributes = new AttributeModifierManager(n.build());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.LivingRenderer#render(LivingEntity, float, float, MatrixStack, IRenderTypeBuffer, int)}<br>
	 * [BEFORE] INVOKEVIRTUAL : {@link net.minecraft.client.renderer.entity.model.EntityModel#renderToBuffer(MatrixStack, IVertexBuilder, int, int, float, float, float, float)}
	 */
	public static float handleEntityTransparency(float alpha, LivingEntity entity) {
		return Math.min(entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> MathHelper.clamp(1F - data.getInfusion() / 600F, 0F, 1F)).orElse(1F)).orElse(1F), alpha);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.LivingRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 * [AFTER] INVOKEVIRTUAL : {@link net.minecraft.client.renderer.entity.model.EntityModel#renderType(ResourceLocation)}
	 */
	@OnlyIn(Dist.CLIENT)
	public static RenderType handleEntityTransparencyRenderType(RenderType type, LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity entity) {
		return entity.level != null && Voidscape.checkForVoidDimension(entity.level) ? RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) : type;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.server.ServerChunkProvider#ServerChunkProvider(ServerWorld, SaveFormat.LevelSave, DataFixer, TemplateManager, Executor, ChunkGenerator, int, boolean, IChunkStatusListener, Supplier)}<br>
	 * [AFTER] INVOKESPECIAL : {@link ChunkManager#ChunkManager(ServerWorld, SaveFormat.LevelSave, DataFixer, TemplateManager, Executor, ThreadTaskExecutor, IChunkLightProvider, ChunkGenerator, IChunkStatusListener, Supplier, int, boolean)}
	 */
	public static ChunkManager chunkManager(ChunkManager old, ServerWorld serverWorld_, SaveFormat.LevelSave levelSave_, DataFixer dataFixer_, TemplateManager templateManager_, Executor executor_, ThreadTaskExecutor<Runnable> threadTaskExecutor_, IChunkLightProvider chunkLightProvider_, ChunkGenerator chunkGenerator_, IChunkStatusListener chunkStatusListener_, Supplier<DimensionSavedDataManager> supplier_, int int_, boolean boolean_) {
		return chunkGenerator_ instanceof InstanceChunkGenerator ? new HackyWorldGen.DeepFreezeChunkManager(serverWorld_, levelSave_, dataFixer_, templateManager_, executor_, threadTaskExecutor_, chunkLightProvider_, chunkGenerator_, chunkStatusListener_, supplier_, int_, boolean_) : old;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.gen.settings.DimensionGeneratorSettings#DimensionGeneratorSettings(long, boolean, boolean, SimpleRegistry, Optional)}<br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		HackyWorldGen.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraftforge.common.ForgeHooks#onLivingDeath(LivingEntity, DamageSource)}
	 * [BEFORE FIRST GETSTATIC]
	 */
	public static boolean death(LivingEntity entity, DamageSource source) {
		if (entity instanceof PlayerEntity) {
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
			if ((source.getDirectEntity() instanceof PlayerEntity || source.getEntity() instanceof PlayerEntity) && Voidscape.checkForVoidDimension(entity.level) && entity.getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapInsanity).map(data -> data.
					getInfusion() > 200).orElse(false)).orElse(false) && entity.getRandom().nextInt(3) == 0) {
				InventoryHelper.dropItemStack(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.ETHEREAL_ESSENCE.get()));
			}
		}
		return MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entity, source));
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraftforge.common.ForgeHooks#enhanceBiome(ResourceLocation, Biome.Climate, Biome.Category, Float, Float, BiomeAmbience, BiomeGenerationSettings, MobSpawnInfo, RecordCodecBuilder.Instance, ForgeHooks.BiomeCallbackFunction)}
	 * [AFTER NEW {@link BiomeLoadingEvent}]
	 */
	public static Biome fukUrBiomeEdits(BiomeLoadingEvent event, final ForgeHooks.BiomeCallbackFunction callback) {
		if (event.getName() == null || !event.getName().getNamespace().equals(Voidscape.MODID))
			MinecraftForge.EVENT_BUS.post(event);
		return callback.apply(event.getClimate(), event.getCategory(), event.getDepth(), event.getScale(), event.getEffects(), event.getGeneration().build(), event.getSpawns().build()).setRegistryName(event.getName());
	}

	/**
	 * Injection Point:<br>
	 * {@link PlayerEntity#attack(Entity)}
	 * [AFTER INVOKEVIRTUAL {@link PlayerEntity#getAttackStrengthScale(float)}]
	 */
	public static synchronized float getAttackStrengthScale(float o) {
		PlayerEntity_getAttackStrengthScale = o;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(IWorldReader, BlockPos)}
	 * [AFTER ICONST_1]
	 */
	public static boolean shouldSnow(boolean o, Biome biome) {
		if (biome.getRegistryName() != null && biome.getRegistryName().getNamespace().equals(Voidscape.MODID))
			return false;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.enchantment.EnchantmentType.WEAPON#canEnchant(Item)}
	 * [BEFORE IRETURN]
	 */
	public static boolean axesRWeps(boolean o, Item i) {
		return o || i instanceof RegUtil.ToolAndArmorHelper.LootingAxe;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#getBrightness(World, int)}
	 * [BEFORE FRETURN]
	 */
	public static float visibility(float o, World level, int light) {
		if (level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return VoidVisibilityCache.value(o, light);
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.model.ModelBakery#processLoading(IProfiler, int)}
	 * [BEFORE GETSTATIC {@link net.minecraft.util.registry.Registry.ITEM)]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void redirectModels() {
		ModelBakeListener.redirectModels();
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.model.ModelBakery#processLoading(IProfiler, int)}
	 * [BEFORE INVOKESTATIC {@link com.google.common.collect.Sets#newLinkedHashSet()}]
	 */
	@OnlyIn(Dist.CLIENT)
	public static void cleanModels() {
		ModelBakeListener.clearOldModels();
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.GameRenderer#bobHurt(MatrixStack, float)}
	 * [BEFORE FIRST IFEQ]
	 */
	@OnlyIn(Dist.CLIENT)
	public static boolean cancelBobHurt(boolean o) {
		if (!o) // Short-Circuit
			return false;
		Entity camera = Objects.requireNonNull(Minecraft.getInstance().getCameraEntity());
		return ((LivingEntity) camera).hurtTime == 0 || (!camera.canUpdate() && camera.
				getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapBind).map(bind -> !bind.isBound()).orElse(true)).orElse(true));
	}

}
