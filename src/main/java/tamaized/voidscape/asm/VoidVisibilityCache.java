package tamaized.voidscape.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import tamaized.voidscape.registry.ModAttributes;

public class VoidVisibilityCache {

	private static double attributeCache;
	private static float[] brightnessCache;

	public static float value(float o, int l) {
		if (Minecraft.getInstance().player == null)
			return o;
		double attribute = Minecraft.getInstance().player.getAttributeValue(ModAttributes.VOIDIC_VISIBILITY.get());
		o = -0.3F; // hardcode for now, need to get this value from the DimType
		double light = o + (2D - o) * (attribute - 1D);
		if (brightnessCache == null || attributeCache != attribute) {
			brightnessCache = fillBrightnessRamp((float) light);
			attributeCache = attribute;
		}
		return brightnessCache[l];
	}

	private static float[] fillBrightnessRamp(float light) {
		float[] afloat = new float[16];

		for (int i = 0; i <= 15; ++i) {
			float f = (float) i / 15.0F;
			float f1 = f / (4.0F - 3.0F * f);
			afloat[i] = Mth.lerp(light, f1, 1.0F);
		}

		return afloat;
	}

}
