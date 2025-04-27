package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class VoidmancerAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private IchorAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModDimensions dimensions;

	@Override
	protected String name() {
		return "voidmancer";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.toolSetComponentDirectory().spellTomeSet().VOIDIC_TOME.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.AND)
			.addCriterion("voidic_tome", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().spellTomeSet().VOIDIC_TOME.get()
			))
			.addCriterion("corrupt_tome", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().spellTomeSet().CORRUPT_TOME.get()
			))
			.addCriterion("titanite_tome", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().spellTomeSet().TITANITE_TOME.get()
			))
			.addCriterion("ichor_tome", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().spellTomeSet().ICHOR_TOME.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
