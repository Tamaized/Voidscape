package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class EntropicGearAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private EntropicRiftCloseAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "entropic_gear";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.toolSetComponentDirectory().entropicToolSet().ENTROPIC_AXE.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.AND)
			.addCriterion("entropic_axe", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().entropicToolSet().ENTROPIC_AXE.get()
			))
			.addCriterion("entropic_shield", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().entropicToolSet().ENTROPIC_SHIELD.get()
			))
			.addCriterion("entropic_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().entropicArmorSet().ENTROPIC_HELMET.get()
			))
			.addCriterion("entropic_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().entropicArmorSet().ENTROPIC_CHEST.get()
			))
			.addCriterion("entropic_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().entropicArmorSet().ENTROPIC_LEGS.get()
			))
			.addCriterion("entropic_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().entropicArmorSet().ENTROPIC_BOOTS.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
