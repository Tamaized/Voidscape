package tamaized.voidscape.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tamaized.voidscape.entity.EntityCorruptedPawn;

import java.util.function.Function;

public class ModelCorruptedPawn<T extends EntityCorruptedPawn> extends EntityModel<T> {

	private static final int[] CAST_MOVEMENT = {225, 315, 135, 270, 45, 180, 0, 90};

	private final ImmutableList<TransparentModelWrapper> parts;

	private final TransparentModelWrapper head;
	private final TransparentModelWrapper topTentacle;
	private final TransparentModelWrapper topRightTentacle;
	private final TransparentModelWrapper rightTentacle;
	private final TransparentModelWrapper bottomRightTentacle;
	private final TransparentModelWrapper bottomTentacle;
	private final TransparentModelWrapper bottomLeftTentacle;
	private final TransparentModelWrapper leftTentacle;
	private final TransparentModelWrapper topLeftTentacle;

	private static class TransparentModelWrapper {
		private float alpha = 1F;
		private final ModelPart part;

		private TransparentModelWrapper(ModelPart part) {
			this.part = part;
		}
	}

	public ModelCorruptedPawn(ModelPart p_170677_) {
		this(p_170677_, RenderType::entityTranslucent);
	}

	public ModelCorruptedPawn(ModelPart parent, Function<ResourceLocation, RenderType> p_170680_) {
		super(p_170680_);

		ImmutableList.Builder<TransparentModelWrapper> builder = ImmutableList.builder();

		builder.add(

				head = new TransparentModelWrapper(parent.getChild("head")),
				topTentacle = new TransparentModelWrapper(parent.getChild("topTentacle")),
				topRightTentacle = new TransparentModelWrapper(parent.getChild("topRightTentacle")),
				rightTentacle = new TransparentModelWrapper(parent.getChild("rightTentacle")),
				bottomRightTentacle = new TransparentModelWrapper(parent.getChild("bottomRightTentacle")),
				bottomTentacle = new TransparentModelWrapper(parent.getChild("bottomTentacle")),
				bottomLeftTentacle = new TransparentModelWrapper(parent.getChild("bottomLeftTentacle")),
				leftTentacle = new TransparentModelWrapper(parent.getChild("leftTentacle")),
				topLeftTentacle = new TransparentModelWrapper(parent.getChild("topLeftTentacle"))

		);

		parts = builder.build();
	}

	public static LayerDefinition createMesh() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).
						addBox(-4F, -8F, -4F, 16, 16, 16).mirror(), PartPose.
						offsetAndRotation(-4F, 0F, -2F, 0F, 0F, 0F));

		definition.addOrReplaceChild("topTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(0F, -9F, 2F, 0F, 0F, 3.141593F));

		definition.addOrReplaceChild("topRightTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(-9F, -9F, 2F, 0F, 0F, 2.324799F));

		definition.addOrReplaceChild("rightTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(-9F, 0F, 2F, 0F, 0F, 1.570796F));

		definition.addOrReplaceChild("bottomRightTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(-9F, 9F, 2F, 0F, 0F, 0.7435722F));

		definition.addOrReplaceChild("bottomTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(0F, 9F, 2F, 0F, 0F, 0F));

		definition.addOrReplaceChild("bottomLeftTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(9F, 9F, 2F, 0F, 0F, -0.7435801F));

		definition.addOrReplaceChild("leftTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(9F, 0F, 2F, 0F, 0F, -1.570796F));

		definition.addOrReplaceChild("topLeftTentacle",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.
						offsetAndRotation(9F, -9F, 2F, 0F, 0F, -2.324796F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.part.xRot = headPitch / (180F / (float) Math.PI);
		this.head.part.yRot = netHeadYaw / (180F / (float) Math.PI);

		float swing = limbSwingAmount * 4F;
		float swingCorner = swing * 0.45F + Mth.sin(limbSwing) * 0.25F;
		float swingCardinal = swing * 0.95F + Mth.cos(limbSwing) * 0.25F;

		this.topRightTentacle.part.xRot = swingCorner;
		this.topLeftTentacle.part.xRot = swingCorner;
		this.bottomRightTentacle.part.xRot = swingCorner;
		this.bottomLeftTentacle.part.xRot = swingCorner;
		this.topTentacle.part.xRot = swingCardinal;
		this.leftTentacle.part.xRot = swingCardinal;
		this.rightTentacle.part.xRot = swingCardinal;
		this.bottomTentacle.part.xRot = swingCardinal;

		int i = 0;
		for (TransparentModelWrapper part : parts) {
			if (part == head)
				continue;
			if (entity.isCasting()) {
				float rot = (float) Math.toRadians(CAST_MOVEMENT[i] + (entity.tickCount - entity.castTick) * 25L);
				float sine = Mth.sin(rot) * 0.5F;
				float cosine = Mth.cos(rot) * 0.5F;
				part.part.yRot = sine + cosine;
				part.part.xRot = cosine - sine;
			} else
				part.part.yRot = 0;
			boolean state = ((entity.getTentacleBits() >> (7 - i)) & 0b1) == 1;
			float perc = Mth.clamp(1F - (entity.tentacleTimes[i] - entity.tickCount + Minecraft.getInstance().getFrameTime()) / (20F * 5F), 0F, 1F);
			float alpha = Mth.clamp(entity.tickCount >= entity.tentacleTimes[i] ? state ? 0F : 1F : state ? 1F - perc : perc, 0F, 1F);
			part.part.visible = alpha > 0F;
			part.alpha = alpha;
			i++;
		}
	}

	@Override
	public void renderToBuffer(PoseStack p_103013_, VertexConsumer p_103014_, int p_103015_, int p_103016_, float p_103017_, float p_103018_, float p_103019_, float p_103020_) {
		this.parts.forEach((part) -> {
			part.part.render(p_103013_, p_103014_, p_103015_, p_103016_, p_103017_, p_103018_, p_103019_, p_103020_ * part.alpha);
			part.alpha = 1F;
		});
	}

}
