package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.shader.Shaders;

import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class DonatorLayerBuffers {

	@Autowired
	private Shaders shaders;

	private final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/donator.png");

	private final Function<Supplier<ShaderInstance>, RenderType> RENDER_TYPE = Util.memoize(shader -> RenderType.create("voidscape_wings", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().
		setTransparencyState(RenderStateAccessor.TRANSLUCENT_TRANSPARENCY()).
		setCullState(RenderStateAccessor.NO_CULL()).
		setShaderState(new RenderStateShard.ShaderStateShard(shader)).
		setTextureState(new RenderStateShard.MultiTextureStateShard.Builder().add(TEXTURE, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).
		createCompositeState(true)));

	public final RenderType WRAPPED_POS_TEX_COLOR = RENDER_TYPE.apply(GameRenderer::getPositionTexColorShader);
	public final RenderType WINGS = RENDER_TYPE.apply(() -> shaders.VOIDSKY_WINGS);

	public final MultiBufferSource.BufferSource BUFFERS = MultiBufferSource.immediateWithBuffers(Util.make(new Object2ObjectLinkedOpenHashMap<>(), map -> {
		map.put(WRAPPED_POS_TEX_COLOR, new ByteBufferBuilder(WRAPPED_POS_TEX_COLOR.bufferSize()));
		map.put(WINGS, new ByteBufferBuilder(WINGS.bufferSize()));
	}), new ByteBufferBuilder(256));

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RenderLevelStageEvent.class, event -> {
			if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
				BUFFERS.endBatch();
		});
	}

}
