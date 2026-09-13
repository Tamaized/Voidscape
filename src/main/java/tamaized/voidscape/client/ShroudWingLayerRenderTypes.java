package tamaized.voidscape.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.shader.Shaders;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

@Component(dist = Dist.CLIENT)
public class ShroudWingLayerRenderTypes {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	private final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/mask/shroud-wing.png");

	public final Supplier<RenderType> WRAPPED_POS_TEX_COLOR = Suppliers.memoize(() -> RenderType.create(
		Voidscape.MODID + "_wings_wrapped",
		renderSetup(shaders.POSITION_TEX_COLOR_NO_DEPTH_WRITE).createRenderSetup()
	));

	public final Function<WingVisualType, RenderType> WINGS = Util.memoize((type) -> RenderType.create(
		Voidscape.MODID + "_wings_" + type.name().toLowerCase(Locale.ROOT),
		wingsSetup(type).createRenderSetup()
	));

	public final Function<WingVisualType, RenderType> WINGS_ITEM_TARGET = Util.memoize((type) -> RenderType.create(
		Voidscape.MODID + "_wings_item_target_" + type.name().toLowerCase(Locale.ROOT),
		wingsSetup(type).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).createRenderSetup()
	));

	private RenderSetup.RenderSetupBuilder wingsSetup(WingVisualType type) {
		return renderSetup(type.getPipeline(shaders)).withTexture("Sampler1", type.getTexture());
	}

	private RenderSetup.RenderSetupBuilder renderSetup(RenderPipeline pipeline) {
		return RenderSetup.builder(pipeline)
			.withTexture("Sampler0", TEXTURE)
			.bufferSize(256)
			.affectsCrumbling()
			.sortOnUpload()
			.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
	}

	public enum WingVisualType {
		SKY((shaders) -> shaders.VOIDSKY_WINGS, AbstractEndPortalRenderer.END_PORTAL_LOCATION),
		ARTI((shaders) -> shaders.VOIDSKY_WINGS, projection("arti")),
		TOXIC((shaders) -> shaders.VOIDSKY_WINGS_FAST, projection("toxic"));

		private final Function<Shaders, RenderPipeline> pipeline;
		private final Identifier texture;

		WingVisualType(Function<Shaders, RenderPipeline> pipeline, Identifier texture) {
			this.pipeline = pipeline;
			this.texture = texture;
		}

		public RenderPipeline getPipeline(Shaders shaders) {
			return pipeline.apply(shaders);
		}

		public Identifier getTexture() {
			return texture;
		}

		private static Identifier projection(String name) {
			return Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/projection/" + name + ".png");
		}
	}

}
