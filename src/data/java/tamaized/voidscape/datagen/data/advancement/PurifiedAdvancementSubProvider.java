package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.advancement.ItemMatchesAdvancementTrigger;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class PurifiedAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private PsychosisAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "purified";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.materialItems().TITANITE_SHARD.get(),
				title(),
				description(),
				null,
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("purified", advancementTriggers.ITEM_USED_ON_NULL_SERVANT_TRIGGER.get().createCriterion(
				new ItemMatchesAdvancementTrigger.Instance(Optional.empty(), new ItemStack(items.materialItems().TITANITE_CHUNK))
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
