package tamaized.voidscape.turmoil.caps;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class VoidicArrowCapability implements IVoidicArrow {

	private final Map<ResourceLocation, Float> actives = new HashMap<>();

	@Override
	public void mark(ResourceLocation id, float dmg) {
		actives.putIfAbsent(id, dmg);
	}

	@Override
	public float active(ResourceLocation id) {
		return actives.getOrDefault(id, 0F);
	}
}
