package tamaized.voidscape.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;

public class ModelCorruptedPawnTentacle<T extends EntityCorruptedPawnTentacle> extends SegmentedModel<T> {

	private final ImmutableList<ModelRenderer> parts;

	private TransparentModelRenderer tentacle;

	public ModelCorruptedPawnTentacle() {
		super(RenderType::entityTranslucent);
		texWidth = 64;
		texHeight = 64;
		ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();

		tentacle = new TransparentModelRenderer(this, 0, 32);
		tentacle.addBox(-2F, 0F, -2F, 4, 12, 4);
		tentacle.setTexSize(64, 64);
		tentacle.mirror = true;
		builder.add(tentacle);

		this.parts = builder.build();
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entityIn.falling() && !Minecraft.getInstance().isPaused())
			tentacle.yRot += Math.toRadians(Minecraft.getInstance().getFrameTime());
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return parts;
	}


}
