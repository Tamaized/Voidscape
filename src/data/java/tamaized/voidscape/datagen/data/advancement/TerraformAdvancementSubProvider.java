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
public class TerraformAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private AstralCrystalAdvancementSubProvider parent;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected String name() {
		return "terraform";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver))
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
