package tamaized.voidscape.client.armor.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class ModelArmorCorrupt<T extends HumanoidRenderState> extends HumanoidModel<T> implements IOverlayArmorModel {

	private final ImmutableList<ModelPart> parts;

	@Nullable
	private final Identifier overlayTexture;

	public ModelPart head;
	public ModelPart headoverlay;
	public ModelPart body;
	public ModelPart rightarm;
	public ModelPart leftarm;
	public ModelPart bodyToLeg;
	public ModelPart rightleg;
	public ModelPart leftleg;
	public ModelPart rightfoot;
	public ModelPart leftfoot;
	public ModelPart topLeftTentacle;
	public ModelPart topRightTentacle;
	public ModelPart bottomLeftTentacle;
	public ModelPart bottomRightTentacle;

	public ModelArmorCorrupt(ModelPart parent, @Nullable Identifier overlayTexture) {
		this(parent, RenderTypes::entityCutout, overlayTexture);
	}

	public ModelArmorCorrupt(ModelPart parent, Function<Identifier, RenderType> p_170680_, @Nullable Identifier overlayTexture) {
		super(parent, p_170680_);
		this.overlayTexture = overlayTexture;

		ImmutableList.Builder<ModelPart> builder = ImmutableList.builder();

		builder.add(

			head = parent.getChild("realhead"),
			headoverlay = parent.getChild("headoverlay"),
			body = parent.getChild("realbody"),
			rightarm = parent.getChild("rightarm"),
			leftarm = parent.getChild("leftarm"),
			bodyToLeg = parent.getChild("bodyToLeg"),
			rightleg = parent.getChild("rightleg"),
			leftleg = parent.getChild("leftleg"),
			rightfoot = parent.getChild("rightfoot"),
			leftfoot = parent.getChild("leftfoot")

		);
		topLeftTentacle = body.getChild("topLeftTentacle");
		topRightTentacle = body.getChild("topRightTentacle");
		bottomLeftTentacle = body.getChild("bottomLeftTentacle");
		bottomRightTentacle = body.getChild("bottomRightTentacle");

		parts = builder.build();
	}

	public static LayerDefinition makeMesh(CubeDeformation deformation, float f) {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		// Copied from super
		PartDefinition headDefinition = definition.addOrReplaceChild(
			"head",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
			PartPose.offset(0.0F, 0.0F + f, 0.0F)
		);
		headDefinition.addOrReplaceChild(
			"hat",
			CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation.extend(0.5F)),
			PartPose.ZERO
		);
		definition.addOrReplaceChild(
			"body",
			CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(0.0F, 0.0F + f, 0.0F)
		);
		definition.addOrReplaceChild(
			"right_arm",
			CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(-5.0F, 2.0F + f, 0.0F)
		);
		definition.addOrReplaceChild(
			"left_arm",
			CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(5.0F, 2.0F + f, 0.0F)
		);
		definition.addOrReplaceChild(
			"right_leg",
			CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(-1.9F, 12.0F + f, 0.0F)
		);
		definition.addOrReplaceChild(
			"left_leg",
			CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(1.9F, 12.0F + f, 0.0F)
		);
		// End super

		PartDefinition bodyDefinition = definition.addOrReplaceChild(
			"realbody",
			CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);

		bodyDefinition.addOrReplaceChild(
			"bottomLeftTentacle",
			CubeListBuilder.create().texOffs(42, 53).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
			PartPose.offsetAndRotation(1.8F, 7.0F, 1.5F, -0.5970771211282936F, 0.5473352640780661F, 0.0F)
		);

		bodyDefinition.addOrReplaceChild(
			"bottomRightTentacle",
			CubeListBuilder.create().texOffs(42, 53).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
			PartPose.offsetAndRotation(-1.8F, 7.0F, 1.5F, -0.5970771211282936F, -0.5473352640780661F, 0.0F)
		);

		bodyDefinition.addOrReplaceChild(
			"topLeftTentacle",
			CubeListBuilder.create().texOffs(42, 53).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
			PartPose.offsetAndRotation(1.8F, 3.0F, 1.5F, 0.5970771211282936F, 0.5473352640780661F, 0.0F)
		);

		bodyDefinition.addOrReplaceChild(
			"topRightTentacle",
			CubeListBuilder.create().texOffs(42, 53).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F),
			PartPose.offsetAndRotation(-1.8F, 3.0F, 1.5F, 0.5970771211282936F, -0.5473352640780661F, 0.0F)
		);

		definition.addOrReplaceChild(
			"leftarm",
			CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation).mirror(),
			PartPose.offset(5.0F, 2.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"rightarm",
			CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(-5.0F, 2.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"headoverlay",
			CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation.extend(0.5F)),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"leftleg",
			CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation).mirror(),
			PartPose.offset(1.9F, 12.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"bodyToLeg",
			CubeListBuilder.create().texOffs(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"realhead",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"rightfoot",
			CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(-1.9F, 12.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"leftfoot",
			CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation).mirror(),
			PartPose.offset(1.9F, 12.0F, 0.0F)
		);

		definition.addOrReplaceChild(
			"rightleg",
			CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation),
			PartPose.offset(-1.9F, 12.0F, 0.0F)
		);

		return LayerDefinition.create(mesh, 64, 64);

	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
		float tick = state.ageInTicks;
		float scale = 0.05F;
		float amp = 0.15F;
		float offset = 0.25F;
		topLeftTentacle.xRot = Mth.cos(tick * scale) * amp + offset;
		topLeftTentacle.yRot = Mth.sin(tick * scale + 0.2F) * amp + offset;
		topRightTentacle.xRot = Mth.sin(tick * scale + 0.4F) * amp + offset;
		topRightTentacle.yRot = Mth.cos(tick * scale + 0.6F) * amp - offset;
		bottomLeftTentacle.xRot = Mth.sin(tick * scale + 0.7F) * amp - offset;
		bottomLeftTentacle.yRot = Mth.cos(tick * scale + 0.5F) * amp + offset;
		bottomRightTentacle.xRot = Mth.cos(tick * scale + 0.3F) * amp - offset;
		bottomRightTentacle.yRot = Mth.sin(tick * scale + 0.1F) * amp - offset;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		head.loadPose(super.head.storePose());
		headoverlay.loadPose(super.head.storePose());
		body.loadPose(super.body.storePose());
		rightarm.loadPose(super.rightArm.storePose());
		leftarm.loadPose(super.leftArm.storePose());
		bodyToLeg.loadPose(body.storePose());
		rightleg.loadPose(super.rightLeg.storePose());
		leftleg.loadPose(super.leftLeg.storePose());
		rightfoot.loadPose(super.rightLeg.storePose());
		leftfoot.loadPose(super.leftLeg.storePose());
		parts.forEach((modelRenderer_) -> modelRenderer_.render(poseStack, buffer, packedLight, packedOverlay, color));
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

	@Override
	public @Nullable Identifier overlayTexture() {
		return overlayTexture;
	}

	@Override
	public boolean overlayFullbright() {
		return true;
	}


	public void setVisibleFor(EquipmentSlot armorSlot) {
		head.visible = false;
		headoverlay.visible = false;
		body.visible = false;
		rightarm.visible = false;
		leftarm.visible = false;
		bodyToLeg.visible = false;
		rightleg.visible = false;
		leftleg.visible = false;
		rightfoot.visible = false;
		leftfoot.visible = false;
		switch (armorSlot) {
			case FEET -> {
				rightfoot.visible = true;
				leftfoot.visible = true;
			}
			case LEGS -> {
				rightleg.visible = true;
				leftleg.visible = true;
			}
			case CHEST -> {
				bodyToLeg.visible = true;
				body.visible = true;
				rightarm.visible = true;
				leftarm.visible = true;
			}
			case HEAD -> {
				head.visible = true;
				headoverlay.visible = true;
			}
			default -> {
			}
		}
	}

}
