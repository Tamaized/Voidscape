package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelVoidsWrath;
import tamaized.voidscape.client.entity.render.state.VoidsWrathRenderState;
import tamaized.voidscape.entity.VoidsWrathEntity;

public class RenderVoidsWrath<T extends VoidsWrathEntity> extends MobRenderer<T, VoidsWrathRenderState, ModelVoidsWrath<VoidsWrathRenderState>> {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/voidswrath.png");

	public RenderVoidsWrath(EntityRendererProvider.Context context) {
		super(context, new ModelVoidsWrath<>(context.bakeLayer(modelLayerLocations.VOIDS_WRATH)), 0F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new OverlayLayer(this));
		this.addLayer(new PowerLayer(this, context.getModelSet()));
	}

	@Override
	public VoidsWrathRenderState createRenderState() {
		return new VoidsWrathRenderState();
	}

	@Override
	public void extractRenderState(T entity, VoidsWrathRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
		state.isPowered = entity.isPowered();
	}

	@Override
	public Identifier getTextureLocation(VoidsWrathRenderState state) {
		return TEXTURE;
	}

	private static class OverlayLayer extends EyesLayer<VoidsWrathRenderState, ModelVoidsWrath<VoidsWrathRenderState>> {

		private static final RenderType OVERLAY = RenderTypes.eyes(Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/voidswrath_overlay.png"));

		public OverlayLayer(RenderLayerParent<VoidsWrathRenderState, ModelVoidsWrath<VoidsWrathRenderState>> renderer) {
			super(renderer);
		}

		@Override
		public RenderType renderType() {
			return OVERLAY;
		}

	}

	private static class PowerLayer extends EnergySwirlLayer<VoidsWrathRenderState, ModelVoidsWrath<VoidsWrathRenderState>> {

		private static final Identifier POWER_LOCATION = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/voidswrath_armor.png");

		private final ModelVoidsWrath<VoidsWrathRenderState> model;

		public PowerLayer(RenderLayerParent<VoidsWrathRenderState, ModelVoidsWrath<VoidsWrathRenderState>> renderer, EntityModelSet modelSet) {
			super(renderer);
			this.model = new ModelVoidsWrath<>(modelSet.bakeLayer(modelLayerLocations.VOIDS_WRATH_CHARGED));
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, VoidsWrathRenderState state, float yRot, float xRot) {
			super.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, state, yRot, xRot);
		}

		@Override
		protected boolean isPowered(VoidsWrathRenderState state) {
			return state.isPowered;
		}

		@Override
		protected float xOffset(float t) {
			return t * 0.01F;
		}

		@Override
		protected Identifier getTextureLocation() {
			return POWER_LOCATION;
		}

		@Override
		protected ModelVoidsWrath<VoidsWrathRenderState> model() {
			return this.model;
		}

	}

}
