package tamaized.voidscape.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;

import java.io.IOException;

@Component
public class Shaders {

	public OptimalAlphaShaderInstance OPTIMAL_ALPHA_LESSTHAN_POS_COLOR;
	public OptimalAlphaShaderInstance OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR;
	public OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR;
	public OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_TEX;
	public OptimalAlphaShaderInstance OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR;
	public WrappedBindableShaderInstance WRAPPED_POS_COLOR;
	public WrappedBindableShaderInstance WRAPPED_POS_TEX;
	public WrappedBindableShaderInstance WRAPPED_POS_TEX_COLOR;
	public BindableShaderInstance LINES;
	public BindableShaderInstance VOIDSKY;
	public BindableShaderInstance VOIDSKY_ENTITY;
	public BindableShaderInstance VOIDSKY_WINGS;

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(RegisterShadersEvent.class, event -> {
			try {
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "optimal_alpha/lessthan/pos_color"), DefaultVertexFormat.
						POSITION_COLOR), shader -> OPTIMAL_ALPHA_LESSTHAN_POS_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "optimal_alpha/lessthan/pos_tex_color"), DefaultVertexFormat.
						POSITION_TEX_COLOR), shader -> OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "optimal_alpha/greaterthan/pos_color"), DefaultVertexFormat.
						POSITION_COLOR), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "optimal_alpha/greaterthan/pos_tex"), DefaultVertexFormat.
						POSITION_TEX), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_TEX = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new OptimalAlphaShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "optimal_alpha/greaterthan/pos_tex_color"), DefaultVertexFormat.
						POSITION_TEX_COLOR), shader -> OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR = (OptimalAlphaShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voidsky/sky"), DefaultVertexFormat.
						POSITION), shader -> VOIDSKY = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voidsky/entity"), DefaultVertexFormat.
						NEW_ENTITY), shader -> VOIDSKY_ENTITY = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "voidsky/wings"), DefaultVertexFormat.
						POSITION_TEX), shader -> VOIDSKY_WINGS = (BindableShaderInstance) shader);
				event.registerShader(new BindableShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "lines/lines"), DefaultVertexFormat.
						POSITION_COLOR_NORMAL), shader -> LINES = (BindableShaderInstance) shader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		WRAPPED_POS_COLOR = WrappedBindableShaderInstance.make(GameRenderer::getPositionColorShader);
		WRAPPED_POS_TEX = WrappedBindableShaderInstance.make(GameRenderer::getPositionTexShader);
		WRAPPED_POS_TEX_COLOR = WrappedBindableShaderInstance.make(GameRenderer::getPositionTexColorShader);
	}

}
