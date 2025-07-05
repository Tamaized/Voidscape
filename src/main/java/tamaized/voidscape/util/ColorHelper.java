package tamaized.voidscape.util;

import tamaized.beanification.Component;

@Component
public class ColorHelper {

	public int colorWithAlphaMul(int color, float alphaPerc) {
		return color | (((int) (((color >> 24) & 0xFF) * alphaPerc)) << 24);
	}

	public HSV rgbToHsv(int color) {
		float r = ((color >> 16) & 0xFF) / 255F;
		float g = ((color >> 8) & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;

		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));
		float delta = max - min;

		float h = 0;
		float s = 0;

		if (delta != 0) {
			if (max == r) {
				h = ((g - b) / delta) % 6;
			} else if (max == g) {
				h = ((b - r) / delta) + 2;
			} else if (max == b) {
				h = ((r - g) / delta) + 4;
			}
			h /= 6;
			if (h < 0) {
				h += 1;
			}
		}

		if (max != 0) {
			s = delta / max;
		}

		return new HSV(h, s, max);

	}

	public record HSV(float hue, float saturation, float value) {

	}
}
