package tamaized.voidscape.client.armor.model;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface IOverlayArmorModel {

	@Nullable
	Identifier overlayTexture();

	boolean overlayFullbright();

}
