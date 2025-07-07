package tamaized.voidscape.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.ClientHooks;
import tamaized.beanification.Autowired;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModArmorSetComponentDirectory;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.util.LevelUtil;

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

	@Autowired(dist = Dist.CLIENT)
	private static VoidVisibilityCache voidVisibilityCache;

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
	public static int modifyEntityTransparency(int color, LivingEntity entity) {
		float infusion = entity.getData(dataAttachments.INSANITY).getInfusion();
		if (infusion <= 0F)
			return color;
		int alpha = (int) (Math.min(Mth.clamp(1F - infusion / 600F, 0F, 1F) * 255F, FastColor.ARGB32.alpha(color)));
		return FastColor.ARGB32.color(alpha, color);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 */
	public static RenderType modifyEntityRenderType(RenderType type, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity entity) {
		return !(entity instanceof IEthereal) && entity.getData(dataAttachments.INSANITY).getInfusion() > 0F ?
			RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) :
			type;
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.biome.BiomeSnowAndFreezeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link Biome#shouldSnow(LevelReader, BlockPos)} and {@link Biome#shouldFreeze(LevelReader, BlockPos, boolean)}<br>
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
	 * {@link tamaized.voidscape.coremod.transformers.visibility.LightTextureBrightnessTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#getBrightness(net.minecraft.world.level.dimension.DimensionType, int)}<br>
	 */
	public static float lightTextureBrightness(float o, int light) {
		if (levelUtil.isInVoidDimension(Minecraft.getInstance().level))
			return voidVisibilityCache.value(o, light);
		return o;
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.visibility.LightTextureNightVisionAndGammaTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LightTexture#updateLightTexture(float)}<br>
	 */
	public static float nightVisionAndGamma(float o, Level level) {
		if (o > 0 && level.isClientSide() && levelUtil.isInVoidDimension(level))
			return 0;
		return o;
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.render.ItemInHandRendererIsBowTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#evaluateWhichHandsToRender(LocalPlayer)}<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#selectionUsingItemWhileHoldingBowLike(LocalPlayer)}<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#isChargedCrossbow(ItemStack)}
	 */
	public static boolean isBowInRenderingHand(boolean o, ItemStack stack, Item item) {
		return o || RegUtil.isMyBow(stack, item);
	}

	/**
	 * Injection Point:<br>
	 * {@link HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel, float, float, float, float, float, float)} <br>
	 */
	public static void armorOverlay(HumanoidArmorLayer<?, ?, ?> layer, ArmorMaterial.Layer armormaterial$layer, PoseStack poseStack, MultiBufferSource bufferSource, int light, Model model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		if (RegUtil.isArmorOverlay(stack)) {
			RegUtil.renderingArmorOverlay = true;
			ResourceLocation texture = ClientHooks.getArmorTexture(entity, stack, armormaterial$layer, true, slot);
			VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(texture), false);
			model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
			RegUtil.renderingArmorOverlay = false;
		}
	}

}
