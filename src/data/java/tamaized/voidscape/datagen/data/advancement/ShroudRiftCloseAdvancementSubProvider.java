package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.advancement.GenericAdvancementTrigger;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ShroudRiftCloseAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private AstralCrystalAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "shroud_rift_close";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
			.display(
				items.materialItems().SHROUD_THREAD.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("shroud_rift_close", advancementTriggers.SHROUD_RIFT_CLOSE_TRIGGER.get().createCriterion(
				new GenericAdvancementTrigger.Instance(Optional.empty())
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
