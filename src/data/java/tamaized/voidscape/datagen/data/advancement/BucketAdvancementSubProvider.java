package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;

import java.util.function.Consumer;

@Component
public class BucketAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private LiquidAdvancementSubProvider parent;

	@Autowired
	private ModFluidBuckets buckets;

	@Override
	protected String name() {
		return "bucket";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
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
