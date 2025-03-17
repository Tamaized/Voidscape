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
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class BucketAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private LiquidAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModFluidBuckets buckets;

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModEntities entities;

	@Override
	protected String name() {
		return "bucket";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				buckets.VOIDIC.get(),
				title(),
				description(),
				null,
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("bucket", InventoryChangeTrigger.TriggerInstance.hasItems(
				buckets.VOIDIC.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
