package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.client.RenderStateAccessor;
import tamaized.voidscape.entity.EntityAntiBolt;

import java.util.Random;

public class RenderAntiBolt extends EntityRenderer<EntityAntiBolt> {

	protected static final RenderStateShard.TransparencyStateShard ANTI_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("antibolt_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	private static final RenderType ANTIBOLT = RenderType.create("antibolt", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().
			setShaderState(RenderStateAccessor.RENDERTYPE_LIGHTNING_SHADER()).
			setWriteMaskState(RenderStateAccessor.COLOR_DEPTH_WRITE()).
			setTransparencyState(ANTI_TRANSPARENCY).
			setOutputState(RenderStateAccessor.WEATHER_TARGET()).
			createCompositeState(false));


	public RenderAntiBolt(EntityRendererProvider.Context p_i46157_1_) {
		super(p_i46157_1_);
	}

	@Override
	public void render(EntityAntiBolt p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
		float[] lvt_7_1_ = new float[8];
		float[] lvt_8_1_ = new float[8];
		float lvt_9_1_ = 0.0F;
		float lvt_10_1_ = 0.0F;
		Random lvt_11_1_ = new Random(p_225623_1_.seed);

		for (int lvt_12_1_ = 7; lvt_12_1_ >= 0; --lvt_12_1_) {
			lvt_7_1_[lvt_12_1_] = lvt_9_1_;
			lvt_8_1_[lvt_12_1_] = lvt_10_1_;
			lvt_9_1_ += (float) (lvt_11_1_.nextInt(11) - 5);
			lvt_10_1_ += (float) (lvt_11_1_.nextInt(11) - 5);
		}

		VertexConsumer vertexconsumer = p_225623_5_.getBuffer(ANTIBOLT);
		Matrix4f lvt_12_2_ = p_225623_4_.last().pose();

		for (int lvt_13_1_ = 0; lvt_13_1_ < 4; ++lvt_13_1_) {
			Random lvt_14_1_ = new Random(p_225623_1_.seed);

			for (int lvt_15_1_ = 0; lvt_15_1_ < 3; ++lvt_15_1_) {
				int lvt_16_1_ = 7;
				int lvt_17_1_ = 0;
				if (lvt_15_1_ > 0) {
					lvt_16_1_ = 7 - lvt_15_1_;
				}

				if (lvt_15_1_ > 0) {
					lvt_17_1_ = lvt_16_1_ - 2;
				}

				float lvt_18_1_ = lvt_7_1_[lvt_16_1_] - lvt_9_1_;
				float lvt_19_1_ = lvt_8_1_[lvt_16_1_] - lvt_10_1_;

				for (int lvt_20_1_ = lvt_16_1_; lvt_20_1_ >= lvt_17_1_; --lvt_20_1_) {
					float lvt_21_1_ = lvt_18_1_;
					float lvt_22_1_ = lvt_19_1_;
					if (lvt_15_1_ == 0) {
						lvt_18_1_ += (float) (lvt_14_1_.nextInt(11) - 5);
						lvt_19_1_ += (float) (lvt_14_1_.nextInt(11) - 5);
					} else {
						lvt_18_1_ += (float) (lvt_14_1_.nextInt(31) - 15);
						lvt_19_1_ += (float) (lvt_14_1_.nextInt(31) - 15);
					}

					float lvt_23_1_ = 0.5F;
					float lvt_24_1_ = 0.45F;
					float lvt_25_1_ = 0.45F;
					float lvt_26_1_ = 0.5F;
					float lvt_27_1_ = 0.1F + (float) lvt_13_1_ * 0.2F;
					if (lvt_15_1_ == 0) {
						lvt_27_1_ = (float) ((double) lvt_27_1_ * ((double) lvt_20_1_ * 0.1D + 1.0D));
					}

					float lvt_28_1_ = 0.1F + (float) lvt_13_1_ * 0.2F;
					if (lvt_15_1_ == 0) {
						lvt_28_1_ *= (float) (lvt_20_1_ - 1) * 0.1F + 1.0F;
					}

					float red = 0.05F;
					float green = 0.02F;
					float blue = 0.1F;

					quad(lvt_12_2_, vertexconsumer, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, red, green, blue, lvt_27_1_, lvt_28_1_, false, false, true, false);
					quad(lvt_12_2_, vertexconsumer, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, red, green, blue, lvt_27_1_, lvt_28_1_, true, false, true, true);
					quad(lvt_12_2_, vertexconsumer, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, red, green, blue, lvt_27_1_, lvt_28_1_, true, true, false, true);
					quad(lvt_12_2_, vertexconsumer, lvt_18_1_, lvt_19_1_, lvt_20_1_, lvt_21_1_, lvt_22_1_, red, green, blue, lvt_27_1_, lvt_28_1_, false, true, false, false);
				}
			}
		}

	}

	private static void quad(Matrix4f p_229116_0_, VertexConsumer p_229116_1_, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, float p_229116_7_, float p_229116_8_, float p_229116_9_, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
		p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * -16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
		p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * -16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
		p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * -16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
		p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * -16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 0.3F).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityAntiBolt p_110775_1_) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}