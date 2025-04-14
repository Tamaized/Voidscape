package tamaized.voidscape.registry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.VoidSkyRenderer;

import javax.annotation.Nullable;

@Component
public class ModDimensions {

	@Autowired
	private VoidSkyRenderer voidSkyRenderer;

	public final ResourceKey<Level> VOID = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "void"));

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterDimensionSpecialEffectsEvent.class, event -> {
			event.register(VOID.location(), new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, false) {
				@Override
				public Vec3 getBrightnessDependentFogColor(Vec3 p_230494_1_, float p_230494_2_) {
					return Vec3.ZERO;
				}

				@Override
				public boolean isFoggyAt(int p_230493_1_, int p_230493_2_) {
					return true;
				}

				@Override
				@Nullable
				public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
					return null;
				}

				@Override
				public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
					voidSkyRenderer.render();
					return true;
				}
			});
		});
	}

}
