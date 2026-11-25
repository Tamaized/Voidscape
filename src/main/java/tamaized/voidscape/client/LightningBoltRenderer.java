package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.RequiresDist;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiresDist(Dist.CLIENT)
public class LightningBoltRenderer {

	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vector3fc offset, long seed, Style style) {
		float length = offset.length();
		if (length < 1.0E-4F)
			return;
		Vector3f direction = new Vector3f(offset).div(length);
		RandomSource random = RandomSource.createThreadLocalInstance(seed);

		Vector3f[] main = polyline(random, new Vector3f(), direction, length, style.segmentLength(), style.jitter(), true);
		List<Vector3f[]> branches = new ArrayList<>();
		for (int i = (int) (main.length * style.branchStart()); i < main.length - 1; i++) {
			float progress = (float) i / (main.length - 1);
			if (random.nextFloat() < (progress - style.branchStart()) / (1F - style.branchStart()) * style.branchChance())
				branches.add(branch(random, main[i], direction, length, style));
		}
		for (int i = 0; i < style.endBranches(); i++)
			branches.add(branch(random, main[main.length - 1], direction, length, style));

		submitNodeCollector.submitCustomGeometry(poseStack, style.renderType(), (pose, buffer) -> {
			Matrix4fc poseMatrix = pose.pose();
			for (int layer = 0; layer < style.layers(); layer++) {
				float radius = style.coreThickness() + layer * style.layerThickness();
				bolt(poseMatrix, buffer, main, radius, style.color());
				for (Vector3f[] branch : branches)
					bolt(poseMatrix, buffer, branch, radius * style.branchRadiusScale(), style.color());
			}
		});
	}

	private Vector3f[] branch(RandomSource random, Vector3fc origin, Vector3fc direction, float mainLength, Style style) {
		Vector3f side = perpendicular(direction);
		Vector3f normal = direction.cross(side, new Vector3f());
		Vector3f branchDirection = new Vector3f(direction)
			.fma((random.nextFloat() * 2F - 1F) * style.branchSpread(), side)
			.fma((random.nextFloat() * 2F - 1F) * style.branchSpread(), normal)
			.normalize();
		float branchLength = Math.min(mainLength * 0.3F, 0.75F + random.nextFloat() * 1.25F);
		return polyline(random, origin, branchDirection, branchLength, style.branchSegmentLength(), style.branchJitter(), false);
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

	private void bolt(Matrix4fc pose, VertexConsumer buffer, Vector3f[] points, float radius, int color) {
		for (int i = 0; i < points.length - 1; i++)
			prism(pose, buffer, points[i], points[i + 1], radius, color);
	}

	private void prism(Matrix4fc pose, VertexConsumer buffer, Vector3fc from, Vector3fc to, float radius, int color) {
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
			vertex(pose, buffer, from, current, color);
			vertex(pose, buffer, from, next, color);
			vertex(pose, buffer, to, next, color);
			vertex(pose, buffer, to, current, color);
		}
	}

	private void vertex(Matrix4fc pose, VertexConsumer buffer, Vector3fc point, Vector3fc corner, int color) {
		buffer.addVertex(pose, point.x() + corner.x(), point.y() + corner.y(), point.z() + corner.z()).setColor(color);
	}

	public record Style(
		RenderType renderType,
		int color,
		float segmentLength,
		float jitter,
		float branchSegmentLength,
		float branchJitter,
		float branchSpread,
		float branchChance,
		float branchStart,
		float branchRadiusScale,
		int endBranches,
		int layers,
		float coreThickness,
		float layerThickness
	) {

		public static class Builder {

			private final int color;
			private RenderType renderType = RenderTypes.lightning();
			private float segmentLength = 0.75F;
			private float jitter = 0.2F;
			private float branchSegmentLength = 0.4F;
			private float branchJitter = 0.12F;
			private float branchSpread = 0.9F;
			private float branchChance = 0.5F;
			private float branchStart = 0.05F;
			private float branchRadiusScale = 0.6F;
			private int endBranches = 3;
			private int layers = 3;
			private float coreThickness = 0.004F;
			private float layerThickness = 0.007F;

			public Builder(int color) {
				this.color = color;
			}

			public Builder renderType(RenderType renderType) {
				this.renderType = renderType;
				return this;
			}

			public Builder segmentLength(float segmentLength) {
				this.segmentLength = segmentLength;
				return this;
			}

			public Builder jitter(float jitter) {
				this.jitter = jitter;
				return this;
			}

			public Builder branchSegmentLength(float branchSegmentLength) {
				this.branchSegmentLength = branchSegmentLength;
				return this;
			}

			public Builder branchJitter(float branchJitter) {
				this.branchJitter = branchJitter;
				return this;
			}

			public Builder branchSpread(float branchSpread) {
				this.branchSpread = branchSpread;
				return this;
			}

			public Builder branchChance(float branchChance) {
				this.branchChance = branchChance;
				return this;
			}

			public Builder branchStart(float branchStart) {
				this.branchStart = branchStart;
				return this;
			}

			public Builder branchRadiusScale(float branchRadiusScale) {
				this.branchRadiusScale = branchRadiusScale;
				return this;
			}

			public Builder endBranches(int endBranches) {
				this.endBranches = endBranches;
				return this;
			}

			public Builder layers(int layers) {
				this.layers = layers;
				return this;
			}

			public Builder coreThickness(float coreThickness) {
				this.coreThickness = coreThickness;
				return this;
			}

			public Builder layerThickness(float layerThickness) {
				this.layerThickness = layerThickness;
				return this;
			}

			public Style build() {
				return new Style(
					renderType,
					color,
					segmentLength,
					jitter,
					branchSegmentLength,
					branchJitter,
					branchSpread,
					branchChance,
					branchStart,
					branchRadiusScale,
					endBranches,
					layers,
					coreThickness,
					layerThickness
				);
			}

		}

	}

}
