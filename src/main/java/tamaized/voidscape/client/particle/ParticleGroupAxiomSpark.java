package tamaized.voidscape.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;

import java.util.List;

@Configurable
public class ParticleGroupAxiomSpark extends ParticleGroup<ParticleAxiomSpark> {

	@Autowired(dist = Dist.CLIENT)
	private AxiomSparkRenderer sparkRenderer;

	public ParticleGroupAxiomSpark(ParticleEngine engine) {
		super(engine);
	}

	@Override
	public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
		List<ParticleAxiomSpark.State> sparks = particles.stream()
			.filter(particle -> particle.isVisible(frustum))
			.map(particle -> particle.extract(camera))
			.toList();
		return (submitNodeCollector, _) -> {
			for (ParticleAxiomSpark.State spark : sparks)
				sparkRenderer.submit(new PoseStack(), submitNodeCollector, spark.position(), spark.offset(), spark.seed());
		};
	}

}
