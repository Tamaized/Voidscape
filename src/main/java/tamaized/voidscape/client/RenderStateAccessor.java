package tamaized.voidscape.client;

import net.minecraft.client.renderer.RenderState;

public class RenderStateAccessor extends RenderState {
	private RenderStateAccessor(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static TransparencyState NO_TRANSPARENCY() {
		return NO_TRANSPARENCY;
	}

	public static WriteMaskState COLOR_DEPTH_WRITE() {
		return COLOR_DEPTH_WRITE;
	}

	public static TransparencyState LIGHTNING_TRANSPARENCY() {
		return LIGHTNING_TRANSPARENCY;
	}

	public static TargetState WEATHER_TARGET() {
		return WEATHER_TARGET;
	}

	public static ShadeModelState SMOOTH_SHADE() {
		return SMOOTH_SHADE;
	}

	public static DiffuseLightingState DIFFUSE_LIGHTING() {
		return DIFFUSE_LIGHTING;
	}

	public static AlphaState DEFAULT_ALPHA() {
		return DEFAULT_ALPHA;
	}

	public static CullState NO_CULL() {
		return NO_CULL;
	}

	public static LightmapState LIGHTMAP() {
		return LIGHTMAP;
	}

	public static OverlayState OVERLAY() {
		return OVERLAY;
	}
}
