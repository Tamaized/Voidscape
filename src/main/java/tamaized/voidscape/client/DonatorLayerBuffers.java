package tamaized.voidscape.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.shader.Shaders;

import java.util.function.Supplier;

@Component(dist = Dist.CLIENT)
public class DonatorLayerBuffers {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	private final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/entity/donator.png");

	public final Supplier<RenderType> WRAPPED_POS_TEX_COLOR = Suppliers.
		memoize(() -> RenderType.create("voidscape_wings_wrapped", renderSetup(shaders.POSITION_TEX_COLOR).createRenderSetup()));

	public final Supplier<RenderType> WINGS = Suppliers.
		memoize(() -> RenderType.create("voidscape_wings", renderSetup(shaders.VOIDSKY_WINGS).
			withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION).
			createRenderSetup()));

	public final Supplier<MultiBufferSource.BufferSource> BUFFERS = Suppliers.
		memoize(() -> MultiBufferSource.immediateWithBuffers(Util.make(new Object2ObjectLinkedOpenHashMap<>(), map -> {
			map.put(WRAPPED_POS_TEX_COLOR.get(), new ByteBufferBuilder(WRAPPED_POS_TEX_COLOR.get().bufferSize()));
			map.put(WINGS.get(), new ByteBufferBuilder(WINGS.get().bufferSize()));
		}), new ByteBufferBuilder(256)));

	private RenderSetup.RenderSetupBuilder renderSetup(RenderPipeline pipeline) {
		return RenderSetup.builder(pipeline).
			withTexture("Sampler0", TEXTURE).
			bufferSize(256).
			affectsCrumbling().
			sortOnUpload().
			setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
	}

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(RenderLevelStageEvent.AfterTranslucentBlocks.class, _ -> BUFFERS.get().endBatch());
	}

}
