package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
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
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.registry.ModBiomes;

@Component(dist = Dist.CLIENT)
public class ThunderAuroraRenderer {

	@Autowired(dist = Dist.CLIENT)
	private ModBiomes biomes;

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	private int aurora, lastAurora;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ClientTickEvent.Post.class, event -> {
			Minecraft mc = Minecraft.getInstance();

			if (mc.isPaused())
				return;

			lastAurora = aurora;
			if (mc.level != null && mc.cameraEntity != null) {
				Holder<Biome> biome = mc.level.getBiome(mc.cameraEntity.blockPosition());
				if (biome.is(biomes.THUNDER_FOREST) || biome.is(biomes.THUNDERSPIRES))
					aurora++;
				else
					aurora--;
				aurora = Mth.clamp(aurora, 0, 60);
			} else {
				aurora = 0;
			}
		});
		bus.addListener(RenderLevelStageEvent.class, event -> {
			if (Minecraft.getInstance().level == null)
				return;

			if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && (aurora > 0 || lastAurora > 0) && shaders.THUNDER_AURORA != null) {
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				final float scale = 2048F * (Minecraft.getInstance().gameRenderer.getRenderDistance() / 32F);
				Vec3 pos = event.getCamera().getPosition();
				float y = (float) (256F - pos.y());
				buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

				RenderSystem.enableBlend();
				RenderSystem.enableDepthTest();
				RenderSystem.setShaderColor(
					1F,
					1F,
					1F,
					(Mth.lerp(event.getPartialTick().getGameTimeDeltaTicks(), lastAurora, aurora)) / 60F * 0.5F
				);
				shaders.THUNDER_AURORA.invokeThenUpload(
					Minecraft.getInstance().level == null ? 0 : Mth.abs((int) Minecraft.getInstance().level.getBiomeManager().biomeZoomSeed),
					(float) pos.x(),
					(float) pos.y(),
					(float) pos.z(),
					buffer
				);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.disableDepthTest();
				RenderSystem.disableBlend();
			}
		});
	}

}
