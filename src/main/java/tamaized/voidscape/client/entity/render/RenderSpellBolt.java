package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.joml.Matrix4f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.SpellBoltEntity;

public class RenderSpellBolt<T extends SpellBoltEntity> extends EntityRenderer<T, EntityRenderState> {

	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/spells/mage/bolt.png");

	private final int color;

	public RenderSpellBolt(EntityRendererProvider.Context context, int color) {
		super(context);
		this.color = color;
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}

	private void vertex(VertexConsumer buffer, Matrix4f vertex, PoseStack.Pose normals, float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int lightmapUV, float normalX, float normalY, float normalZ) {
		buffer.addVertex(vertex, x, y, z)
			.setColor(red, green, blue, alpha)
			.setUv(texU, texV)
			.setLight(lightmapUV)
			.setNormal(normals, normalX, normalY, normalZ);
	}

	@Override
	public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		final float size = 0.5F;
		final float red = ((color >> 16) & 0xFF) / 255F;
		final float green = ((color >> 8) & 0xFF) / 255F;
		final float blue = (color & 0xFF) / 255F;
		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(TEXTURE, true), (pose, buffer) -> {
			Matrix4f v = new Matrix4f(pose.pose());
			for (int i = 0; i < 8; i++) {
				int deg = (int) (45 * i + state.ageInTicks * 2) % 360;
				v.rotate(Axis.XP.rotationDegrees(deg));
				v.rotate(Axis.YP.rotationDegrees(deg));
				vertex(buffer, v, pose, -size, -size, 0, red, green, blue, 0.75F, 0, 0, LightCoordsUtil.FULL_BRIGHT, 0F, 1F, 0F);
				vertex(buffer, v, pose, -size, size, 0, red, green, blue, 0.75F, 0, 1, LightCoordsUtil.FULL_BRIGHT, 0F, 1F, 0F);
				vertex(buffer, v, pose, size, size, 0, red, green, blue, 0.75F, 1, 1, LightCoordsUtil.FULL_BRIGHT, 0F, 1F, 0F);
				vertex(buffer, v, pose, size, -size, 0, red, green, blue, 0.75F, 1, 0, LightCoordsUtil.FULL_BRIGHT, 0F, 1F, 0F);
			}
		});
	}

}
