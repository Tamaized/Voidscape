package tamaized.voidscape.client.entity.model;

import com.google.common.base.Suppliers;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.voidscape.client.shader.Shaders;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModelNullServant<S extends HumanoidRenderState> extends HumanoidModel<S> {

	@Autowired(dist = Dist.CLIENT)
	private static Shaders shaders;

	private static final Supplier<RenderType> RENDER_TYPE = Suppliers.memoize(() -> RenderType.create(
		"entity_solid_voidskyshader",
		RenderSetup.builder(shaders.VOIDSKY_ENTITY)
			.withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
			.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
			.createRenderSetup()
	));

	public ModelNullServant(ModelPart root) {
		this(root, _ -> RENDER_TYPE.get());
	}

	public ModelNullServant(ModelPart root, Function<Identifier, RenderType> renderType) {
		super(root, renderType);
	}

	public static LayerDefinition createMesh() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		PartDefinition head = definition.addOrReplaceChild(
			"head",
			CubeListBuilder.create().texOffs(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8),
			PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
		);

		head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		definition.addOrReplaceChild(
			"body",
			CubeListBuilder.create().texOffs(32, 0).addBox(-4F, 0F, -2F, 8, 12, 4),
			PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
		);

		definition.addOrReplaceChild(
			"left_arm",
			CubeListBuilder.create().texOffs(16, 16).addBox(-1F, -2F, -2F, 4, 12, 4),
			PartPose.offsetAndRotation(5F, 2F, 0F, 0F, 0F, -0.10000736613927509F)
		);

		definition.addOrReplaceChild(
			"right_arm",
			CubeListBuilder.create().texOffs(0, 16).addBox(-3F, -2F, -2F, 4, 12, 4),
			PartPose.offsetAndRotation(-5F, 2F, 0F, 0F, 0F, 0.10000736613927509F)
		);

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		definition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

}
