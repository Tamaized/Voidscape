package tamaized.voidscape.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;

public class ThreeByThreeTrigger extends SimpleCriterionTrigger<ThreeByThreeTrigger.Instance> {

	@Override
	public ThreeByThreeTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext condition) {
		if(!json.has("item"))
			throw new JsonSyntaxException("ThreeByThreeTrigger: Missing item field");
		return new ThreeByThreeTrigger.Instance(player, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "item")))));
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, instance -> instance.matches(stack));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final Item item;

		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> player, Item item) {
			super(player);
			this.item = item;
		}

		public boolean matches(ItemStack item) {
			return item.is(this.item);
		}

	}

}
