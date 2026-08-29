package tamaized.voidscape.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.shader.Shaders;

import java.util.function.Supplier;

@Component(dist = Dist.CLIENT)
public class DonatorLayerRenderTypes {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	private final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/donator.png");

	public final Supplier<RenderType> WRAPPED_POS_TEX_COLOR = Suppliers.
		memoize(() -> RenderType.create("voidscape_wings_wrapped",
			renderSetup(shaders.POSITION_TEX_COLOR_NO_DEPTH_WRITE).createRenderSetup()));

	public final Supplier<RenderType> WINGS = Suppliers.
		memoize(() -> RenderType.create("voidscape_wings", wingsSetup().createRenderSetup()));

	public final Supplier<RenderType> WINGS_ITEM_TARGET = Suppliers.
		memoize(() -> RenderType.create("voidscape_wings_item_target", wingsSetup().
			setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).
			createRenderSetup()));

	private RenderSetup.RenderSetupBuilder wingsSetup() {
		return renderSetup(shaders.VOIDSKY_WINGS).
			withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION);
	}

	private RenderSetup.RenderSetupBuilder renderSetup(RenderPipeline pipeline) {
		return RenderSetup.builder(pipeline).
			withTexture("Sampler0", TEXTURE).
			bufferSize(256).
			affectsCrumbling().
			sortOnUpload().
			setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
	}

}
