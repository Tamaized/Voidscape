package tamaized.voidscape.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import tamaized.regutil.RegUtil;

import java.util.function.Function;

public class ModelArmorTitanite<T extends LivingEntity> extends HumanoidModel<T> {

	private final ImmutableList<ModelPart> parts;

	public ModelPart head;
	public ModelPart headoverlay;
	public ModelPart body;
	public ModelPart leftarm;
	public ModelPart rightarm;
	public ModelPart leftleg;
	public ModelPart rightleg;
	public ModelPart leftfoot;
	public ModelPart rightfoot;

	private final boolean fullbright;

	public ModelArmorTitanite(ModelPart p_170677_) {
		this(p_170677_, false);
	}

	public ModelArmorTitanite(ModelPart p_170677_, boolean fullbright) {
		this(p_170677_, RenderType::entityCutoutNoCull, fullbright);
	}

	public ModelArmorTitanite(ModelPart parent, Function<ResourceLocation, RenderType> p_170680_, boolean fullbright) {
		super(parent, p_170680_);
		this.fullbright = fullbright;

		ImmutableList.Builder<ModelPart> builder = ImmutableList.builder();

		builder.add(

				head = parent.getChild("realhead"),
				headoverlay = parent.getChild("headoverlay"),
				body = parent.getChild("realbody"),
				leftarm = parent.getChild("leftarm"),
				rightarm = parent.getChild("rightarm"),
				leftleg = parent.getChild("leftleg"),
				rightleg = parent.getChild("rightleg"),
				leftfoot = parent.getChild("leftfoot"),
				rightfoot = parent.getChild("rightfoot")

		);

		parts = builder.build();
	}

	public static LayerDefinition makeMesh(CubeDeformation deformation, float f) {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		// Copied from super
		definition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, 0.0F + f, 0.0F));
		definition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation.extend(0.5F)), PartPose.offset(0.0F, 0.0F + f, 0.0F));
		definition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation), PartPose.offset(0.0F, 0.0F + f, 0.0F));
		definition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.0F + f, 0.0F));
		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F + f, 0.0F));
		definition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(-1.9F, 12.0F + f, 0.0F));
		definition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F + f, 0.0F));
		// End super

		definition.addOrReplaceChild("realhead",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
						.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F))
						.texOffs(0, 32).addBox(-2.0F, -10.0F, -4.5F, 4.0F, 0.5F, 4.0F, new CubeDeformation(0.5F))
						.texOffs(0, 37).addBox(-1.0F, -11.76F, -4.74F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.26F))
						.texOffs(0, 40).addBox(-1.13F, -13.15F, -4.87F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.13F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		definition.addOrReplaceChild("headoverlay",
				CubeListBuilder.create().texOffs(32, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F).extend(0.5F)),
						PartPose.offset(0.0F, 0.0F, 0.0F));

		definition.addOrReplaceChild("realbody",
				CubeListBuilder.create().texOffs(16, 16)
						.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)),
						PartPose.offset(0.0F, 0.0F, 0.0F));

		definition.addOrReplaceChild("leftarm",
				CubeListBuilder.create().texOffs(32, 48)
						.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F))
						.texOffs(52, 60).addBox(0.24F, -3.78F, -2.24F, 3.0F, 0.02F, 3.0F, new CubeDeformation(0.76F))
						.texOffs(56, 57).addBox(1.47F, -5.55F, -2.49F, 2.01F, 0.5F, 2.0F, new CubeDeformation(0.51F))
						.texOffs(60, 55).addBox(2.745F, -7.315F, -2.745F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.255F)),
						PartPose.offset(5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("rightarm",
				CubeListBuilder.create().texOffs(40, 16)
						.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F))
						.texOffs(0, 60).addBox(-3.24F, -3.78F, -2.24F, 3.0F, 0.02F, 3.0F, new CubeDeformation(0.76F))
						.texOffs(0, 57).addBox(-3.49F, -5.55F, -2.49F, 2.01F, 0.5F, 2.0F, new CubeDeformation(0.51F))
						.texOffs(0, 55).addBox(-3.745F, -7.315F, -2.745F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.255F)),
						PartPose.offset(-5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(32, 32)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(1.9F, 24.0F, 0.0F));

		definition.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(16, 32)
				.addBox(-2F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(-1.9F, 24.0F, 0.0F));

		definition.addOrReplaceChild("leftfoot",
				CubeListBuilder.create().texOffs(16, 48)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)),
						PartPose.offset(1.9F, 12.0F, 0.0F));

		definition.addOrReplaceChild("rightfoot",
				CubeListBuilder.create().texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)),
						PartPose.offset(-1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);

	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		head.copyFrom(super.head);
		headoverlay.copyFrom(super.hat);
		body.copyFrom(super.body);
		leftarm.copyFrom(super.leftArm);
		rightarm.copyFrom(super.rightArm);
		leftleg.copyFrom(super.leftLeg);
		rightleg.copyFrom(super.rightLeg);
		leftfoot.copyFrom(super.leftLeg);
		rightfoot.copyFrom(super.rightLeg);

		parts.forEach((part) -> part.render(matrixStackIn, bufferIn, fullbright || RegUtil.renderingArmorOverlay ? 0xF000F0 : packedLightIn, packedOverlayIn, red, green, blue, alpha));
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

}
