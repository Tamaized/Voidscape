package tamaized.voidscape.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModAttributes;

@Component
public class VoidVisibilityCache {

	@Autowired
	private ModAttributes attributes;

	private double attributeCache;
	private float[] brightnessCache = new float[0];

	public float value(float o, int l) {
		if (Minecraft.getInstance().player == null)
			return o;
		double attribute = Minecraft.getInstance().player.getAttributeValue(attributes.VOIDIC_VISIBILITY);
		o = -0.3F; // hardcode for now, need to get this value from the DimType
		// Note to self: LightTexture#getBrightness contains the DimType
		double light = attribute > 0 ? attribute : o;
		if (brightnessCache.length == 0 || attributeCache != attribute) {
			brightnessCache = fillBrightnessRamp((float) light);
			attributeCache = attribute;
		}
		return brightnessCache[l];
	}

	private float[] fillBrightnessRamp(float light) {
		float[] afloat = new float[16];

		for (int i = 0; i <= 15; ++i) {
			float f = (float) i / 15.0F;
			float f1 = f / (4.0F - 3.0F * f);
			afloat[i] = Mth.lerp(light, f1, 1.0F);
		}

		return afloat;
	}

}
