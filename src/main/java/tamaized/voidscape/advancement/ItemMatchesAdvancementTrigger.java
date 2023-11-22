package tamaized.voidscape.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class ItemMatchesAdvancementTrigger extends SimpleCriterionTrigger<ItemMatchesAdvancementTrigger.Instance> {

	@Override
	public ItemMatchesAdvancementTrigger.Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> player, DeserializationContext condition) {
		if(!json.has("item"))
			throw new JsonSyntaxException("ItemUsedOnNullServantTrigger: Missing item field");
		return new ItemMatchesAdvancementTrigger.Instance(player, Objects.requireNonNull(BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(json, "item")))));
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
