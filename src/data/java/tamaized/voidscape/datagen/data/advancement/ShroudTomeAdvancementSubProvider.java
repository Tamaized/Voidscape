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
public class ShroudTomeAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private ShroudRiftCloseAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "shroud_tome";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.toolSetComponentDirectory().spellTomeSet().SHROUD_TOME.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.AND)
			.addCriterion("shroud_tome", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.toolSetComponentDirectory().spellTomeSet().SHROUD_TOME.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
