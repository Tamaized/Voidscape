package tamaized.voidscape.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.util.LevelUtil;

import java.util.Map;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	@Autowired
	private static ModArmorSetComponentDirectory armor;

	@Autowired
	private static ModItemComponents components;

	@Autowired
	private static ModDataAttachments dataAttachments;

	@Autowired
	private static LevelUtil levelUtil;

	/**
	 * {@link tamaized.voidscape.coremod.transformers.elytra.DisableCapeRenderTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.CapeLayer#render(PoseStack, MultiBufferSource, int, AbstractClientPlayer, float, float, float, float, float, float)}<br>
	 */
	public static boolean disableCapeRender(boolean o, ItemStack stack) {
		return o || stack.is(armor.corruptArmorSet().CORRUPT_CHEST.get()) || stack.getOrDefault(components.ELYTRA, false);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.elytra.ShouldRenderElytraTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.layers.ElytraLayer#shouldRender(ItemStack, LivingEntity)}<br>
	 */
	public static boolean shouldRenderElytra(boolean o, ItemStack stack) {
		return o || stack.getOrDefault(components.ELYTRA, false);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTransparencyTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#render(LivingEntity, float, float, PoseStack, MultiBufferSource, int)}<br>
	 */
	public static float modifyEntityTransparency(float alpha, LivingEntity entity) {
		return Math.min(Mth.clamp(1F - entity.getData(dataAttachments.INSANITY).getInfusion() / 600F, 0F, 1F), alpha);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 */
	public static RenderType modifyEntityRenderType(RenderType type, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity entity) {
		return entity.level() != null && levelUtil.isInVoidDimension(entity.level()) && !(entity instanceof IEthereal) ?
			RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) :
			type;
	}

	/**
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(LevelReader, BlockPos)} and {@link Biome#shouldFreeze(LevelReader, BlockPos, boolean)}<br>
	 * [BEFORE EACH IRETURN]
	 */
	public static boolean shouldBiomeHaveSnowfallAndLiquidFreeze(boolean o, Biome biome, LevelReader level) {
		if (!o) // Short-circuit
			return false;

		RegistryAccess registryAccess = level instanceof ServerLevel serverLevel ? serverLevel.registryAccess() :
			level instanceof WorldGenRegion worldGenRegion ? worldGenRegion.registryAccess() :
				null;
		return registryAccess == null || !registryAccess.registryOrThrow(Registries.BIOME).getResourceKey(biome)
			.map(key -> key.location().getNamespace().equals(Voidscape.MODID))
			.orElse(false);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#getBrightness(net.minecraft.world.level.dimension.DimensionType, int)}<br>
	 * [BEFORE FRETURN]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static float visibility(float o, int light) {
		if (Voidscape.checkForVoidDimension(Minecraft.getInstance().level))
			return VoidVisibilityCache.value(o, light);
		return o;
	}

	*//**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 * [AFTER FIRST FLOAD 9]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static float cancelNightVision(float o, Level level) {
		if (o > 0 && level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return 0;
		return o;
	}

	*//**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 * [AFTER GETFIELD {@link net.minecraft.client.Options#gamma}]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static float cancelGamma(float o, Level level) {
		if (o > 0 && level.isClientSide() && Voidscape.checkForVoidDimension(level))
			return 0;
		return o;
	}

	*//**
	 * Injection Point:<br>
	 * {@link ModelBakery#ModelBakery(BlockColors, ProfilerFiller, Map, Map)}<br>
	 * [BEFORE FIRST GETSTATIC {@link net.minecraft.core.registries.BuiltInRegistries#ITEM)}]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static void redirectModels(ModelBakery bakery) {
		try {
			ModelBakeListener.redirectModels(bakery);
		} catch (NullPointerException e) {
			// Another mod crashed earlier on, this will throw a NPE when the registry isn't populated, just fail silently and let the game error properly later
		}
	}

	*//**
	 * Injection Point:<br>
	 * {@link ModelBakery#ModelBakery(BlockColors, ProfilerFiller, Map, Map)}<br>
	 * [BEFORE LAST INVOKESTATIC {@link com.google.common.collect.Sets#newHashSet()}]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static void cleanModels(ModelBakery bakery) {
		ModelBakeListener.clearOldModels(bakery);
	}

	*//**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer) all potential methods}<br>
	 * [AFTER ALL INVOKESTATIC {@link ItemStack#is(Item)}]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static boolean isMyBow(boolean o, ItemStack stack, Item item) {
		return o || RegUtil.isMyBow(stack, item);
	}

	*//**
	 * Injection Point:<br>
	 * {@link HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel)}<br>
	 * [AFTER LAST INVOKEVIRTUAL {@link HumanoidArmorLayer#renderModel(PoseStack, MultiBufferSource, int, net.minecraft.world.item.ArmorItem, Model, boolean, float, float, float, ResourceLocation)}]
	 *//*
	@OnlyIn(Dist.CLIENT)
	public static void armorOverlay(HumanoidArmorLayer<?, ?, ?> layer, PoseStack poseStack, MultiBufferSource bufferSource, int light, boolean p_117111_, Model model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		if (RegUtil.isArmorOverlay(stack)) {
			RegUtil.renderingArmorOverlay = true;
			ResourceLocation texture = layer.getArmorResource(entity, stack, slot, "overlay");
			VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(texture), false, false);
			model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			RegUtil.renderingArmorOverlay = false;
		}
	}

	*//**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.GameRenderer#renderLevel(float, long, PoseStack)}<br>
	 * [BEFORE FIRST ASTORE 7]
	 *//*
	@OnlyIn(Dist.CLIENT)
	@Nullable
	public static Entity lockCamera(@Nullable Entity entity) {
		if (entity == null)
			return null;
		CorruptedPawnEntity hunt = entity.getData(ModDataAttachments.INSANITY).getHunter();
		if (hunt != null) {
			entity.lookAt(EntityAnchorArgument.Anchor.EYES, hunt.getEyePosition());
			entity.yRotO = entity.getYRot();
			entity.xRotO = entity.getXRot();
		}
		return entity;
	}*/

}
