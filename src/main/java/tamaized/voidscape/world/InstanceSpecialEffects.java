package tamaized.voidscape.world;

import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class InstanceSpecialEffects {

	private static final Map<ResourceLocation, List<Consumer<Instance>>> REGISTRY = new HashMap<>();

	static {
		registerEffect(new ResourceLocation(Voidscape.MODID, "pawn"), (instance) -> instance.getLevel().players().forEach(player -> {
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapInsanity).ifPresent(data -> {
				data.decrementEffects = false;
				float rate = data.calcInfusionRate(player);
				if (rate > 0.8F)
					rate = 5F;
				else
					rate *= 0.025F;
				data.setInfusion(data.getInfusion() + rate);
			}));
		}));
	}

	private InstanceSpecialEffects() {

	}

	@SafeVarargs
	public static void registerEffect(ResourceLocation instance, Consumer<Instance>... effects) {
		REGISTRY.computeIfAbsent(instance, (e) -> new ArrayList<>());
		for (Consumer<Instance> effect : effects)
			REGISTRY.get(instance).add(effect);
	}

	public static void doEffects(Instance instance) {
		REGISTRY.computeIfAbsent(instance.generator().group(), (e) -> new ArrayList<>());
		REGISTRY.get(instance.generator().group()).forEach(effect -> effect.accept(instance));
	}

}
