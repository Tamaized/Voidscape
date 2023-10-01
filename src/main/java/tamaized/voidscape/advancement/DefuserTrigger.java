package tamaized.voidscape.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tamaized.voidscape.Voidscape;

public class DefuserTrigger extends SimpleCriterionTrigger<DefuserTrigger.Instance> {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "defuser");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public DefuserTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		return new DefuserTrigger.Instance(player);
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
