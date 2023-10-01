package tamaized.voidscape.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tamaized.voidscape.Voidscape;

public class ActivatePortalTrigger extends SimpleCriterionTrigger<ActivatePortalTrigger.Instance> {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "activate_portal");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public ActivatePortalTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		return new ActivatePortalTrigger.Instance(player);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		public Instance(ContextAwarePredicate player) {
			super(ID, player);
		}

	}

}
