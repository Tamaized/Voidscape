package tamaized.voidscape.client;

import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;

@Component(dist = Dist.CLIENT)
public class VoidDimensionSpecialEffectsFactory { // FIXME: EnvironmentAttribute

	@Autowired(dist = Dist.CLIENT)
	private VoidSkyRenderer voidSkyRenderer;

	/*public DimensionSpecialEffects make() {
		return new DimensionSpecialEffects(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, false) {
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
		};
	}*/

}
