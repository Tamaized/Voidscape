package tamaized.voidscape.client.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ParticleAxiomSpark extends Particle {

	private final ParticleRenderType group;
	private final Vector3fc offset;
	private final long seed;

	ParticleAxiomSpark(ClientLevel level, double x, double y, double z, Vector3fc offset, ParticleRenderType group) {
		super(level, x, y, z);
		this.offset = offset;
		this.group = group;
		seed = random.nextLong();
		lifetime = 3;
		hasPhysics = false;
	}

	@Override
	public ParticleRenderType getGroup() {
		return group;
	}

	boolean isVisible(Frustum frustum) {
		return frustum.pointInFrustum(x, y, z);
	}

	State extract(Camera camera) {
		Vec3 cameraPosition = camera.position();
		Vector3f position = new Vector3f((float) (x - cameraPosition.x()), (float) (y - cameraPosition.y()), (float) (z - cameraPosition.z()));
		return new State(position, offset, seed + age);
	}

	record State(Vector3fc position, Vector3fc offset, long seed) {

	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {

		private final ParticleRenderType group;

		public Provider(ParticleRenderType group) {
			this.group = group;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new ParticleAxiomSpark(level, x, y, z, new Vector3f((float) xAux, (float) yAux, (float) zAux), group);
		}

	}

}
