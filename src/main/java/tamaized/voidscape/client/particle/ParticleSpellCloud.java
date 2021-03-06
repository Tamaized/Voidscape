package tamaized.voidscape.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tamaized.voidscape.registry.ModParticles;

import javax.annotation.Nonnull;

public class ParticleSpellCloud extends SpriteTexturedParticle {

	private final Vector3d target;
	private float rot;

	ParticleSpellCloud(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		this(world, x, y, z, vx, vy, vz, 1.0F);
	}

	ParticleSpellCloud(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		target = new Vector3d(x, y, z);
		this.xd *= 0.10000000149011612D;
		this.yd *= 0.10000000149011612D;
		this.zd *= 0.10000000149011612D;
		this.xd += vx * 0.4D;
		this.yd += vy * 0.4D;
		this.xd += vz * 0.4D;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.alpha = 0F;
		this.quadSize *= 1.5F * (random.nextBoolean() ? -1F : 1F);
		this.quadSize *= scale;
		this.lifetime = 30 + ((int) (random.nextFloat() * 30F));
		this.lifetime = (int) (this.lifetime * scale);
		this.hasPhysics = true;
		this.oRoll = this.roll = random.nextFloat() * 2F - 1F;
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
	public void render(IVertexBuilder buffer, ActiveRenderInfo entity, float partialTicks) {
		alpha = Math.min(MathHelper.clamp(age, 0, 20) / 20F, MathHelper.clamp(lifetime - age, 0, 20) / 20F);
		Vector3d lvt_4_1_ = entity.getPosition();
		float lvt_5_1_ = (float) (MathHelper.lerp((double) partialTicks, this.xo, this.x) - lvt_4_1_.x());
		float lvt_6_1_ = (float) (MathHelper.lerp((double) partialTicks, this.yo, this.y) - lvt_4_1_.y());
		float lvt_7_1_ = (float) (MathHelper.lerp((double) partialTicks, this.zo, this.z) - lvt_4_1_.z());
		Quaternion lvt_8_2_ = new Quaternion(entity.rotation());
		if (this.roll != 0.0F) {
			float lvt_9_1_ = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
			lvt_8_2_.mul(Vector3f.ZP.rotation(lvt_9_1_));
		}
		lvt_8_2_.mul(Vector3f.YP.rotation(MathHelper.cos((float) Math.toRadians(rot % 360F))));
		Vector3f lvt_9_2_ = new Vector3f(-1.0F, -1.0F, 0.0F);
		lvt_9_2_.transform(lvt_8_2_);
		Vector3f[] lvt_10_1_ = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float lvt_11_1_ = this.getQuadSize(partialTicks);

		for (int lvt_12_1_ = 0; lvt_12_1_ < 4; ++lvt_12_1_) {
			Vector3f lvt_13_1_ = lvt_10_1_[lvt_12_1_];
			lvt_13_1_.transform(lvt_8_2_);
			lvt_13_1_.mul(lvt_11_1_);
			lvt_13_1_.add(lvt_5_1_, lvt_6_1_, lvt_7_1_);
		}
		float lvt_12_2_ = this.getU0();
		float lvt_13_2_ = this.getU1();
		float lvt_14_1_ = this.getV0();
		float lvt_15_1_ = this.getV1();
		int lvt_16_1_ = this.getLightColor(partialTicks);
		buffer.vertex((double) lvt_10_1_[0].x(), (double) lvt_10_1_[0].y(), (double) lvt_10_1_[0].z()).uv(lvt_13_2_, lvt_15_1_).color(rCol, gCol, bCol, alpha).uv2(lvt_16_1_).endVertex();
		buffer.vertex((double) lvt_10_1_[1].x(), (double) lvt_10_1_[1].y(), (double) lvt_10_1_[1].z()).uv(lvt_13_2_, lvt_14_1_).color(rCol, gCol, bCol, alpha).uv2(lvt_16_1_).endVertex();
		buffer.vertex((double) lvt_10_1_[2].x(), (double) lvt_10_1_[2].y(), (double) lvt_10_1_[2].z()).uv(lvt_12_2_, lvt_14_1_).color(rCol, gCol, bCol, alpha).uv2(lvt_16_1_).endVertex();
		buffer.vertex((double) lvt_10_1_[3].x(), (double) lvt_10_1_[3].y(), (double) lvt_10_1_[3].z()).uv(lvt_12_2_, lvt_15_1_).color(rCol, gCol, bCol, alpha).uv2(lvt_16_1_).endVertex();
	}


	@Override
	public int getLightColor(float partialTicks) {
		return 240 | 240 << 16;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<ModParticles.ParticleSpellCloudData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(ModParticles.ParticleSpellCloudData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleSpellCloud particle = new ParticleSpellCloud(world, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setColor(data.r / 255F, data.g / 255F, data.b / 255F);
			particle.pickSprite(this.spriteSet);
			return particle;
		}
	}
}
