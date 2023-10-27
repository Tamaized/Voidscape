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
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;

import java.util.Objects;

public class ThreeByThreeTrigger extends SimpleCriterionTrigger<ThreeByThreeTrigger.Instance> {

	private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "three_by_three");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public ThreeByThreeTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
		if(!json.has("item"))
			throw new JsonSyntaxException("ThreeByThreeTrigger: Missing item field");
		return new ThreeByThreeTrigger.Instance(player, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "item")))));
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, instance -> instance.matches(stack));
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		private final Item item;

		public Instance(ContextAwarePredicate player, Item item) {
			super(ID, player);
			this.item = item;
		}

		public boolean matches(ItemStack item) {
			return item.is(this.item);
		}

	}

}
