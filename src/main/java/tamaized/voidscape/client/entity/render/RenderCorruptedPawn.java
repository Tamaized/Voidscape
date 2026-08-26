package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ModModelLayerLocations;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.client.entity.render.state.CorruptedPawnRenderState;
import tamaized.voidscape.entity.CorruptedPawnEntity;

public class RenderCorruptedPawn<T extends CorruptedPawnEntity> extends MobRenderer<T, CorruptedPawnRenderState, ModelCorruptedPawn<CorruptedPawnRenderState>> {

	@Autowired(dist = Dist.CLIENT)
	private static ModModelLayerLocations modelLayerLocations;

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private static final RenderType OVERLAY = RenderTypes.eyes(Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/corruptedpawn_overlay.png"));

	public RenderCorruptedPawn(EntityRendererProvider.Context context) {
		super(context, new ModelCorruptedPawn<>(context.bakeLayer(modelLayerLocations.CORRUPTED_PAWN)), 0F);
		addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return OVERLAY;
			}
		});
	}

	@Override
	public CorruptedPawnRenderState createRenderState() {
		return new CorruptedPawnRenderState();
	}

	@Override
	public void extractRenderState(T entity, CorruptedPawnRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.shouldRender = entity.shouldRender(Minecraft.getInstance().player);
	}

	@Override
	public void submit(CorruptedPawnRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.shouldRender) {
			super.submit(state, poseStack, submitNodeCollector, camera);
		}
	}

	@Override
	public Identifier getTextureLocation(CorruptedPawnRenderState state) {
		return TEXTURE;
	}

}
