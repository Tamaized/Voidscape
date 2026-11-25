package tamaized.voidscape.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.LightningBoltRenderer;

@Component
@RequiresDist(Dist.CLIENT)
public class AxiomSparkRenderer {

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private LightningBoltRenderer lightningBoltRenderer;

	private final RenderType renderType = RenderType.create(
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "axiom_spark").toString(),
		RenderSetup.builder(RenderPipelines.LIGHTNING).sortOnUpload().createRenderSetup()
	);
	private final LightningBoltRenderer.Style style = new LightningBoltRenderer.Style.Builder(
		ARGB.color(0.3F, 0x65B3C0)
	)
		.renderType(renderType)
		.segmentLength(0.06F)
		.jitter(0.025F)
		.branchSegmentLength(0.04F)
		.branchJitter(0.015F)
		.endBranches(2)
		.coreThickness(0.003F)
		.layerThickness(0.005F)
		.build();

	@EventBusPostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerRenderBuffers);
	}

	private void registerRenderBuffers(RegisterRenderBuffersEvent event) {
		event.registerRenderBuffer(renderType);
	}

	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vector3fc position, Vector3fc offset, long seed) {
		float scale = offset.length() / 0.225F;
		poseStack.pushPose();
		poseStack.translate(position.x(), position.y(), position.z());
		poseStack.scale(scale, scale, scale);
		lightningBoltRenderer.submit(poseStack, submitNodeCollector, new Vector3f(offset).div(scale), seed, style);
		poseStack.popPose();
	}

}
