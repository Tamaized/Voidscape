package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LightningBoltRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4fc;
import tamaized.voidscape.entity.AntiBoltEntity;

public class RenderAntiBolt extends EntityRenderer<AntiBoltEntity, LightningBoltRenderState> {

	private static final float RED = 0.05F;
	private static final float GREEN = 0.02F;
	private static final float BLUE = 0.1F;

	public RenderAntiBolt(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public LightningBoltRenderState createRenderState() {
		return new LightningBoltRenderState();
	}

	@Override
	public void extractRenderState(AntiBoltEntity entity, LightningBoltRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.seed = entity.seed;
	}

	@Override
	protected boolean affectedByCulling(AntiBoltEntity entity) {
		return false;
	}

	@Override
	public void submit(LightningBoltRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		float[] xOffs = new float[8];
		float[] zOffs = new float[8];
		float xOff = 0.0F;
		float zOff = 0.0F;
		RandomSource random = RandomSource.createThreadLocalInstance(state.seed);

		for (int h = 7; h >= 0; h--) {
			xOffs[h] = xOff;
			zOffs[h] = zOff;
			xOff += random.nextInt(11) - 5;
			zOff += random.nextInt(11) - 5;
		}

		float finalXOff = xOff;
		float finalZOff = zOff;
		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, buffer) -> {
			Matrix4fc poseMatrix = pose.pose();

			for (int r = 0; r < 4; r++) {
				RandomSource boltRandom = RandomSource.createThreadLocalInstance(state.seed);

				for (int p = 0; p < 3; p++) {
					int hs = 7;
					int ht = 0;
					if (p > 0) {
						hs = 7 - p;
						ht = hs - 2;
					}

					float xo0 = xOffs[hs] - finalXOff;
					float zo0 = zOffs[hs] - finalZOff;

					for (int h = hs; h >= ht; h--) {
						float xo1 = xo0;
						float zo1 = zo0;
						if (p == 0) {
							xo0 += boltRandom.nextInt(11) - 5;
							zo0 += boltRandom.nextInt(11) - 5;
						} else {
							xo0 += boltRandom.nextInt(31) - 15;
							zo0 += boltRandom.nextInt(31) - 15;
						}

						float rr1 = 0.1F + r * 0.2F;
						if (p == 0) {
							rr1 *= h * 0.1F + 1.0F;
						}

						float rr2 = 0.1F + r * 0.2F;
						if (p == 0) {
							rr2 *= (h - 1.0F) * 0.1F + 1.0F;
						}

						quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, rr1, rr2, false, false, true, false);
						quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, rr1, rr2, true, false, true, true);
						quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, rr1, rr2, true, true, false, true);
						quad(poseMatrix, buffer, xo0, zo0, h, xo1, zo1, rr1, rr2, false, true, false, false);
					}
				}
			}
		});
	}

	private static void quad(Matrix4fc pose, VertexConsumer buffer, float xo0, float zo0, int h, float xo1, float zo1, float rr1, float rr2, boolean px1, boolean pz1, boolean px2, boolean pz2) {
		buffer.addVertex(pose, xo0 + (px1 ? rr2 : -rr2), (float) (h * -16), zo0 + (pz1 ? rr2 : -rr2)).setColor(RED, GREEN, BLUE, 0.3F);
		buffer.addVertex(pose, xo1 + (px1 ? rr1 : -rr1), (float) ((h + 1) * -16), zo1 + (pz1 ? rr1 : -rr1)).setColor(RED, GREEN, BLUE, 0.3F);
		buffer.addVertex(pose, xo1 + (px2 ? rr1 : -rr1), (float) ((h + 1) * -16), zo1 + (pz2 ? rr1 : -rr1)).setColor(RED, GREEN, BLUE, 0.3F);
		buffer.addVertex(pose, xo0 + (px2 ? rr2 : -rr2), (float) (h * -16), zo0 + (pz2 ? rr2 : -rr2)).setColor(RED, GREEN, BLUE, 0.3F);
	}

}
