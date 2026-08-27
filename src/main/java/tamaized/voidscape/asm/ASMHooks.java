package tamaized.voidscape.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import tamaized.beanification.Autowired;
import tamaized.regutil.ToolAndArmorHelper;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.event.QuiverHandler;
import tamaized.voidscape.registry.ModDataAttachments;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	@Autowired
	private static ModDataAttachments dataAttachments;

	@Autowired
	private static QuiverHandler quiverHandler;

	@Autowired
	private static ToolAndArmorHelper toolAndArmorHelper;

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
		int alpha = (int) (Math.min(Mth.clamp(1F - infusion / 600F, 0F, 1F) * 255F, ARGB.alpha(color)));
		return ARGB.color(alpha, color);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#getRenderType(LivingEntity, boolean, boolean, boolean)}<br>
	 */
	public static RenderType modifyEntityRenderType(RenderType type, LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer, LivingEntity entity) {
		return !(entity instanceof IEthereal) && entity.getData(dataAttachments.INSANITY).getInfusion() > 0F ?
			null : //RenderType.entityTranslucentCull(renderer.getTextureLocation(entity)) : // FIXME
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
		return registryAccess == null || !registryAccess.lookupOrThrow(Registries.BIOME).getResourceKey(biome)
			.map(key -> key.identifier().getNamespace().equals(Voidscape.MODID))
			.orElse(false);
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
		return o || toolAndArmorHelper.isMyBow(stack, item);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.ProjectileWeaponItem#useAmmo(ItemStack, ItemStack, LivingEntity, boolean)} <br>
	 */
	public static ItemStack useAmmo(ItemStack result, ItemStack bow, ItemStack ammo, LivingEntity shooter) {
		return quiverHandler.useAmmo(result, bow, ammo, shooter);
	}

}
