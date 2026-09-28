package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import tamaized.voidscape.client.entity.render.state.ShroudBoltRenderState;
import tamaized.voidscape.entity.ShroudBoltEntity;

import java.util.ArrayList;
import java.util.List;

public class RenderShroudBolt extends EntityRenderer<ShroudBoltEntity, ShroudBoltRenderState> {

	private static final float RED = 1.0F;
	private static final float GREEN = 0.6F;
	private static final float BLUE = 0.8F;
	private static final float ALPHA = 0.3F;
	private static final float SEGMENT_LENGTH = 0.75F;
	private static final float JITTER = 0.2F;
	private static final float BRANCH_SEGMENT_LENGTH = 0.4F;
	private static final float BRANCH_JITTER = 0.12F;
	private static final float BRANCH_SPREAD = 0.9F;
	private static final float BRANCH_CHANCE = 0.5F;
	private static final float BRANCH_START = 0.05F;
	private static final float BRANCH_RADIUS_SCALE = 0.6F;
	private static final int END_BRANCHES = 3;
	private static final int LAYERS = 3;
	private static final float CORE_TICKNESS = 0.004F;
	private static final float LAYER_TICKNESS = 0.007F;

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
		float length = state.offset.length();
		if (length < 1.0E-4F)
			return;
		Vector3f direction = new Vector3f(state.offset).div(length);
		RandomSource random = RandomSource.createThreadLocalInstance(state.seed);

		Vector3f[] main = polyline(random, new Vector3f(), direction, length, SEGMENT_LENGTH, JITTER, true);
		List<Vector3f[]> branches = new ArrayList<>();
		for (int i = (int) (main.length * BRANCH_START); i < main.length - 1; i++) {
			float progress = (float) i / (main.length - 1);
			if (random.nextFloat() < (progress - BRANCH_START) / (1F - BRANCH_START) * BRANCH_CHANCE)
				branches.add(branch(random, main[i], direction, length));
		}
		for (int i = 0; i < END_BRANCHES; i++)
			branches.add(branch(random, main[main.length - 1], direction, length));

		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, buffer) -> {
			Matrix4fc poseMatrix = pose.pose();
			for (int layer = 0; layer < LAYERS; layer++) {
				float radius = CORE_TICKNESS + layer * LAYER_TICKNESS;
				bolt(poseMatrix, buffer, main, radius);
				for (Vector3f[] branch : branches)
					bolt(poseMatrix, buffer, branch, radius * BRANCH_RADIUS_SCALE);
			}
		});
	}

	private Vector3f[] branch(RandomSource random, Vector3fc origin, Vector3fc direction, float mainLength) {
		Vector3f side = perpendicular(direction);
		Vector3f normal = direction.cross(side, new Vector3f());
		Vector3f branchDirection = new Vector3f(direction)
			.fma((random.nextFloat() * 2F - 1F) * BRANCH_SPREAD, side)
			.fma((random.nextFloat() * 2F - 1F) * BRANCH_SPREAD, normal)
			.normalize();
		float branchLength = Math.min(mainLength * 0.3F, 0.75F + random.nextFloat() * 1.25F);
		return polyline(random, origin, branchDirection, branchLength, BRANCH_SEGMENT_LENGTH, BRANCH_JITTER, false);
	}

	private Vector3f[] polyline(RandomSource random, Vector3fc origin, Vector3fc direction, float length, float segmentLength, float jitter, boolean pinEnd) {
		Vector3f side = perpendicular(direction);
		Vector3f normal = direction.cross(side, new Vector3f());
		int segments = Math.max(2, Mth.ceil(length / segmentLength));
		Vector3f[] points = new Vector3f[segments + 1];
		for (int i = 0; i <= segments; i++) {
			Vector3f point = new Vector3f(direction).mul(length * i / segments).add(origin);
			if (i > 0 && (i < segments || !pinEnd)) {
				point.fma((random.nextFloat() * 2F - 1F) * jitter, side);
				point.fma((random.nextFloat() * 2F - 1F) * jitter, normal);
			}
			points[i] = point;
		}
		return points;
	}

	private Vector3f perpendicular(Vector3fc axis) {
		Vector3f up = Math.abs(axis.y()) > 0.99F ? new Vector3f(1F, 0F, 0F) : new Vector3f(0F, 1F, 0F);
		return axis.cross(up, new Vector3f()).normalize();
	}

	private void bolt(Matrix4fc pose, VertexConsumer buffer, Vector3f[] points, float radius) {
		for (int i = 0; i < points.length - 1; i++)
			prism(pose, buffer, points[i], points[i + 1], radius);
	}

	private void prism(Matrix4fc pose, VertexConsumer buffer, Vector3fc from, Vector3fc to, float radius) {
		Vector3f axis = to.sub(from, new Vector3f()).normalize();
		Vector3f side = perpendicular(axis);
		Vector3f normal = axis.cross(side, new Vector3f());
		Vector3f[] corners = {
			new Vector3f(side).add(normal).mul(radius),
			new Vector3f(side).negate().add(normal).mul(radius),
			new Vector3f(side).add(normal).negate().mul(radius),
			new Vector3f(side).sub(normal).mul(radius)
		};
		for (int k = 0; k < corners.length; k++) {
			Vector3fc current = corners[k];
			Vector3fc next = corners[(k + 1) % corners.length];
			vertex(pose, buffer, from, current);
			vertex(pose, buffer, from, next);
			vertex(pose, buffer, to, next);
			vertex(pose, buffer, to, current);
		}
	}

	private void vertex(Matrix4fc pose, VertexConsumer buffer, Vector3fc point, Vector3fc corner) {
		buffer.addVertex(pose, point.x() + corner.x(), point.y() + corner.y(), point.z() + corner.z()).setColor(RED, GREEN, BLUE, ALPHA);
	}

}
