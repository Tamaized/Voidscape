package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class RootAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModDimensions dimensions;

	@Override
	protected String name() {
		return "root";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.display(
				items.materialItems().VOIDIC_CRYSTAL.get(),
				title(),
				description(),
				ResourceLocation.withDefaultNamespace("textures/block/bedrock.png"),
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("in_void", PlayerTrigger.TriggerInstance.located(
				LocationPredicate.Builder.location().setDimension(dimensions.VOID)
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
