package tamaized.voidscape.capability;

import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.Voidscape;

public interface IVoidicArrow {

	ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "voidicarrow");

	void setDamage(float dmg);

	float getDamage();

}
