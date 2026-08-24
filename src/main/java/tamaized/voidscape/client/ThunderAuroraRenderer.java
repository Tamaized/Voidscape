package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Vector4f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.shader.ShaderRenderer;
import tamaized.voidscape.registry.ModBiomes;

@Component(dist = Dist.CLIENT)
public class ThunderAuroraRenderer {

	@Autowired(dist = Dist.CLIENT)
	private ModBiomes biomes;

	@Autowired(dist = Dist.CLIENT)
	private ShaderRenderer shaderRenderer;

	private int aurora, lastAurora;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ClientTickEvent.Post.class, event -> {
			Minecraft mc = Minecraft.getInstance();

			if (mc.isPaused())
				return;

			lastAurora = aurora;
			if (mc.level != null && mc.getCameraEntity() != null) {
				Holder<Biome> biome = mc.level.getBiome(mc.getCameraEntity().blockPosition());
				if (biome.is(biomes.THUNDER_FOREST) || biome.is(biomes.THUNDERSPIRES))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}
		});
		bus.addListener(RenderLevelStageEvent.AfterWeather.class, event -> {
			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null)
				return;

			if (aurora > 0 || lastAurora > 0) {
				BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				final float scale = 2048F * (mc.options.getEffectiveRenderDistance() * 16F / 32F);
				Vec3 pos = event.getLevelRenderState().cameraRenderState.pos;
				float y = (float) (256F - pos.y());
				buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

				float alpha = Mth.lerp(mc.getDeltaTracker().getGameTimeDeltaTicks(), lastAurora, aurora) / 60F * 0.5F;
				shaderRenderer.drawAurora(
					buffer.buildOrThrow(),
					new Vector4f(1F, 1F, 1F, alpha),
					Mth.abs((int) mc.level.getBiomeManager().biomeZoomSeed),
					(float) pos.x(),
					(float) pos.y(),
					(float) pos.z()
				);
			}
		});
	}

}
