package tamaized.voidscape.client.entity.render;

import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelNullServant;
import tamaized.voidscape.client.entity.render.state.NullServantRenderState;
import tamaized.voidscape.entity.NullServantEntity;

public class RenderNullServant<T extends NullServantEntity> extends MobRenderer<T, NullServantRenderState, ModelNullServant<NullServantRenderState>> {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	public RenderNullServant(EntityRendererProvider.Context context) {
		super(context, new ModelNullServant<>(context.bakeLayer(modelLayerLocations.NULL_SERVANT)), 0F);
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new EyeLayer(this));
	}

	@Override
	public NullServantRenderState createRenderState() {
		return new NullServantRenderState();
	}

	@Override
	public void extractRenderState(T entity, NullServantRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
		state.augmentColor = switch (entity.getAugment()) {
			case NullServantEntity.AUGMENT_TITANITE -> 0x00FF00;
			case NullServantEntity.AUGMENT_ICHOR -> 0xFF7F00;
			case NullServantEntity.AUGMENT_ASTRAL -> 0xFFB2CC;
			default -> 0xFFFFFF;
		};
	}

	@Override
	protected int getModelTint(NullServantRenderState state) {
		return ARGB.opaque(state.augmentColor);
	}

	@Override
	public Identifier getTextureLocation(NullServantRenderState state) {
		return AbstractEndPortalRenderer.END_PORTAL_LOCATION;
	}

	private static class EyeLayer extends EyesLayer<NullServantRenderState, ModelNullServant<NullServantRenderState>> {

		private static final RenderType EYES = RenderTypes.eyes(Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/nullservant.png"));

		public EyeLayer(RenderLayerParent<NullServantRenderState, ModelNullServant<NullServantRenderState>> renderer) {
			super(renderer);
		}

		@Override
		public RenderType renderType() {
			return EYES;
		}

	}

}
