package tamaized.voidscape.client.shader;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.stencil.StencilOperation;
import net.neoforged.neoforge.client.stencil.StencilPerFaceTest;
import net.neoforged.neoforge.client.stencil.StencilTest;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

@Component(dist = Dist.CLIENT)
public class Shaders {

	public final String ALPHA_UNIFORM = "VoidscapeAlpha";
	public final String AURORA_UNIFORM = "VoidscapeAurora";

	private final int STENCIL_INDEX = 10;

	public final RenderPipeline POSITION_COLOR = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET).
		withLocation(id("pipeline/position_color")).
		withVertexShader("core/position_color").
		withFragmentShader("core/position_color").
		withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT)).
		withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS).
		build();

	public final RenderPipeline POSITION_TEX_COLOR = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET).
		withLocation(id("pipeline/position_tex_color")).
		withVertexShader("core/position_tex_color").
		withFragmentShader("core/position_tex_color").
		withSampler("Sampler0").
		withCull(false).
		withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT)).
		withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS).
		withDepthStencilState(DepthStencilState.DEFAULT).
		build();

	public final RenderPipeline STENCIL_ZERO_POS_COLOR = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET).
		withLocation(id("pipeline/stencil_zero_pos_color")).
		withVertexShader("core/position_color").
		withFragmentShader("core/position_color").
		withColorTargetState(new ColorTargetState(Optional.empty(), ColorTargetState.WRITE_NONE)).
		withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS).
		withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)).
		withStencilTest(new StencilTest(new StencilPerFaceTest(StencilOperation.REPLACE, StencilOperation.REPLACE, StencilOperation.REPLACE, CompareOp.ALWAYS_PASS), 0xFF, 0xFF, 0)).
		build();

	public final RenderPipeline OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET).
		withLocation(id("pipeline/optimal_alpha_lessthan_pos_tex_color")).
		withVertexShader(id("core/optimal_alpha/pos_tex_color")).
		withFragmentShader(id("core/optimal_alpha/pos_tex_color")).
		withSampler("Sampler0").
		withUniform(ALPHA_UNIFORM, UniformType.UNIFORM_BUFFER).
		withColorTargetState(new ColorTargetState(Optional.empty(), ColorTargetState.WRITE_NONE)).
		withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS).
		withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)).
		withStencilTest(new StencilTest(new StencilPerFaceTest(StencilOperation.KEEP, StencilOperation.KEEP, StencilOperation.REPLACE, CompareOp.ALWAYS_PASS), 0xFF, 0xFF, STENCIL_INDEX)).
		build();

	public final RenderPipeline STENCIL_MASKED_GUI = RenderPipeline.builder(RenderPipelines.GUI_SNIPPET).
		withLocation(id("pipeline/stencil_masked_gui")).
		withStencilTest(new StencilTest(new StencilPerFaceTest(StencilOperation.KEEP, StencilOperation.KEEP, StencilOperation.KEEP, CompareOp.EQUAL), 0xFF, 0x00, STENCIL_INDEX)).
		build();

	public final RenderPipeline VOIDSKY = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET).
		withLocation(id("pipeline/voidsky")).
		withVertexShader(id("core/voidsky/sky")).
		withFragmentShader(id("core/voidsky/sky")).
		withSampler("Sampler1").
		withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE)).
		withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS).
		build();

	public final RenderPipeline VOIDSKY_ENTITY = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET).
		withLocation(id("pipeline/voidsky_entity")).
		withVertexShader(id("core/voidsky/entity")).
		withFragmentShader(id("core/voidsky/entity")).
		withSampler("Sampler1").
		withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE)).
		withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS).
		withDepthStencilState(DepthStencilState.DEFAULT).
		build();

	public final RenderPipeline VOIDSKY_WINGS = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET).
		withLocation(id("pipeline/voidsky_wings")).
		withVertexShader(id("core/voidsky/wings")).
		withFragmentShader(id("core/voidsky/wings")).
		withSampler("Sampler0").
		withSampler("Sampler1").
		withCull(false).
		withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE)).
		withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS).
		withDepthStencilState(DepthStencilState.DEFAULT).
		build();

	public final RenderPipeline THUNDER_AURORA = RenderPipeline.
		builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET).
		withLocation(id("pipeline/thunder_aurora")).
		withVertexShader(id("core/aurora/aurora")).
		withFragmentShader(id("core/aurora/aurora")).
		withUniform(AURORA_UNIFORM, UniformType.UNIFORM_BUFFER).
		withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT)).
		withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS).
		withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false)).
		build();

	private Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, path);
	}

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(RegisterRenderPipelinesEvent.class, event -> {
			event.registerPipeline(POSITION_COLOR);
			event.registerPipeline(POSITION_TEX_COLOR);
			event.registerPipeline(STENCIL_ZERO_POS_COLOR);
			event.registerPipeline(OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR);
			event.registerPipeline(STENCIL_MASKED_GUI);
			event.registerPipeline(VOIDSKY);
			event.registerPipeline(VOIDSKY_ENTITY);
			event.registerPipeline(VOIDSKY_WINGS);
			event.registerPipeline(THUNDER_AURORA);
		});
	}

}
