package tamaized.voidscape.config.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.ModConfigSpec;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.util.ColorHelper;

@Component(dist = Dist.CLIENT)
public class HexColorEditBoxFactory {

	@Autowired(dist = Dist.CLIENT)
	private ColorHelper colorHelper;

	public EditBox make(ModConfigSpec.IntValue configOption) {
		EditBox input = new EditBox(
			Minecraft.getInstance().font,
			Button.DEFAULT_WIDTH,
			Button.DEFAULT_HEIGHT,
			net.minecraft.network.chat.Component.literal("Test")
		);

		input.setValue(Integer.toHexString(configOption.getAsInt()));
		input.setTextColor(resolveColor(input.getValue()));

		input.setResponder(newValue -> {
			if (newValue != null) {
				int decValue;
				try {
					decValue = (int) Long.parseLong(input.getValue(), 16);
				} catch (NumberFormatException ex) {
					input.setTextColor(0xFFFFFF);
					return;
				}
				if (decValue != configOption.getAsInt()) {
					configOption.set(decValue);
					configOption.save();
				}
				input.setTextColor(resolveColor(input.getValue()));
			}
		});

		return input;
	}

	private int resolveColor(String hex) {
		try {
			int val = Integer.decode("0x" + hex);
			ColorHelper.HSV hsv = colorHelper.rgbToHsv(val);
			return hsv.value() > 0.25F ? val : Mth.hsvToRgb(hsv.hue(), hsv.saturation(), 0.25F);
		} catch (NumberFormatException ex) {
			return 0xFFFFFF;
		}
	}

}
