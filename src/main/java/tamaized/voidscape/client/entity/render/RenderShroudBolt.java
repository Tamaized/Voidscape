package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.client.LightningBoltRenderer;
import tamaized.voidscape.client.entity.render.state.ShroudBoltRenderState;
import tamaized.voidscape.entity.ShroudBoltEntity;

@Configurable
public class RenderShroudBolt extends EntityRenderer<ShroudBoltEntity, ShroudBoltRenderState> {

	private static final LightningBoltRenderer.Style STYLE = new LightningBoltRenderer.Style.Builder(
		ARGB.colorFromFloat(0.3F, 1.0F, 0.6F, 0.8F)
	).build();

	@Autowired(dist = Dist.CLIENT)
	private LightningBoltRenderer lightningBoltRenderer;

	public RenderShroudBolt(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ShroudBoltRenderState createRenderState() {
		return new ShroudBoltRenderState();
	}

	@Override
	public void extractRenderState(ShroudBoltEntity entity, ShroudBoltRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.offset.set(entity.getOffset());
		state.seed = entity.getId() * 31L + entity.tickCount;
	}

	@Override
	protected boolean affectedByCulling(ShroudBoltEntity entity) {
		return false;
	}

	@Override
	public void submit(ShroudBoltRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		lightningBoltRenderer.submit(poseStack, submitNodeCollector, state.offset, state.seed, STYLE);
	}

}
