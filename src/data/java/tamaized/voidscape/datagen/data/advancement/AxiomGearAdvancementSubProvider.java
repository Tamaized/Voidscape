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
public class AxiomGearAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private AxiomGearItemAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "axiom_gear";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.toolSetComponentDirectory().axiomToolSet().AXIOM_XBOW.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.AND)
			.addCriterion("axiom_axe", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().axiomToolSet().AXIOM_XBOW.get()
			))
			.addCriterion("axiom_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().axiomArmorSet().AXIOM_CHEST.get()
			))
			.addCriterion("axiom_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().axiomArmorSet().AXIOM_LEGS.get()
			))
			.addCriterion("axiom_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().axiomArmorSet().AXIOM_BOOTS.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
