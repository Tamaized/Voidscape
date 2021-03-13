package tamaized.voidscape.turmoil.caps;

import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;

public interface IFireArrow {

	static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "firearrow");

	void mark();

	boolean active();

}
