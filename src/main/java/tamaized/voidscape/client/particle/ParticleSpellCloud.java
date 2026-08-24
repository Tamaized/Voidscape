package tamaized.voidscape.client.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;

public class ParticleSpellCloud extends SingleQuadParticle {

	private final Vec3 target;
	private float rot;

	ParticleSpellCloud(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, TextureAtlasSprite sprite) {
		super(world, x, y, z, vx, vy, vz, sprite);
		target = new Vec3(x, y, z);
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.alpha = 0F;
		this.quadSize *= 1.5F * (random.nextBoolean() ? -1F : 1F);
		this.lifetime = 30 + ((int) (random.nextFloat() * 30F));
		this.hasPhysics = true;
		this.oRoll = this.roll = random.nextFloat() * 2F - 1F;
	}

	@Override
	protected SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.move(this.xd, this.yd, this.zd);

		this.yd *= 0.699999988079071D;
		this.yd -= 0.009999999552965164D;

		if (this.onGround) {
			this.xd *= 0.699999988079071D;
			this.zd *= 0.699999988079071D;
		} else {
			rot += 5F;
			if (xd == 0)
				xd += (random.nextBoolean() ? 1 : -1) * 0.001F;
			if (zd == 0)
				zd += (random.nextBoolean() ? 1 : -1) * 0.001F;
			if (random.nextInt(5) == 0)
				xd += Math.signum(target.x - x) * random.nextFloat() * 0.005F;
			if (random.nextInt(5) == 0)
				zd += Math.signum(target.z - z) * random.nextFloat() * 0.005F;
		}
	}

	@Override
	public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
		alpha = Math.min(Mth.clamp(age, 0, 20) / 20F, Mth.clamp(lifetime - age, 0, 20) / 20F);
		Quaternionf rotation = new Quaternionf(camera.rotation());
		if (this.roll != 0.0F) {
			rotation.rotateZ(Mth.lerp(partialTickTime, this.oRoll, this.roll));
		}
		rotation.rotateY(Mth.cos((float) Math.toRadians(rot % 360F)));
		extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
		extractRotatedQuad(particleTypeRenderState, camera, rotation.rotateY((float) Math.PI), partialTickTime);
	}

	@Override
	protected int getLightCoords(float partialTicks) {
		return 0xF000F0;
	}

	public static class Factory implements ParticleProvider<ParticleTypeSpellCloud.Options> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Nullable
		@Override
		public Particle createParticle(ParticleTypeSpellCloud.Options data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			ParticleSpellCloud particle = new ParticleSpellCloud(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet.get(random));
			particle.setColor(((data.color() >> 16) & 0xFF) / 255F, ((data.color() >> 8) & 0xFF) / 255F, (data.color() & 0xFF) / 255F);
			return particle;
		}
	}
}
