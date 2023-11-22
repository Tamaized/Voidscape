package tamaized.voidscape.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GenericAdvancementTrigger extends SimpleCriterionTrigger<GenericAdvancementTrigger.Instance> {

	@Override
	public GenericAdvancementTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext condition) {
		return new GenericAdvancementTrigger.Instance(player);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> player) {
			super(player);
		}

	}

}
