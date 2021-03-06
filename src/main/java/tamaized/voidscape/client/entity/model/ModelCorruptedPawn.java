package tamaized.voidscape.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import tamaized.voidscape.entity.EntityCorruptedPawn;

public class ModelCorruptedPawn<T extends EntityCorruptedPawn> extends SegmentedModel<T> {

	private static final int[] CAST_MOVEMENT = {225, 315, 135, 270, 45, 180, 0, 90};

	private final ImmutableList<ModelRenderer> parts;

	private final ModelRenderer head;
	private final TransparentModelRenderer topTentacle;
	private final TransparentModelRenderer topRightTentacle;
	private final TransparentModelRenderer rightTentacle;
	private final TransparentModelRenderer bottomRightTentacle;
	private final TransparentModelRenderer bottomTentacle;
	private final TransparentModelRenderer bottomLeftTentacle;
	private final TransparentModelRenderer leftTentacle;
	private final TransparentModelRenderer topLeftTentacle;

	public ModelCorruptedPawn() {
		super(RenderType::entityTranslucent);
		texWidth = 64;
		texHeight = 64;
		ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 16, 16, 16);
		head.setPos(-4F, 0F, -2F);
		head.setTexSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		builder.add(head);

		topTentacle = new TransparentModelRenderer(this, 0, 32);
		topTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		topTentacle.setPos(0F, -9F, 2F);
		topTentacle.setTexSize(64, 64);
		topTentacle.mirror = true;
		setRotation(topTentacle, 0F, 0F, 3.141593F);
		builder.add(topTentacle);

		topRightTentacle = new TransparentModelRenderer(this, 0, 32);
		topRightTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		topRightTentacle.setPos(-9F, -9F, 2F);
		topRightTentacle.setTexSize(64, 64);
		topRightTentacle.mirror = true;
		setRotation(topRightTentacle, 0F, 0F, 2.324799F);
		builder.add(topRightTentacle);

		rightTentacle = new TransparentModelRenderer(this, 0, 32);
		rightTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightTentacle.setPos(-9F, 0F, 2F);
		rightTentacle.setTexSize(64, 64);
		rightTentacle.mirror = true;
		setRotation(rightTentacle, 0F, 0F, 1.570796F);
		builder.add(rightTentacle);

		bottomRightTentacle = new TransparentModelRenderer(this, 0, 32);
		bottomRightTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		bottomRightTentacle.setPos(-9F, 9F, 2F);
		bottomRightTentacle.setTexSize(64, 64);
		bottomRightTentacle.mirror = true;
		setRotation(bottomRightTentacle, 0F, 0F, 0.7435722F);
		builder.add(bottomRightTentacle);

		bottomTentacle = new TransparentModelRenderer(this, 0, 32);
		bottomTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		bottomTentacle.setPos(0F, 9F, 2F);
		bottomTentacle.setTexSize(64, 64);
		bottomTentacle.mirror = true;
		setRotation(bottomTentacle, 0F, 0F, 0F);
		builder.add(bottomTentacle);

		bottomLeftTentacle = new TransparentModelRenderer(this, 0, 32);
		bottomLeftTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		bottomLeftTentacle.setPos(9F, 9F, 2F);
		bottomLeftTentacle.setTexSize(64, 64);
		bottomLeftTentacle.mirror = true;
		setRotation(bottomLeftTentacle, 0F, 0F, -0.7435801F);
		builder.add(bottomLeftTentacle);

		leftTentacle = new TransparentModelRenderer(this, 0, 32);
		leftTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		leftTentacle.setPos(9F, 0F, 2F);
		leftTentacle.setTexSize(64, 64);
		leftTentacle.mirror = true;
		setRotation(leftTentacle, 0F, 0F, -1.570796F);
		builder.add(leftTentacle);

		topLeftTentacle = new TransparentModelRenderer(this, 0, 32);
		topLeftTentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		topLeftTentacle.setPos(9F, -9F, 2F);
		topLeftTentacle.setTexSize(64, 64);
		topLeftTentacle.mirror = true;
		setRotation(topLeftTentacle, 0F, 0F, -2.324796F);
		builder.add(topLeftTentacle);

		this.parts = builder.build();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);

		float swing = limbSwingAmount * 4F;
		float swingCorner = swing * 0.45F + MathHelper.sin(limbSwing) * 0.25F;
		float swingCardinal = swing * 0.95F + MathHelper.cos(limbSwing) * 0.25F;

		this.topRightTentacle.xRot = swingCorner;
		this.topLeftTentacle.xRot = swingCorner;
		this.bottomRightTentacle.xRot = swingCorner;
		this.bottomLeftTentacle.xRot = swingCorner;
		this.topTentacle.xRot = swingCardinal;
		this.leftTentacle.xRot = swingCardinal;
		this.rightTentacle.xRot = swingCardinal;
		this.bottomTentacle.xRot = swingCardinal;

		int i = -1;
		for (ModelRenderer part : parts) {
			if (part instanceof TransparentModelRenderer) {
				if (entity.isCasting()) {
					float rot = (float) Math.toRadians(CAST_MOVEMENT[i] + (entity.tickCount - entity.castTick) * 25L);
					float sine = MathHelper.sin(rot) * 0.5F;
					float cosine = MathHelper.cos(rot) * 0.5F;
					part.yRot = sine + cosine;
					part.xRot = cosine - sine;
				} else
					part.yRot = 0;
				boolean state = ((entity.getTentacleBits() >> (7 - i)) & 0b1) == 1;
				float perc = MathHelper.clamp(1F - (entity.tentacleTimes[i] - entity.tickCount + Minecraft.getInstance().getFrameTime()) / (20F * 5F), 0F, 1F);
				float alpha = MathHelper.clamp(entity.tickCount >= entity.tentacleTimes[i] ? state ? 0F : 1F : state ? 1F - perc : perc, 0F, 1F);
				part.visible = alpha > 0F;
				((TransparentModelRenderer) part).alpha = alpha;
			}
			i++;
		}
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return parts;
	}


}
