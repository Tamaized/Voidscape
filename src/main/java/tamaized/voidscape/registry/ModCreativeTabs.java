package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.armor.set.*;
import tamaized.voidscape.registry.block.*;
import tamaized.voidscape.registry.item.*;

import java.util.function.Supplier;

@tamaized.beanification.Component
public class ModCreativeTabs {

	// Blocks
	@Autowired
	private EtherealFruitBlocks etherealFruitBlocks;

	@Autowired
	private FunctionalBlocks functionalBlocks;

	@Autowired
	private MachineBlocks machineBlocks;

	@Autowired
	private MaterialBlocks materialBlocks;

	@Autowired
	private NullBiomeBlocks nullBiomeBlocks;

	@Autowired
	private OreBlocks oreBlocks;

	@Autowired
	private SpireBlocks spireBlocks;

	@Autowired
	private ThunderForestBiomeBlocks thunderForestBiomeBlocks;

	// Items
	@Autowired
	private AugmentItems augmentItems;

	@Autowired
	private EtherealFruitItems etherealFruitItems;

	@Autowired
	private MaterialItems materialItems;

	@Autowired
	private MiscItems miscItems;

	@Autowired
	private PartItems partItems;

	// Armor Sets
	@Autowired
	private VoidicCrystalArmorSet voidicCrystalArmorSet;

	@Autowired
	private CorruptArmorSet corruptArmorSet;

	@Autowired
	private TitaniteArmorSet titaniteArmorSet;

	@Autowired
	private IchorArmorSet ichorArmorSet;

	@Autowired
	private AstralArmorSet astralArmorSet;

	private final DeferredRegister<CreativeModeTab> REGISTRY = RegUtil.create(Registries.CREATIVE_MODE_TAB);

