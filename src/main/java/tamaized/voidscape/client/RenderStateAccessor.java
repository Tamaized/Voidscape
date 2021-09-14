package tamaized.voidscape.client;

import net.minecraft.client.renderer.RenderStateShard;

public class RenderStateAccessor extends RenderStateShard {
	private RenderStateAccessor(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static TransparencyStateShard NO_TRANSPARENCY() {
		return NO_TRANSPARENCY;
	}

	public static WriteMaskStateShard COLOR_DEPTH_WRITE() {
		return COLOR_DEPTH_WRITE;
	}

	public static TransparencyStateShard LIGHTNING_TRANSPARENCY() {
		return LIGHTNING_TRANSPARENCY;
	}

	public static OutputStateShard WEATHER_TARGET() {
		return WEATHER_TARGET;
	}

	public static CullStateShard NO_CULL() {
		return NO_CULL;
	}

	public static LightmapStateShard LIGHTMAP() {
		return LIGHTMAP;
	}

	public static LightmapStateShard NO_LIGHTMAP() {
		return NO_LIGHTMAP;
	}

	public static OverlayStateShard OVERLAY() {
		return OVERLAY;
	}

	public static ShaderStateShard RENDERTYPE_LIGHTNING_SHADER() {
		return RENDERTYPE_LIGHTNING_SHADER;
	}

	public static ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER() {
		return RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER;
	}
}
