package tamaized.voidscape.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ModelArmorCorrupt<T extends LivingEntity> extends BipedModel<T> {

	private final ImmutableList<ModelRenderer> parts;

	public ModelRenderer head;
	public ModelRenderer headoverlay;
	public ModelRenderer body;
	public ModelRenderer rightarm;
	public ModelRenderer leftarm;
	public ModelRenderer bodyToLeg;
	public ModelRenderer rightleg;
	public ModelRenderer leftleg;
	public ModelRenderer rightfoot;
	public ModelRenderer leftfoot;
	public ModelRenderer topLeftTentacle;
	public ModelRenderer topRightTentacle;
	public ModelRenderer bottomLeftTentacle;
	public ModelRenderer bottomRightTentacle;

	public ModelArmorCorrupt(float modelSize) {
		super(modelSize);
		this.texWidth = 64;
		this.texHeight = 64;

		ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();

		this.rightfoot = new ModelRenderer(this, 0, 16);
		this.rightfoot.setPos(-1.9F, 12.0F, 0.0F);
		this.rightfoot.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(rightfoot);

		this.bottomLeftTentacle = new ModelRenderer(this, 42, 53);
		this.bottomLeftTentacle.setPos(1.8F, 7.0F, 1.5F);
		this.bottomLeftTentacle.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(bottomLeftTentacle, -0.5970771211282936F, 0.5473352640780661F, 0.0F);

		this.body = new ModelRenderer(this, 16, 16);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(body);

		this.leftarm = new ModelRenderer(this, 40, 16);
		this.leftarm.mirror = true;
		this.leftarm.setPos(5.0F, 2.0F, 0.0F);
		this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(leftarm);

		this.bottomRightTentacle = new ModelRenderer(this, 42, 53);
		this.bottomRightTentacle.setPos(-1.8F, 7.0F, 1.5F);
		this.bottomRightTentacle.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(bottomRightTentacle, -0.5970771211282936F, -0.5473352640780661F, 0.0F);

		this.topLeftTentacle = new ModelRenderer(this, 42, 53);
		this.topLeftTentacle.setPos(1.8F, 3.0F, 1.5F);
		this.topLeftTentacle.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(topLeftTentacle, 0.5970771211282936F, 0.5473352640780661F, 0.0F);

		this.topRightTentacle = new ModelRenderer(this, 42, 53);
		this.topRightTentacle.setPos(-1.8F, 3.0F, 1.5F);
		this.topRightTentacle.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(topRightTentacle, 0.5970771211282936F, -0.5473352640780661F, 0.0F);

		this.rightarm = new ModelRenderer(this, 40, 16);
		this.rightarm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(rightarm);

		this.headoverlay = new ModelRenderer(this, 32, 0);
		this.headoverlay.setPos(0.0F, 0.0F, 0.0F);
		this.headoverlay.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, 1.0F, 1.0F);
		builder.add(headoverlay);

		this.leftleg = new ModelRenderer(this, 0, 48);
		this.leftleg.mirror = true;
		this.leftleg.setPos(1.9F, 12.0F, 0.0F);
		this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(leftleg);

		this.bodyToLeg = new ModelRenderer(this, 16, 48);
		this.bodyToLeg.setPos(0.0F, 0.0F, 0.0F);
		this.bodyToLeg.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(bodyToLeg);

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, 0.5F, 0.5F);
		builder.add(head);

		this.leftfoot = new ModelRenderer(this, 0, 16);
		this.leftfoot.mirror = true;
		this.leftfoot.setPos(1.9F, 12.0F, 0.0F);
		this.leftfoot.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(leftfoot);

		this.rightleg = new ModelRenderer(this, 0, 48);
		this.rightleg.setPos(-1.9F, 12.0F, 0.0F);
		this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, 0.5F, 0.5F);
		builder.add(rightleg);

		this.body.addChild(this.bottomLeftTentacle);
		this.body.addChild(this.bottomRightTentacle);
		this.body.addChild(this.topLeftTentacle);
		this.body.addChild(this.topRightTentacle);

		parts = builder.build();
	}

	private void setRotateAngle(ModelRenderer model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		head.copyFrom(super.head);
		headoverlay.copyFrom(super.hat);
		body.copyFrom(super.body);
		rightarm.copyFrom(super.rightArm);
		leftarm.copyFrom(super.leftArm);
		bodyToLeg.copyFrom(body);
		rightleg.copyFrom(super.rightLeg);
		leftleg.copyFrom(super.leftLeg);
		rightfoot.copyFrom(super.rightLeg);
		leftfoot.copyFrom(super.leftLeg);
		parts.forEach((modelRenderer_) -> modelRenderer_.render(matrixStackIn, bufferIn, 0xF000F0, packedOverlayIn, red, green, blue, alpha));
	}
}
