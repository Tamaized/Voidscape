package tamaized.voidscape.registry;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.Objects;
import java.util.UUID;

public class ModAdvancementTriggers implements RegistryClass {

	@Override
	public void init(IEventBus bus) {

	}

	public static final ItemUsedOnNullServantTrigger ITEM_USED_ON_NULL_SERVANT_TRIGGER = CriteriaTriggers.register(new ItemUsedOnNullServantTrigger());

	public static class ItemUsedOnNullServantTrigger extends SimpleCriterionTrigger<ItemUsedOnNullServantTrigger.Instance> {

		private static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "item_used_on_null_servant");

		@Override
		public ResourceLocation getId() {
			return ID;
		}

		@Override
		public Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext condition) {
			if(!json.has("item"))
				throw new JsonSyntaxException("ItemUsedOnNullServantTrigger: Missing item field");
			return new ItemUsedOnNullServantTrigger.Instance(player, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "item")))));
		}

		public void trigger(ServerPlayer player, ItemStack stack) {
			this.trigger(player, instance -> instance.matches(stack));
		}

		private static class Instance extends AbstractCriterionTriggerInstance {

			private Item item;

			public Instance(ContextAwarePredicate player, Item item) {
				super(ID, player);
				this.item = item;
			}

			public boolean matches(ItemStack item) {
				return item.is(this.item);
			}

		}

	}

}
