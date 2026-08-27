package tamaized.voidscape.asm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.client.event.EntityLayerRendererRegistration;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ClientASMHooks {

	@Autowired(dist = Dist.CLIENT)
	private static EntityLayerRendererRegistration entityLayerRendererRegistration;

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTransparencyTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#submit(LivingEntityRenderState, PoseStack, SubmitNodeCollector, CameraRenderState)}<br>
	 */
	public static int modifyEntityTransparency(int color, LivingEntityRenderState state) {
		Float value = state.getRenderData(entityLayerRendererRegistration.CONTEXT_KEY_INFUSION);
		if (value == null || value <= 0F)
			return color;
		int alpha = (int) (Math.min(Mth.clamp(1F - value / 600F, 0F, 1F) * 255F, ARGB.alpha(color)));
		return ARGB.color(alpha, color);
	}

	/**
	 * {@link tamaized.voidscape.coremod.transformers.entity.render.transparency.ModifyEntityRenderTypeTransformer}<p>
	 *
	 * Injection Point:<br>
	 * {@link LivingEntityRenderer#getRenderType(LivingEntityRenderState, boolean, boolean, boolean)}<br>
	 */
	public static boolean modifyEntityRenderType(boolean o, LivingEntityRenderState state) {
		Float value = state.getRenderData(entityLayerRendererRegistration.CONTEXT_KEY_INFUSION);
		return (value != null && value > 0F) || o;
	}

}
