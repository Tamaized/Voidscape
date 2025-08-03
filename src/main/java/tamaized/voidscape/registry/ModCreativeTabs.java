package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;

import java.util.function.Supplier;

@tamaized.beanification.Component
public class ModCreativeTabs {

	@Autowired
	private ModBlockComponentDirectory blockComponentDirectory;

	@Autowired
	private ModItemComponentDirectory itemComponentDirectory;

	@Autowired
	private ModArmorSetComponentDirectory armorSetComponentDirectory;

	@Autowired
	private ModToolSetComponentDirectory toolSetComponentDirectory;

	private final DeferredRegister<CreativeModeTab> REGISTRY = RegUtil.create(Registries.CREATIVE_MODE_TAB);

	public final Supplier<CreativeModeTab> TAB = REGISTRY.register("tab", () -> CreativeModeTab.builder()
		.title(Component.translatable(Voidscape.MODID + ".item_group"))
		.icon(() -> new ItemStack(itemComponentDirectory.materialItems().VOIDIC_CRYSTAL.get()))
		.displayItems((parameters, output) -> {
			//// Blocks
			output.accept(blockComponentDirectory.oreBlocks().VOIDIC_CRYSTAL_ORE_ITEM.get());
			output.accept(blockComponentDirectory.materialBlocks().VOIDIC_CRYSTAL_BLOCK_ITEM.get());
			output.accept(blockComponentDirectory.functionalBlocks().VERY_DRIPPY_DRIPSTONE_ITEM.get());
			output.accept(blockComponentDirectory.materialBlocks().CHARRED_BRICK_ITEM.get());
			output.accept(blockComponentDirectory.oreBlocks().TITANITE_ORE_ITEM.get());
			output.accept(blockComponentDirectory.oreBlocks().FLESH_ORE_ITEM.get());
			output.accept(blockComponentDirectory.materialBlocks().FLESH_BLOCK_ITEM.get());
			output.accept(blockComponentDirectory.oreBlocks().STRANGE_ORE_ITEM.get());
			output.accept(blockComponentDirectory.spireBlocks().THUNDERROCK_ITEM.get());
			output.accept(blockComponentDirectory.spireBlocks().ANTIROCK_ITEM.get());
			output.accept(blockComponentDirectory.spireBlocks().ASTRALROCK_ITEM.get());
			output.accept(blockComponentDirectory.oreBlocks().CRACKED_ASTRALROCK_ITEM.get());
			output.accept(blockComponentDirectory.nullBiomeBlocks().NULL_BLACK_ITEM.get());
			output.accept(blockComponentDirectory.nullBiomeBlocks().NULL_WHITE_ITEM.get());
			// Machine
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_CORE_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_LIQUIFIER_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_DEFUSER_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_GERMINATOR_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_WELL_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_COOP_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_HATCHERY_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_INFUSER_ITEM.get());
			output.accept(blockComponentDirectory.machineBlocks().MACHINE_COLLECTOR_ITEM.get());
			// Biome - Thunder Forest
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_NYLIUM_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_ROOTS_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_WART_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_VINES_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_FUNGUS_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_STEM_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_HYPHAE_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_PLANKS_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_STAIRS_ITEM.get());
			output.accept(blockComponentDirectory.thunderForestBiomeBlocks().THUNDER_SLAB_ITEM.get());
			// Crops
			output.accept(blockComponentDirectory.etherealFruitBlocks().VOID.get());
			output.accept(blockComponentDirectory.etherealFruitBlocks().NULL.get());
			output.accept(blockComponentDirectory.etherealFruitBlocks().OVERWORLD.get());
			output.accept(blockComponentDirectory.etherealFruitBlocks().NETHER.get());
			output.accept(blockComponentDirectory.etherealFruitBlocks().END.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_VOID.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_NULL.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_OVERWORLD.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_NETHER.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_END.get());
			output.accept(itemComponentDirectory.etherealFruitItems().ETHEREAL_FRUIT_SALAD.get());
			//// Items
			output.accept(itemComponentDirectory.miscItems().ETHEREAL_SPIDER_EGGS.get());
			output.accept(itemComponentDirectory.augmentItems().ETHEREAL_SPIDER_FANG.get());
			output.accept(itemComponentDirectory.miscItems().ETHEREAL_ESSENCE.get());
			output.accept(itemComponentDirectory.materialItems().VOIDIC_CRYSTAL.get());
			output.accept(itemComponentDirectory.augmentItems().VOIDIC_TEMPLATE.get());
			// Gear - Voidic
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.get());
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_AXE.get());
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_PICKAXE.get());
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_SHIELD.get());
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_BOW.get());
			output.accept(toolSetComponentDirectory.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW.get());
			output.accept(armorSetComponentDirectory.voidicCrystalArmorSet().VOIDIC_CRYSTAL_HELMET.get());
			output.accept(armorSetComponentDirectory.voidicCrystalArmorSet().VOIDIC_CRYSTAL_CHEST.get());
			output.accept(armorSetComponentDirectory.voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get());
			output.accept(armorSetComponentDirectory.voidicCrystalArmorSet().VOIDIC_CRYSTAL_BOOTS.get());
			// Gear - Charred
			output.accept(itemComponentDirectory.materialItems().CHARRED_BONE.get());
			output.accept(itemComponentDirectory.partItems().CHARRED_WARHAMMER_HEAD.get());
			output.accept(toolSetComponentDirectory.charredToolSet().CHARRED_WARHAMMER.get());
			// Gear - Corrupt
			output.accept(itemComponentDirectory.materialItems().TENDRIL.get());
			output.accept(toolSetComponentDirectory.corruptToolSet().CORRUPT_SWORD.get());
			output.accept(toolSetComponentDirectory.corruptToolSet().CORRUPT_AXE.get());
			output.accept(toolSetComponentDirectory.corruptToolSet().CORRUPT_BOW.get());
			output.accept(toolSetComponentDirectory.corruptToolSet().CORRUPT_XBOW.get());
			output.accept(armorSetComponentDirectory.corruptArmorSet().CORRUPT_HELMET.get());
			output.accept(armorSetComponentDirectory.corruptArmorSet().CORRUPT_CHEST.get());
			output.accept(armorSetComponentDirectory.corruptArmorSet().CORRUPT_LEGS.get());
			output.accept(armorSetComponentDirectory.corruptArmorSet().CORRUPT_BOOTS.get());
			// Gear - Titanite
			output.accept(itemComponentDirectory.materialItems().TITANITE_CHUNK.get());
			output.accept(itemComponentDirectory.materialItems().TITANITE_SHARD.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_SWORD.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_AXE.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_PICKAXE.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_HOE.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_BOW.get());
			output.accept(toolSetComponentDirectory.titaniteToolSet().TITANITE_XBOW.get());
			output.accept(armorSetComponentDirectory.titaniteArmorSet().TITANITE_HELMET.get());
			output.accept(armorSetComponentDirectory.titaniteArmorSet().TITANITE_CHEST.get());
			output.accept(armorSetComponentDirectory.titaniteArmorSet().TITANITE_LEGS.get());
			output.accept(armorSetComponentDirectory.titaniteArmorSet().TITANITE_BOOTS.get());
			// Gear - Ichor
			output.accept(itemComponentDirectory.materialItems().FLESH_CHUNK.get());
			output.accept(itemComponentDirectory.materialItems().ICHOR.get());
			output.accept(itemComponentDirectory.materialItems().ICHOR_CRYSTAL.get());
			output.accept(toolSetComponentDirectory.spellTomeSet().ICHOR_TOME.get());
			output.accept(toolSetComponentDirectory.spellTomeSet().VOIDIC_TOME.get());
			output.accept(toolSetComponentDirectory.spellTomeSet().CORRUPT_TOME.get());
			output.accept(toolSetComponentDirectory.spellTomeSet().TITANITE_TOME.get());
			output.accept(toolSetComponentDirectory.ichorToolSet().ICHOR_SWORD.get());
			output.accept(toolSetComponentDirectory.ichorToolSet().ICHOR_AXE.get());
			output.accept(toolSetComponentDirectory.ichorToolSet().ICHOR_PICKAXE.get());
			output.accept(toolSetComponentDirectory.ichorToolSet().ICHOR_BOW.get());
			output.accept(toolSetComponentDirectory.ichorToolSet().ICHOR_XBOW.get());
			output.accept(armorSetComponentDirectory.ichorArmorSet().ICHOR_HELMET.get());
			output.accept(armorSetComponentDirectory.ichorArmorSet().ICHOR_CHEST.get());
			output.accept(armorSetComponentDirectory.ichorArmorSet().ICHOR_LEGS.get());
			output.accept(armorSetComponentDirectory.ichorArmorSet().ICHOR_BOOTS.get());
			// Gear - Astral
			output.accept(itemComponentDirectory.materialItems().STRANGE_PEARL.get());
			output.accept(itemComponentDirectory.materialItems().ASTRAL_SHARDS.get());
			output.accept(itemComponentDirectory.materialItems().ASTRAL_ESSENCE.get());
			output.accept(itemComponentDirectory.materialItems().ASTRAL_CRYSTAL.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_SWORD.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_AXE.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_PICKAXE.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_SHOVEL.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_BOW.get());
			output.accept(toolSetComponentDirectory.astralToolSet().ASTRAL_XBOW.get());
			output.accept(armorSetComponentDirectory.astralArmorSet().ASTRAL_HELMET.get());
			output.accept(armorSetComponentDirectory.astralArmorSet().ASTRAL_CHEST.get());
			output.accept(armorSetComponentDirectory.astralArmorSet().ASTRAL_LEGS.get());
			output.accept(armorSetComponentDirectory.astralArmorSet().ASTRAL_BOOTS.get());
		})
		.build());

}
