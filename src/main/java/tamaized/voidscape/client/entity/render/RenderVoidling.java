package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.neoforged.api.distmarker.Dist;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelVoidling;
import tamaized.voidscape.entity.VoidlingEntity;

public class RenderVoidling<T extends VoidlingEntity> extends MobRenderer<T, LivingEntityRenderState, ModelVoidling> {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/voidling.png");

	public RenderVoidling(EntityRendererProvider.Context context) {
		super(context, new ModelVoidling(context.bakeLayer(modelLayerLocations.VOIDLING)), 0F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(T entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}

	@Nullable
	@Override
	protected RenderType getRenderType(LivingEntityRenderState state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
		return RenderTypes.entityTranslucent(getTextureLocation(state));
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		float scale = 0.7F;
		poseStack.scale(scale, scale, scale);
	}

}
