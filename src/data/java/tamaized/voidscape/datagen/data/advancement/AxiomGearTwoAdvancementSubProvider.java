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
public class AxiomGearTwoAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private ShroudRiftCloseAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "axiom_rift_close_two";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.materialItems().AXIOMATIC_GEAR_RIGHT.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("gear", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.materialItems().AXIOMATIC_GEAR_RIGHT.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
