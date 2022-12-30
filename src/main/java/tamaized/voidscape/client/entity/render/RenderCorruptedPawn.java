package tamaized.voidscape.client.entity.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.RenderStateAccessor;
import tamaized.voidscape.client.entity.model.ModelCorruptedPawn;
import tamaized.voidscape.entity.EntityCorruptedPawn;
import tamaized.voidscape.registry.ModEntities;

public class RenderCorruptedPawn<T extends EntityCorruptedPawn, M extends ModelCorruptedPawn<T>> extends LivingEntityRenderer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn.png");
	private static final RenderType OVERLAY = RenderType.eyes(new ResourceLocation(Voidscape.MODID, "textures/entity/corruptedpawn_overlay.png"));
	private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");
	public static final ResourceLocation LASER_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.
			create("entity_cutout_no_cull_fullbright", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().
					setShaderState(RenderStateAccessor.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER()).
					setTextureState(new RenderStateShard.TextureStateShard(GUARDIAN_BEAM_LOCATION, false, false)).
					setTransparencyState(RenderStateAccessor.NO_TRANSPARENCY()).
					setCullState(RenderStateAccessor.NO_CULL()).
					setLightmapState(RenderStateAccessor.LIGHTMAP()).
					setOverlayState(RenderStateAccessor.OVERLAY()).
					createCompositeState(true));

	public RenderCorruptedPawn(EntityRendererProvider.Context rendererManager, M model) {
		super(rendererManager, model, 0F);
		addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return OVERLAY;
			}
		});
	}

	public static <T extends EntityCorruptedPawn> RenderCorruptedPawn<T, ModelCorruptedPawn<T>> factory(EntityRendererProvider.Context manager) {
		return new RenderCorruptedPawn<>(manager, new ModelCorruptedPawn<>(manager.bakeLayer(ModEntities.ModelLayerLocations.CORRUPTED_PAWN)));
	}

	public static void renderRay(PoseStack stack, MultiBufferSource buffer, Level level, Vec3 offset, Vec3 start, Vec3 end, float lerp, int tickCount, float partialTicks) {
		float t = (float) tickCount + partialTicks;
		float lvt_10_1_ = t * 0.5F % 1.0F;
		stack.pushPose();
		stack.translate(offset.x(), offset.y(), offset.z());
		Vec3 dir = end.subtract(start.add(offset));
		float lvt_15_1_ = (float) (dir.length());
		dir = dir.normalize();
		float xRot = (float) Math.acos(dir.y);
		float yRot = (float) Math.atan2(dir.z, dir.x);
		stack.mulPose(Axis.YP.rotationDegrees((1.5707964F - yRot) * 57.295776F));
		stack.mulPose(Axis.XP.rotationDegrees(xRot * 57.295776F));
		float tOffset = t * 0.05F * -1.5F;
		float lvt_20_1_ = lerp * lerp;
		int lvt_21_1_ = 64 + (int) (lvt_20_1_ * 191.0F);
		int lvt_22_1_ = 32 + (int) (lvt_20_1_ * 191.0F);
		int lvt_23_1_ = 128 - (int) (lvt_20_1_ * 64.0F);
		float lvt_24_1_ = 0.2F;
		float lvt_25_1_ = 0.282F;
		float lvt_26_1_ = Mth.cos(tOffset + 2.3561945F) * lvt_25_1_;
		float lvt_27_1_ = Mth.sin(tOffset + 2.3561945F) * lvt_25_1_;
		float lvt_28_1_ = Mth.cos(tOffset + 0.7853982F) * lvt_25_1_;
		float lvt_29_1_ = Mth.sin(tOffset + 0.7853982F) * lvt_25_1_;
		float lvt_30_1_ = Mth.cos(tOffset + 3.926991F) * lvt_25_1_;
		float lvt_31_1_ = Mth.sin(tOffset + 3.926991F) * lvt_25_1_;
		float lvt_32_1_ = Mth.cos(tOffset + 5.4977875F) * lvt_25_1_;
		float lvt_33_1_ = Mth.sin(tOffset + 5.4977875F) * lvt_25_1_;
		float lvt_34_1_ = Mth.cos(tOffset + 3.1415927F) * lvt_24_1_;
		float lvt_35_1_ = Mth.sin(tOffset + 3.1415927F) * lvt_24_1_;
		float lvt_36_1_ = Mth.cos(tOffset + 0.0F) * lvt_24_1_;
		float lvt_37_1_ = Mth.sin(tOffset + 0.0F) * lvt_24_1_;
		float lvt_38_1_ = Mth.cos(tOffset + 1.5707964F) * lvt_24_1_;
		float lvt_39_1_ = Mth.sin(tOffset + 1.5707964F) * lvt_24_1_;
		float lvt_40_1_ = Mth.cos(tOffset + 4.712389F) * lvt_24_1_;
		float lvt_41_1_ = Mth.sin(tOffset + 4.712389F) * lvt_24_1_;
		float lvt_43_1_ = 0.0F;
		float lvt_44_1_ = 0.4999F;
		float lvt_45_1_ = -1.0F + lvt_10_1_;
		float lvt_46_1_ = lvt_15_1_ * 2.5F + lvt_45_1_;
		VertexConsumer lvt_47_1_ = buffer.getBuffer(BEAM_RENDER_TYPE);
		PoseStack.Pose lvt_48_1_ = stack.last();
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

	private static void vertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_) {
		p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
	}

	private static void renderBeaconBeam(PoseStack p_112177_, MultiBufferSource p_112178_, float partialTicks, long tick, int start, int length, float[] colors) {
		renderBeaconBeam(p_112177_, p_112178_, LASER_LOCATION, partialTicks, 1.0F, tick, start, length, colors, 0.2F, 0.25F);
	}

	private static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource multiBufferSource, ResourceLocation texture, float partialTicks, float scale, long tick, int start, int length, float[] colors, float p_112194_, float p_112195_) {
		int i = start + length;
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.0D, 0.5D);
		float f = (float)Math.floorMod(tick, 40) + partialTicks;
		float f1 = length < 0 ? f : -f;
		float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
		float f6 = 0.0F;
		float f8 = 0.0F;
		float f9 = -p_112194_;
		float f10 = 0.0F;
		float f11 = 0.0F;
		float f12 = -p_112194_;
		float f13 = 0.0F;
		float f14 = 1.0F;
		float f15 = -1.0F + f2;
		float f16 = (float)length * scale * (0.5F / p_112194_) + f15;
		renderPart(poseStack, multiBufferSource.getBuffer(RenderType.beaconBeam(texture, false)), red, green, blue, 1.0F, start, i, 0.0F, p_112194_, p_112194_, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
		poseStack.popPose();
		f6 = -p_112195_;
		float f7 = -p_112195_;
		f8 = -p_112195_;
		f9 = -p_112195_;
		f13 = 0.0F;
		f14 = 1.0F;
		f15 = -1.0F + f2;
		f16 = (float)length * scale + f15;
		renderPart(poseStack, multiBufferSource.getBuffer(RenderType.beaconBeam(texture, true)), red, green, blue, 0.125F, start, i, f6, f7, p_112195_, f8, f9, p_112195_, p_112195_, p_112195_, 0.0F, 1.0F, f16, f15);
		poseStack.popPose();
	}

	private static void renderPart(PoseStack poseStack, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int p_112162_, int p_112163_, float p_112164_, float p_112165_, float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_, float p_112172_, float p_112173_, float p_112174_, float p_112175_) {
		PoseStack.Pose posestack$pose = poseStack.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();
		renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112162_, p_112163_, p_112164_, p_112165_, p_112166_, p_112167_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112162_, p_112163_, p_112170_, p_112171_, p_112168_, p_112169_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112162_, p_112163_, p_112166_, p_112167_, p_112170_, p_112171_, p_112172_, p_112173_, p_112174_, p_112175_);
		renderQuad(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112162_, p_112163_, p_112168_, p_112169_, p_112164_, p_112165_, p_112172_, p_112173_, p_112174_, p_112175_);
	}

	private static void renderQuad(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int p_112127_, int p_112128_, float p_112129_, float p_112130_, float p_112131_, float p_112132_, float p_112133_, float p_112134_, float p_112135_, float p_112136_) {
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112128_, p_112129_, p_112130_, p_112134_, p_112135_);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112127_, p_112129_, p_112130_, p_112134_, p_112136_);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112127_, p_112131_, p_112132_, p_112133_, p_112136_);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, p_112128_, p_112131_, p_112132_, p_112133_, p_112135_);
	}

	private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, float y, float x, float z, float u, float v) {
		vertexConsumer.vertex(matrix4f, x, y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	protected boolean shouldShowName(T entityIn) {
		return super.shouldShowName(entityIn) && (entityIn.shouldShowName() || entityIn.hasCustomName() && entityIn == this.entityRenderDispatcher.crosshairPickEntity);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if (entity.shouldRender(Minecraft.getInstance().player)) {
			super.render(entity, yaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			Entity target = entity.getRayTarget();
			if (target instanceof LivingEntity) {
				final int rayBits = entity.getRayBits();
				final float lerp = (float) (((double) entity.level.getGameTime() - (double) entity.getRayStart()) / (double) entity.getRayEnd());
				final Vec3 targetPos = getPosition((LivingEntity) target, target.getBbHeight() * 0.5F, partialTicks);
				for (int b = -1; b < 8; b++) {
					if (((rayBits >> (7 - b)) & 0b1) == 1) {
						if (b == -1)
							renderRay(matrixStackIn, bufferIn, entity.level, new Vec3(0, entity.getBbHeight() / 2F, 0), getPosition(entity, 0, partialTicks), targetPos, lerp, entity.tickCount, partialTicks);
						else {
							Vec3 rot = new Vec3(0, 1.25F, 0.01F);
							if (b > 0)
								rot = rot.zRot((float) Math.toRadians(45 * b)).yRot((float) Math.toRadians(180 - Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)));
							renderRay(matrixStackIn, bufferIn, entity.level, rot.add(0, entity.getBbHeight() / 2F + 0.25F, 0), getPosition(entity, 0, partialTicks), targetPos, lerp, entity.tickCount, partialTicks);
						}
					}
				}
			}

			if (entity.isSpin() && (entity.tickCount - entity.getSpinStart()) > entity.getSpinEnd() - (20 * 3)) {
				matrixStackIn.pushPose();
				{
					matrixStackIn.mulPose(Axis.YN.rotationDegrees(entity.getViewYRot(partialTicks) + 180F));
					matrixStackIn.translate(-0.5F, 0F, -0.5F);
					matrixStackIn.mulPose(Axis.XN.rotationDegrees(90));
					matrixStackIn.scale(4F, 4F, 4F);
					matrixStackIn.translate(-0.3575F, 0F, -0.15F);
					renderBeaconBeam(matrixStackIn, bufferIn, partialTicks, entity.level.getGameTime(), 0, 10, new float[]{0.75F, 0F, 1F});
				}
				matrixStackIn.popPose();
			}
		}
	}

	private Vec3 getPosition(LivingEntity entity, double yOffset, float partialTicks) {
		double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
		double y = Mth.lerp(partialTicks, entity.yOld, entity.getY()) + yOffset;
		double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
		return new Vec3(x, y, z);
	}

	@Override
	public ResourceLocation getTextureLocation(T entityIn) {
		return TEXTURE;
	}
}
