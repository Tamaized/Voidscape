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
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class TerraformAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private AstralCrystalAdvancementSubProvider parent;

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
		return "terraform";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.toolSetComponentDirectory().astralToolSet().ASTRAL_SHOVEL.get(),
				title(),
				description(),
				null,
				AdvancementType.CHALLENGE,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("terraform", advancementTriggers.THREE_BY_THREE.get().createCriterion(
				new ItemMatchesAdvancementTrigger.Instance(
					Optional.empty(),
					new ItemStack(items.toolSetComponentDirectory().astralToolSet().ASTRAL_SHOVEL)
				)
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
