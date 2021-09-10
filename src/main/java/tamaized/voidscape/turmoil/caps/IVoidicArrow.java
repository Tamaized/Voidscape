package tamaized.voidscape.turmoil.caps;

import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;

public interface IVoidicArrow {

	ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "voidicarrow");

	ResourceLocation ID_VOIDIC = new ResourceLocation(Voidscape.MODID, "voidic");
	ResourceLocation ID_FIRE = new ResourceLocation(Voidscape.MODID, "fire");

	void mark(ResourceLocation id, float dmg);

	float active(ResourceLocation id);

}
