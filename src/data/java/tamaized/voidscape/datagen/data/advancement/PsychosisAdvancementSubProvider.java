package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class PsychosisAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private GearAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModEntities entities;

	@Override
	protected String name() {
		return "psychosis";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.materialItems().TENDRIL.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("pawn", KilledTrigger.TriggerInstance.playerKilledEntity(
				EntityPredicate.Builder.entity()
					.entityType(EntityTypePredicate.of(entities.CORRUPTED_PAWN.get()))
					.located(LocationPredicate.Builder.location().setDimension(dimensions.VOID))
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
