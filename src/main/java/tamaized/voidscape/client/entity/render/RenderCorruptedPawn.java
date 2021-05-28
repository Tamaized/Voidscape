package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.RenderStateAccessor;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.entity.EntityCorruptedPawn;

public class RenderCorruptedPawn<T extends EntityCorruptedPawn, M extends ModelCorruptedPawn<T>> extends LivingRenderer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private static final RenderType OVERLAY = RenderType.eyes(new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn_overlay.png"));
	private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.
			create("entity_cutout_no_cull_fullbright", DefaultVertexFormats.NEW_ENTITY, 7, 256, true, false, RenderType.State.builder().
					setTextureState(new RenderState.TextureState(GUARDIAN_BEAM_LOCATION, false, false)).
					setTransparencyState(RenderStateAccessor.NO_TRANSPARENCY()).
					setAlphaState(RenderStateAccessor.DEFAULT_ALPHA()).
					setCullState(RenderStateAccessor.NO_CULL()).
					setOverlayState(RenderStateAccessor.OVERLAY()).
					createCompositeState(true));

	public RenderCorruptedPawn(EntityRendererManager rendererManager, M model) {
		super(rendererManager, model, 0F);
		addLayer(new AbstractEyesLayer<T, M>(this) {
			@Override
			public RenderType renderType() {
				return OVERLAY;
			}
		});
	}

	public static <T extends EntityCorruptedPawn> RenderCorruptedPawn<T, ModelCorruptedPawn<T>> factory(EntityRendererManager manager) {
		return new RenderCorruptedPawn<>(manager, new ModelCorruptedPawn<>());
	}

	public static void renderRay(MatrixStack stack, IRenderTypeBuffer buffer, World level, Vector3d offset, Vector3d start, Vector3d end, float lerp, int tickCount, float partialTicks) {
		float t = (float) level.getGameTime() + partialTicks;
		float lvt_10_1_ = t * 0.5F % 1.0F;
		stack.pushPose();
		stack.translate(offset.x(), offset.y(), offset.z());
		Vector3d dir = end.subtract(start.add(offset));
		float lvt_15_1_ = (float) (dir.length());
		dir = dir.normalize();
		float xRot = (float) Math.acos(dir.y);
		float yRot = (float) Math.atan2(dir.z, dir.x);
		stack.mulPose(Vector3f.YP.rotationDegrees((1.5707964F - yRot) * 57.295776F));
		stack.mulPose(Vector3f.XP.rotationDegrees(xRot * 57.295776F));
		float tOffset = t * 0.05F * -1.5F;
		float lvt_20_1_ = lerp * lerp;
		int lvt_21_1_ = 64 + (int) (lvt_20_1_ * 191.0F);
		int lvt_22_1_ = 32 + (int) (lvt_20_1_ * 191.0F);
		int lvt_23_1_ = 128 - (int) (lvt_20_1_ * 64.0F);
		float lvt_24_1_ = 0.2F;
		float lvt_25_1_ = 0.282F;
		float lvt_26_1_ = MathHelper.cos(tOffset + 2.3561945F) * lvt_25_1_;
		float lvt_27_1_ = MathHelper.sin(tOffset + 2.3561945F) * lvt_25_1_;
		float lvt_28_1_ = MathHelper.cos(tOffset + 0.7853982F) * lvt_25_1_;
		float lvt_29_1_ = MathHelper.sin(tOffset + 0.7853982F) * lvt_25_1_;
		float lvt_30_1_ = MathHelper.cos(tOffset + 3.926991F) * lvt_25_1_;
		float lvt_31_1_ = MathHelper.sin(tOffset + 3.926991F) * lvt_25_1_;
		float lvt_32_1_ = MathHelper.cos(tOffset + 5.4977875F) * lvt_25_1_;
		float lvt_33_1_ = MathHelper.sin(tOffset + 5.4977875F) * lvt_25_1_;
		float lvt_34_1_ = MathHelper.cos(tOffset + 3.1415927F) * lvt_24_1_;
		float lvt_35_1_ = MathHelper.sin(tOffset + 3.1415927F) * lvt_24_1_;
		float lvt_36_1_ = MathHelper.cos(tOffset + 0.0F) * lvt_24_1_;
		float lvt_37_1_ = MathHelper.sin(tOffset + 0.0F) * lvt_24_1_;
		float lvt_38_1_ = MathHelper.cos(tOffset + 1.5707964F) * lvt_24_1_;
		float lvt_39_1_ = MathHelper.sin(tOffset + 1.5707964F) * lvt_24_1_;
		float lvt_40_1_ = MathHelper.cos(tOffset + 4.712389F) * lvt_24_1_;
		float lvt_41_1_ = MathHelper.sin(tOffset + 4.712389F) * lvt_24_1_;
		float lvt_43_1_ = 0.0F;
		float lvt_44_1_ = 0.4999F;
		float lvt_45_1_ = -1.0F + lvt_10_1_;
		float lvt_46_1_ = lvt_15_1_ * 2.5F + lvt_45_1_;
		IVertexBuilder lvt_47_1_ = buffer.getBuffer(BEAM_RENDER_TYPE);
		MatrixStack.Entry lvt_48_1_ = stack.last();
		Matrix4f lvt_49_1_ = lvt_48_1_.pose();
		Matrix3f lvt_50_1_ = lvt_48_1_.normal();
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_34_1_, lvt_15_1_, lvt_35_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_44_1_, lvt_46_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_34_1_, lvt_43_1_, lvt_35_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_44_1_, lvt_45_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_36_1_, lvt_43_1_, lvt_37_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_43_1_, lvt_45_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_36_1_, lvt_15_1_, lvt_37_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_43_1_, lvt_46_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_38_1_, lvt_15_1_, lvt_39_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_44_1_, lvt_46_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_38_1_, lvt_43_1_, lvt_39_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_44_1_, lvt_45_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_40_1_, lvt_43_1_, lvt_41_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_43_1_, lvt_45_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_40_1_, lvt_15_1_, lvt_41_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, lvt_43_1_, lvt_46_1_);
		float lvt_51_1_ = 0.0F;
		if (tickCount % 2 == 0) {
			lvt_51_1_ = 0.5F;
		}

		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_26_1_, lvt_15_1_, lvt_27_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, 0.5F, lvt_51_1_ + 0.5F);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_28_1_, lvt_15_1_, lvt_29_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, 1.0F, lvt_51_1_ + 0.5F);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_32_1_, lvt_15_1_, lvt_33_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, 1.0F, lvt_51_1_);
		vertex(lvt_47_1_, lvt_49_1_, lvt_50_1_, lvt_30_1_, lvt_15_1_, lvt_31_1_, lvt_21_1_, lvt_22_1_, lvt_23_1_, 0.5F, lvt_51_1_);
		stack.popPose();
	}

	private static void vertex(IVertexBuilder p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_) {
		p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (entity.shouldRender(Minecraft.getInstance().player)) {
			super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			Entity target = entity.getRayTarget();
			if (target instanceof LivingEntity) {
				final int rayBits = entity.getRayBits();
				final float lerp = (float) (((double) entity.level.getGameTime() - (double) entity.getRayStart()) / (double) entity.getRayEnd());
				final Vector3d targetPos = getPosition((LivingEntity) target, target.getBbHeight() * 0.5F, partialTicks);
				for (int b = -1; b < 8; b++) {
					if (((rayBits >> (7 - b)) & 0b1) == 1) {
						if (b == -1)
							renderRay(matrixStackIn, bufferIn, entity.level, new Vector3d(0, entity.getBbHeight() / 2F, 0), getPosition(entity, 0, partialTicks), targetPos, lerp, entity.tickCount, partialTicks);
						else {
							Vector3d rot = new Vector3d(0, 1.25F, 0.01F);
							if (b > 0)
								rot = rot.zRot((float) Math.toRadians(45 * b)).yRot((float) Math.toRadians(180 - MathHelper.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)));
							renderRay(matrixStackIn, bufferIn, entity.level, rot.add(0, entity.getBbHeight() / 2F + 0.25F, 0), getPosition(entity, 0, partialTicks), targetPos, lerp, entity.tickCount, partialTicks);
						}
					}
				}
			}
		}
	}

	private Vector3d getPosition(LivingEntity entity, double yOffset, float partialTicks) {
		double x = MathHelper.lerp(partialTicks, entity.xOld, entity.getX());
		double y = MathHelper.lerp(partialTicks, entity.yOld, entity.getY()) + yOffset;
		double z = MathHelper.lerp(partialTicks, entity.zOld, entity.getZ());
		return new Vector3d(x, y, z);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
