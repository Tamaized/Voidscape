package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.advancement.ItemMatchesAdvancementTrigger;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class AstralCrystalAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private AstralAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModEntities entities;

	@Override
	protected String name() {
		return "astral_crystal";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.materialItems().ASTRAL_CRYSTAL.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("astral_crystal", advancementTriggers.ITEM_USED_ON_NULL_SERVANT_TRIGGER.get().createCriterion(
				new ItemMatchesAdvancementTrigger.Instance(
					Optional.empty(),
					new ItemStack(items.materialItems().ASTRAL_ESSENCE)
				)
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
