package tamaized.voidscape.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class ModelCorruptedPawn<S extends LivingEntityRenderState> extends EntityModel<S> {

	private final ModelPart head;
	private final ModelPart topTentacle;
	private final ModelPart topRightTentacle;
	private final ModelPart rightTentacle;
	private final ModelPart bottomRightTentacle;
	private final ModelPart bottomTentacle;
	private final ModelPart bottomLeftTentacle;
	private final ModelPart leftTentacle;
	private final ModelPart topLeftTentacle;

	public ModelCorruptedPawn(ModelPart root) {
		this(root, RenderTypes::entityTranslucent);
	}

	public ModelCorruptedPawn(ModelPart root, Function<Identifier, RenderType> renderType) {
		super(root, renderType);
		head = root.getChild("head");
		topTentacle = root.getChild("topTentacle");
		topRightTentacle = root.getChild("topRightTentacle");
		rightTentacle = root.getChild("rightTentacle");
		bottomRightTentacle = root.getChild("bottomRightTentacle");
		bottomTentacle = root.getChild("bottomTentacle");
		bottomLeftTentacle = root.getChild("bottomLeftTentacle");
		leftTentacle = root.getChild("leftTentacle");
		topLeftTentacle = root.getChild("topLeftTentacle");
	}

	public static LayerDefinition createMesh() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild(
			"head",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4F, -8F, -4F, 16, 16, 16).mirror(),
			PartPose.offsetAndRotation(-4F, 0F, -2F, 0F, 0F, 0F)
		);

		definition.addOrReplaceChild(
			"topTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(0F, -9F, 2F, 0F, 0F, 3.141593F)
		);

		definition.addOrReplaceChild(
			"topRightTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(-9F, -9F, 2F, 0F, 0F, 2.324799F)
		);

		definition.addOrReplaceChild(
			"rightTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(-9F, 0F, 2F, 0F, 0F, 1.570796F)
		);

		definition.addOrReplaceChild(
			"bottomRightTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(-9F, 9F, 2F, 0F, 0F, 0.7435722F)
		);

		definition.addOrReplaceChild(
			"bottomTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(0F, 9F, 2F, 0F, 0F, 0F)
		);

		definition.addOrReplaceChild(
			"bottomLeftTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(9F, 9F, 2F, 0F, 0F, -0.7435801F)
		);

		definition.addOrReplaceChild(
			"leftTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(9F, 0F, 2F, 0F, 0F, -1.570796F)
		);

		definition.addOrReplaceChild(
			"topLeftTentacle",
			CubeListBuilder.create().texOffs(0, 32).addBox(-2F, 0F, -2F, 4, 12, 4).mirror(),
			PartPose.offsetAndRotation(9F, -9F, 2F, 0F, 0F, -2.324796F)
		);

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);

		this.head.xRot = state.xRot / (180F / (float) Math.PI);
		this.head.yRot = state.yRot / (180F / (float) Math.PI);

		float swing = state.walkAnimationSpeed * 4F;
		float swingCorner = swing * 0.45F + Mth.sin(state.walkAnimationPos) * 0.25F;
		float swingCardinal = swing * 0.95F + Mth.cos(state.walkAnimationPos) * 0.25F;

		this.topRightTentacle.xRot = swingCorner;
		this.topLeftTentacle.xRot = swingCorner;
		this.bottomRightTentacle.xRot = swingCorner;
		this.bottomLeftTentacle.xRot = swingCorner;
		this.topTentacle.xRot = swingCardinal;
		this.leftTentacle.xRot = swingCardinal;
		this.rightTentacle.xRot = swingCardinal;
		this.bottomTentacle.xRot = swingCardinal;
	}

}
