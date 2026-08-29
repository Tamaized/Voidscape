package tamaized.voidscape.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.function.Function;

public class ModelVoidsWrath<S extends HumanoidRenderState> extends EntityModel<S> implements ArmedModel<S> {

	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart leftArm;
	public final ModelPart rightArm;

	public ModelVoidsWrath(ModelPart root) {
		this(root, RenderTypes::entityCutout);
	}

	public ModelVoidsWrath(ModelPart root, Function<Identifier, RenderType> renderType) {
		super(root, renderType);
		head = root.getChild("head");
		body = root.getChild("body");
		leftArm = root.getChild("leftArm");
		rightArm = root.getChild("rightArm");
	}

	public static LayerDefinition createMesh(CubeDeformation deformation) {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild(
			"head",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8, deformation),
			PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
		);

		definition.addOrReplaceChild(
			"body",
			CubeListBuilder.create().texOffs(16, 16).addBox(-4F, 0F, -2F, 8, 12, 4, deformation),
			PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
		);


		definition.addOrReplaceChild(
			"leftArm",
			CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1F, -2F, -2F, 4, 12, 4, deformation),
			PartPose.offsetAndRotation(5F, 2F, 0F, 0F, 0F, 0F)
		);

		definition.addOrReplaceChild(
			"rightArm",
			CubeListBuilder.create().texOffs(40, 16).addBox(-3F, -2F, -2F, 4, 12, 4, deformation),
			PartPose.offsetAndRotation(-5F, 2F, 0F, 0F, 0F, 0F)
		);

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);

		this.head.yRot = state.yRot * ((float) Math.PI / 180F);
		this.head.xRot = state.xRot * ((float) Math.PI / 180F);

		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;

		this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
		this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * state.walkAnimationSpeed * 0.5F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean rightHanded = state.mainArm == HumanoidArm.RIGHT;
		if (state.isUsingItem) {
			boolean mainHandUsed = state.useItemHand == InteractionHand.MAIN_HAND;
			if (mainHandUsed == rightHanded) {
				this.poseRightArm(state);
			} else {
				this.poseLeftArm(state);
			}
		} else {
			boolean twoHandedOffhand = rightHanded ? state.leftArmPose.isTwoHanded() : state.rightArmPose.isTwoHanded();
			if (rightHanded != twoHandedOffhand) {
				this.poseLeftArm(state);
				this.poseRightArm(state);
			} else {
				this.poseRightArm(state);
				this.poseLeftArm(state);
			}
		}

		this.setupAttackAnimation(state);

		this.body.xRot = 0.0F;
		this.head.y = 0.0F;
		this.body.y = 0.0F;
		this.leftArm.y = 2.0F;
		this.rightArm.y = 2.0F;

		if (state.rightArmPose != HumanoidModel.ArmPose.SPYGLASS)
			AnimationUtils.bobModelPart(this.rightArm, state.ageInTicks, 1.0F);
		if (state.leftArmPose != HumanoidModel.ArmPose.SPYGLASS)
			AnimationUtils.bobModelPart(this.leftArm, state.ageInTicks, -1.0F);
	}

	private void poseRightArm(S state) {
		switch (state.rightArmPose) {
			case EMPTY -> this.rightArm.yRot = 0.0F;
			case BLOCK -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
				this.rightArm.yRot = (-(float) Math.PI / 6F);
			}
			case ITEM -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.rightArm.yRot = 0.0F;
			}
			case THROW_TRIDENT -> {
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
				this.rightArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, state.maxCrossbowChargeDuration, state.ticksUsingItem, true);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
			case SPYGLASS -> {
				this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (state.isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.rightArm.yRot = this.head.yRot - 0.2617994F;
			}
			default -> {
			}
		}
	}

	private void poseLeftArm(S state) {
		switch (state.leftArmPose) {
			case EMPTY -> this.leftArm.yRot = 0.0F;
			case BLOCK -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
				this.leftArm.yRot = ((float) Math.PI / 6F);
			}
			case ITEM -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
				this.leftArm.yRot = 0.0F;
			}
			case THROW_TRIDENT -> {
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
				this.leftArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.leftArm.yRot = 0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			}
			case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, state.maxCrossbowChargeDuration, state.ticksUsingItem, false);
			case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
			case SPYGLASS -> {
				this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (state.isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.leftArm.yRot = this.head.yRot + 0.2617994F;
			}
			default -> {
			}
		}
	}

	private void setupAttackAnimation(S state) {
		if (!(state.attackTime <= 0.0F)) {
			ModelPart attackArm = this.getArm(state.attackArm);
			this.body.yRot = Mth.sin(Mth.sqrt(state.attackTime) * ((float) Math.PI * 2F)) * 0.2F;
			if (state.attackArm == HumanoidArm.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
			this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			float swing = 1.0F - state.attackTime;
			swing = swing * swing;
			swing = swing * swing;
			swing = 1.0F - swing;
			float f1 = Mth.sin(swing * (float) Math.PI);
			float f2 = Mth.sin(state.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			attackArm.xRot = (float) ((double) attackArm.xRot - ((double) f1 * 1.2D + (double) f2));
			attackArm.yRot += this.body.yRot * 2.0F;
			attackArm.zRot += Mth.sin(state.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	@Override
	public void translateToHand(S state, HumanoidArm arm, PoseStack poseStack) {
		this.root.translateAndRotate(poseStack);
		this.getArm(arm).translateAndRotate(poseStack);
	}

	public ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}

}
