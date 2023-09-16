package tamaized.voidscape.client.entity.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import tamaized.voidscape.client.RenderStateAccessor;
import tamaized.voidscape.client.Shaders;
import tamaized.voidscape.entity.NullServantEntity;

import java.util.function.Function;

public class ModelNullServant<T extends NullServantEntity> extends EntityModel<T> implements ArmedModel {

	private static final Function<ResourceLocation, RenderType> RENDERTYPE = Util.memoize((p_173204_) -> {
		RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().
				setShaderState(new RenderStateShard.ShaderStateShard(() -> Shaders.VOIDSKY_ENTITY)).
				setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(p_173204_, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).
				setTransparencyState(RenderStateAccessor.NO_TRANSPARENCY()).
				setLightmapState(RenderStateAccessor.NO_LIGHTMAP()).
				setOverlayState(RenderStateAccessor.NO_OVERLAY()).
				createCompositeState(true);
		return RenderType.create("entity_solid_voidskyshader", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
	});

	public ModelPart head;
	public ModelPart body;
	public ModelPart leftArm;
	public ModelPart rightArm;

	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

	public ModelNullServant(ModelPart p_170677_) {
		this(p_170677_, RENDERTYPE);
	}

	public ModelNullServant(ModelPart parent, Function<ResourceLocation, RenderType> p_170680_) {
		super(p_170680_);
		head = parent.getChild("head");
		body = parent.getChild("body");
		leftArm = parent.getChild("leftArm");
		rightArm = parent.getChild("rightArm");
	}

	public static LayerDefinition createMesh() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).
						addBox(-4F, -8F, -4F, 8, 8, 8), PartPose.
						offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));

		definition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(32, 0).
						addBox(-4F, 0F, -2F, 8, 12, 4), PartPose.
						offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));


		definition.addOrReplaceChild("leftArm",
				CubeListBuilder.create().texOffs(16, 16).
						addBox(-1F, -2F, -2F, 4, 12, 4), PartPose.
						offsetAndRotation(5F, 2F, 0F, 0F, 0F, -0.10000736613927509F));

		definition.addOrReplaceChild("rightArm",
				CubeListBuilder.create().texOffs(0, 16).
						addBox(-3F, -2F, -2F, 4, 12, 4), PartPose.
						offsetAndRotation(-5F, 2F, 0F, 0F, 0F, 0.10000736613927509F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;

		float f = 1.0F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean flag2 = entity.getMainArm() == HumanoidArm.RIGHT;
		if (entity.isUsingItem()) {
			boolean flag3 = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
			if (flag3 == flag2) {
				this.poseRightArm(entity);
			} else {
				this.poseLeftArm(entity);
			}
		} else {
			boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
			if (flag2 != flag4) {
				this.poseLeftArm(entity);
				this.poseRightArm(entity);
			} else {
				this.poseRightArm(entity);
				this.poseLeftArm(entity);
			}
		}

		this.setupAttackAnimation(entity, ageInTicks);

		this.body.xRot = 0.0F;
		this.head.y = 0.0F;
		this.body.y = 0.0F;
		this.leftArm.y = 2.0F;
		this.rightArm.y = 2.0F;

		if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS)
			AnimationUtils.bobModelPart(this.rightArm, ageInTicks, 1.0F);
		if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS)
			AnimationUtils.bobModelPart(this.leftArm, ageInTicks, -1.0F);
	}

	private void poseRightArm(T p_102876_) {
		switch (this.rightArmPose) {
			case EMPTY -> this.rightArm.yRot = 0.0F;
			case BLOCK -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
				this.rightArm.yRot = (-(float) Math.PI / 6F);
			}
			case ITEM -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.rightArm.yRot = 0.0F;
			}
			case THROW_SPEAR -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
				this.rightArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102876_, true);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
			case SPYGLASS -> {
				this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102876_.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.rightArm.yRot = this.head.yRot - 0.2617994F;
			}
		}
	}

	private void poseLeftArm(T p_102879_) {
		switch (this.leftArmPose) {
			case EMPTY -> this.leftArm.yRot = 0.0F;
			case BLOCK -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
				this.leftArm.yRot = ((float) Math.PI / 6F);
			}
			case ITEM -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.leftArm.yRot = 0.0F;
			}
			case THROW_SPEAR -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
				this.leftArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.leftArm.yRot = 0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102879_, false);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
			case SPYGLASS -> {
				this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102879_.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.leftArm.yRot = this.head.yRot + 0.2617994F;
			}
		}
	}

	protected void setupAttackAnimation(T p_102858_, float p_102859_) {
		if (!(this.attackTime <= 0.0F)) {
			HumanoidArm humanoidarm = this.getAttackArm(p_102858_);
			ModelPart modelpart = this.getArm(humanoidarm);
			float f = this.attackTime;
			this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
			if (humanoidarm == HumanoidArm.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
			this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = Mth.sin(f * (float) Math.PI);
			float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			modelpart.xRot = (float) ((double) modelpart.xRot - ((double) f1 * 1.2D + (double) f2));
			modelpart.yRot += this.body.yRot * 2.0F;
			modelpart.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	private HumanoidArm getAttackArm(T p_102857_) {
		HumanoidArm humanoidarm = p_102857_.getMainArm();
		return p_102857_.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
	}

	public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
		this.getArm(p_102854_).translateAndRotate(p_102855_);
	}

	protected ModelPart getArm(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int p_103113_, int p_103114_, float r, float g, float b, float a) {
		head.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		body.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		leftArm.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		rightArm.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
	}
}