	public final Supplier<CreativeModeTab> TAB = REGISTRY.register("tab", () -> CreativeModeTab.builder()
		.title(Component.translatable(Voidscape.MODID + ".item_group"))
		.icon(() -> new ItemStack(materialItems.VOIDIC_CRYSTAL.get()))
		.displayItems((parameters, output) -> {
			//// Blocks
			output.accept(oreBlocks.VOIDIC_CRYSTAL_ORE_ITEM.get());
			output.accept(materialBlocks.VOIDIC_CRYSTAL_BLOCK_ITEM.get());
			output.accept(functionalBlocks.VERY_DRIPPY_DRIPSTONE_ITEM.get());
			output.accept(materialBlocks.CHARRED_BRICK_ITEM.get());
			output.accept(oreBlocks.TITANITE_ORE_ITEM.get());
			output.accept(oreBlocks.FLESH_ORE_ITEM.get());
			output.accept(materialBlocks.FLESH_BLOCK_ITEM.get());
			output.accept(oreBlocks.STRANGE_ORE_ITEM.get());
			output.accept(spireBlocks.THUNDERROCK_ITEM.get());
			output.accept(spireBlocks.ANTIROCK_ITEM.get());
			output.accept(spireBlocks.ASTRALROCK_ITEM.get());
			output.accept(oreBlocks.CRACKED_ASTRALROCK_ITEM.get());
			output.accept(nullBiomeBlocks.NULL_BLACK_ITEM.get());
			output.accept(nullBiomeBlocks.NULL_WHITE_ITEM.get());
			// Machine
			output.accept(machineBlocks.MACHINE_CORE_ITEM.get());
			output.accept(machineBlocks.MACHINE_LIQUIFIER_ITEM.get());
			output.accept(machineBlocks.MACHINE_DEFUSER_ITEM.get());
			output.accept(machineBlocks.MACHINE_GERMINATOR_ITEM.get());
			output.accept(machineBlocks.MACHINE_WELL_ITEM.get());
			output.accept(machineBlocks.MACHINE_COOP_ITEM.get());
			output.accept(machineBlocks.MACHINE_HATCHERY_ITEM.get());
			output.accept(machineBlocks.MACHINE_INFUSER_ITEM.get());
			output.accept(machineBlocks.MACHINE_COLLECTOR_ITEM.get());
			// Biome - Thunder Forest
			output.accept(thunderForestBiomeBlocks.THUNDER_NYLIUM_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_ROOTS_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_WART_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_VINES_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_FUNGUS_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_STEM_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_HYPHAE_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_STEM_STRIPPED_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_HYPHAE_STRIPPED_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_PLANKS_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_STAIRS_ITEM.get());
			output.accept(thunderForestBiomeBlocks.THUNDER_SLAB_ITEM.get());
			// Crops
			output.accept(etherealFruitBlocks.VOID.get());
			output.accept(etherealFruitBlocks.NULL.get());
			output.accept(etherealFruitBlocks.OVERWORLD.get());
			output.accept(etherealFruitBlocks.NETHER.get());
			output.accept(etherealFruitBlocks.END.get());
			output.accept(etherealFruitItems.ETHEREAL_FRUIT_VOID.get());
			output.accept(etherealFruitItems.ETHEREAL_FRUIT_NULL.get());
			output.accept(etherealFruitItems.ETHEREAL_FRUIT_OVERWORLD.get());
			output.accept(etherealFruitItems.ETHEREAL_FRUIT_NETHER.get());
			output.accept(etherealFruitItems.ETHEREAL_FRUIT_END.get());
			//// Items
			output.accept(miscItems.ETHEREAL_SPIDER_EGGS.get());
			output.accept(augmentItems.ETHEREAL_SPIDER_FANG.get());
			output.accept(miscItems.ETHEREAL_ESSENCE.get());
			output.accept(materialItems.VOIDIC_CRYSTAL.get());
			output.accept(augmentItems.VOIDIC_TEMPLATE.get());
			// Gear - Voidic
			output.accept(ModTools.VOIDIC_CRYSTAL_SWORD.get());
			output.accept(ModTools.VOIDIC_CRYSTAL_AXE.get());
			output.accept(ModTools.VOIDIC_CRYSTAL_PICKAXE.get());
			output.accept(ModTools.VOIDIC_CRYSTAL_SHIELD.get());
			output.accept(ModTools.VOIDIC_CRYSTAL_BOW.get());
			output.accept(ModTools.VOIDIC_CRYSTAL_XBOW.get());
			output.accept(voidicCrystalArmorSet.VOIDIC_CRYSTAL_HELMET.get());
			output.accept(voidicCrystalArmorSet.VOIDIC_CRYSTAL_CHEST.get());
			output.accept(voidicCrystalArmorSet.VOIDIC_CRYSTAL_LEGS.get());
			output.accept(voidicCrystalArmorSet.VOIDIC_CRYSTAL_BOOTS.get());
			// Gear - Charred
			output.accept(materialItems.CHARRED_BONE.get());
			output.accept(partItems.CHARRED_WARHAMMER_HEAD.get());
			output.accept(ModTools.CHARRED_WARHAMMER.get());
			// Gear - Corrupt
			output.accept(materialItems.TENDRIL.get());
			output.accept(ModTools.CORRUPT_SWORD.get());
			output.accept(ModTools.CORRUPT_AXE.get());
			output.accept(ModTools.CORRUPT_BOW.get());
			output.accept(ModTools.CORRUPT_XBOW.get());
			output.accept(corruptArmorSet.CORRUPT_HELMET.get());
			output.accept(corruptArmorSet.CORRUPT_CHEST.get());
			output.accept(corruptArmorSet.CORRUPT_LEGS.get());
			output.accept(corruptArmorSet.CORRUPT_BOOTS.get());
			// Gear - Titanite
			output.accept(materialItems.TITANITE_CHUNK.get());
			output.accept(materialItems.TITANITE_SHARD.get());
			output.accept(ModTools.TITANITE_SWORD.get());
			output.accept(ModTools.TITANITE_AXE.get());
			output.accept(ModTools.TITANITE_PICKAXE.get());
			output.accept(ModTools.TITANITE_HOE.get());
			output.accept(ModTools.TITANITE_BOW.get());
			output.accept(ModTools.TITANITE_XBOW.get());
			output.accept(titaniteArmorSet.TITANITE_HELMET.get());
			output.accept(titaniteArmorSet.TITANITE_CHEST.get());
			output.accept(titaniteArmorSet.TITANITE_LEGS.get());
			output.accept(titaniteArmorSet.TITANITE_BOOTS.get());
			// Gear - Ichor
			output.accept(materialItems.FLESH_CHUNK.get());
			output.accept(materialItems.ICHOR.get());
			output.accept(materialItems.ICHOR_CRYSTAL.get());
			output.accept(ModTools.ICHOR_TOME.get());
			output.accept(ModTools.VOIDIC_TOME.get());
			output.accept(ModTools.CORRUPT_TOME.get());
			output.accept(ModTools.TITANITE_TOME.get());
			output.accept(ModTools.ICHOR_SWORD.get());
			output.accept(ModTools.ICHOR_AXE.get());
			output.accept(ModTools.ICHOR_PICKAXE.get());
			output.accept(ModTools.ICHOR_BOW.get());
			output.accept(ModTools.ICHOR_XBOW.get());
			output.accept(ichorArmorSet.ICHOR_HELMET.get());
			output.accept(ichorArmorSet.ICHOR_CHEST.get());
			output.accept(ichorArmorSet.ICHOR_LEGS.get());
			output.accept(ichorArmorSet.ICHOR_BOOTS.get());
			// Gear - Astral
			output.accept(materialItems.STRANGE_PEARL.get());
			output.accept(materialItems.ASTRAL_SHARDS.get());
			output.accept(materialItems.ASTRAL_ESSENCE.get());
			output.accept(materialItems.ASTRAL_CRYSTAL.get());
			output.accept(ModTools.ASTRAL_SWORD.get());
			output.accept(ModTools.ASTRAL_AXE.get());
			output.accept(ModTools.ASTRAL_PICKAXE.get());
			output.accept(ModTools.ASTRAL_SHOVEL.get());
			output.accept(ModTools.ASTRAL_BOW.get());
			output.accept(ModTools.ASTRAL_XBOW.get());
			output.accept(astralArmorSet.ASTRAL_HELMET.get());
			output.accept(astralArmorSet.ASTRAL_CHEST.get());
			output.accept(astralArmorSet.ASTRAL_LEGS.get());
			output.accept(astralArmorSet.ASTRAL_BOOTS.get());
		})
		.build());

}
