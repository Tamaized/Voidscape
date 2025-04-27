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
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.function.Consumer;

@Component
public class GearAdvancementSubProvider extends AbstractAdvancementSubProvider {

	@Autowired
	private RootAdvancementSubProvider parent;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModDimensions dimensions;

	@Override
	protected String name() {
		return "gear";
	}

	@Override
	public AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return Advancement.Builder.advancement()
			.parent(parent.getOrMake(registries, saver, existingFileHelper))
			.display(
				items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get(),
				title(),
				description(),
				null,
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.requirements(AdvancementRequirements.Strategy.OR)
			.addCriterion("voidic_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.get()
			))
			.addCriterion("voidic_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get()
			))
			.addCriterion("voidic_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get()
			))
			.addCriterion("voidic_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.get()
			))
			.addCriterion("corrupt_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().corruptArmorSet().CORRUPT_HELMET.get()
			))
			.addCriterion("corrupt_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().corruptArmorSet().CORRUPT_CHEST.get()
			))
			.addCriterion("corrupt_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().corruptArmorSet().CORRUPT_LEGS.get()
			))
			.addCriterion("corrupt_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().corruptArmorSet().CORRUPT_BOOTS.get()
			))
			.addCriterion("titanite_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().titaniteArmorSet().TITANITE_HELMET.get()
			))
			.addCriterion("titanite_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().titaniteArmorSet().TITANITE_CHEST.get()
			))
			.addCriterion("titanite_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().titaniteArmorSet().TITANITE_LEGS.get()
			))
			.addCriterion("titanite_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().titaniteArmorSet().TITANITE_BOOTS.get()
			))
			.addCriterion("ichor_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().ichorArmorSet().ICHOR_HELMET.get()
			))
			.addCriterion("ichor_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().ichorArmorSet().ICHOR_CHEST.get()
			))
			.addCriterion("ichor_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().ichorArmorSet().ICHOR_LEGS.get()
			))
			.addCriterion("ichor_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().ichorArmorSet().ICHOR_BOOTS.get()
			))
			.addCriterion("astral_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_HELMET.get()
			))
			.addCriterion("astral_chest", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_CHEST.get()
			))
			.addCriterion("astral_legs", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_LEGS.get()
			))
			.addCriterion("astral_boots", InventoryChangeTrigger.TriggerInstance.hasItems(
				items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_BOOTS.get()
			))
			.sendsTelemetryEvent()
			.save(saver, location());
	}

}
