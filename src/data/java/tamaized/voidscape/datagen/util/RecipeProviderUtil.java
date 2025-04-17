package tamaized.voidscape.datagen.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import tamaized.beanification.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeProviderUtil {

	public Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike));
	}

	public Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> itemTag) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag));
	}

	public Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate.Builder... items) {
		return inventoryTrigger(Arrays.stream(items).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
	}

	public Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) {
		return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
	}

}
