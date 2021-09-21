package tamaized.voidscape.client.entity.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
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
import tamaized.voidscape.client.RenderStateAccessor;
import tamaized.voidscape.client.Shaders;
import tamaized.voidscape.entity.EntityNullServant;

import java.util.function.Function;

public class ModelNullServant<T extends EntityNullServant> extends EntityModel<T> {

	private static final Function<ResourceLocation, RenderType> RENDERTYPE = Util.memoize((p_173204_) -> {
		RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().
				setShaderState(new RenderStateShard.ShaderStateShard(() -> Shaders.VOIDSKY_ENTITY)).
				setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(p_173204_, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).
				setTransparencyState(RenderStateAccessor.NO_TRANSPARENCY()).
				setLightmapState(RenderStateAccessor.LIGHTMAP()).
				setOverlayState(RenderStateAccessor.OVERLAY()).
				createCompositeState(true);
		return RenderType.create("entity_solid_voidskyshader", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
	});

	public ModelPart head;
	public ModelPart body;
	public ModelPart leftArm;
	public ModelPart rightArm;

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

	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int p_103113_, int p_103114_, float r, float g, float b, float a) {
		head.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		body.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		leftArm.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
		rightArm.render(stack, buffer, p_103113_, p_103114_, r, g, b, a);
	}
}
