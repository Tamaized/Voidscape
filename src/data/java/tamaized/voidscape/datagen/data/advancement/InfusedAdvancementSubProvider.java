package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.advancement.GenericAdvancementTrigger;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class InfusedAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private RootAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModEntities entities;

	@Override
	protected String name() {
		return "infused";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.etherealFruitItems().ETHEREAL_FRUIT_VOID.get(),
				title(),
				description(),
				null,
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("infused", advancementTriggers.INFUSED_TRIGGER.get().createCriterion(
				new GenericAdvancementTrigger.Instance(Optional.empty())
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
