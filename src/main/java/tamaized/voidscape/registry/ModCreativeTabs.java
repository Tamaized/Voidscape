package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.block.ModBlocksThunderForestBiome;

public class ModCreativeTabs implements RegistryClass {

	private static final DeferredRegister<CreativeModeTab> REGISTRY = RegUtil.create(Registries.CREATIVE_MODE_TAB);

	public static final RegistryObject<CreativeModeTab> TAB = REGISTRY.register("tab", () -> CreativeModeTab.builder()
			.title(Component.translatable(Voidscape.MODID + ".item_group"))
			.icon(() -> new ItemStack(ModItems.VOIDIC_CRYSTAL.get()))
			.displayItems((parameters, output) -> {
				//// Blocks
				output.accept(ModBlocks.VOIDIC_CRYSTAL_ORE_ITEM.get());
				output.accept(ModBlocks.VOIDIC_CRYSTAL_BLOCK_ITEM.get());
				output.accept(ModBlocks.TITANITE_ORE_ITEM.get());
				output.accept(ModBlocks.FLESH_ORE_ITEM.get());
				output.accept(ModBlocks.FLESH_BLOCK_ITEM.get());
				output.accept(ModBlocks.STRANGE_ORE_ITEM.get());
				output.accept(ModBlocks.THUNDERROCK_ITEM.get());
				output.accept(ModBlocks.ANTIROCK_ITEM.get());
				output.accept(ModBlocks.ASTRALROCK_ITEM.get());
				output.accept(ModBlocks.CRACKED_ASTRALROCK_ITEM.get());
				output.accept(ModBlocks.NULL_BLACK_ITEM.get());
				output.accept(ModBlocks.NULL_WHITE_ITEM.get());
				// Machine
				output.accept(ModBlocks.MACHINE_CORE_ITEM.get());
				output.accept(ModBlocks.MACHINE_LIQUIFIER_ITEM.get());
				output.accept(ModBlocks.MACHINE_DEFUSER_ITEM.get());
				// Biome - Thunder Forest
				output.accept(ModBlocksThunderForestBiome.THUNDER_NYLIUM_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_ROOTS_ITEM.get());
                output.accept(ModBlocksThunderForestBiome.THUNDER_WART_ITEM.get());
                output.accept(ModBlocksThunderForestBiome.THUNDER_VINES_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_FUNGUS_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_STEM_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_HYPHAE_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_STEM_STRIPPED_ITEM.get());
				output.accept(ModBlocksThunderForestBiome.THUNDER_HYPHAE_STRIPPED_ITEM.get());
                output.accept(ModBlocksThunderForestBiome.THUNDER_PLANKS_ITEM.get());
                output.accept(ModBlocksThunderForestBiome.THUNDER_STAIRS_ITEM.get());
                output.accept(ModBlocksThunderForestBiome.THUNDER_SLAB_ITEM.get());
				// Crops
				output.accept(ModBlocks.ETHEREAL_FRUIT_VOID.get());
				output.accept(ModBlocks.ETHEREAL_FRUIT_NULL.get());
				output.accept(ModBlocks.ETHEREAL_FRUIT_OVERWORLD.get());
				output.accept(ModBlocks.ETHEREAL_FRUIT_NETHER.get());
				output.accept(ModBlocks.ETHEREAL_FRUIT_END.get());
				output.accept(ModItems.ETHEREAL_FRUIT_VOID.get());
				output.accept(ModItems.ETHEREAL_FRUIT_NULL.get());
				output.accept(ModItems.ETHEREAL_FRUIT_OVERWORLD.get());
				output.accept(ModItems.ETHEREAL_FRUIT_NETHER.get());
				output.accept(ModItems.ETHEREAL_FRUIT_END.get());
				//// Items
				output.accept(ModItems.ETHEREAL_ESSENCE.get());
				output.accept(ModItems.VOIDIC_CRYSTAL.get());
				output.accept(ModItems.VOIDIC_TEMPLATE.get());
				// Gear - Voidic
				output.accept(ModTools.VOIDIC_CRYSTAL_SWORD.get());
				output.accept(ModTools.VOIDIC_CRYSTAL_AXE.get());
				output.accept(ModTools.VOIDIC_CRYSTAL_PICKAXE.get());
				output.accept(ModTools.VOIDIC_CRYSTAL_SHIELD.get());
				output.accept(ModTools.VOIDIC_CRYSTAL_BOW.get());
				output.accept(ModTools.VOIDIC_CRYSTAL_XBOW.get());
				output.accept(ModArmors.VOIDIC_CRYSTAL_HELMET.get());
				output.accept(ModArmors.VOIDIC_CRYSTAL_CHEST.get());
				output.accept(ModArmors.VOIDIC_CRYSTAL_LEGS.get());
				output.accept(ModArmors.VOIDIC_CRYSTAL_BOOTS.get());
				// Gear - Charred
				output.accept(ModItems.CHARRED_BONE.get());
				output.accept(ModItems.CHARRED_WARHAMMER_HEAD.get());
				output.accept(ModTools.CHARRED_WARHAMMER.get());
				// Gear - Corrupt
				output.accept(ModItems.TENDRIL.get());
				output.accept(ModTools.CORRUPT_SWORD.get());
				output.accept(ModTools.CORRUPT_AXE.get());
				output.accept(ModTools.CORRUPT_BOW.get());
				output.accept(ModTools.CORRUPT_XBOW.get());
				output.accept(ModArmors.CORRUPT_HELMET.get());
				output.accept(ModArmors.CORRUPT_CHEST.get());
				output.accept(ModArmors.CORRUPT_LEGS.get());
				output.accept(ModArmors.CORRUPT_BOOTS.get());
				// Gear - Titanite
				output.accept(ModItems.TITANITE_CHUNK.get());
				output.accept(ModItems.TITANITE_SHARD.get());
				output.accept(ModTools.TITANITE_SWORD.get());
				output.accept(ModTools.TITANITE_AXE.get());
				output.accept(ModTools.TITANITE_PICKAXE.get());
				output.accept(ModTools.TITANITE_HOE.get());
				output.accept(ModTools.TITANITE_BOW.get());
				output.accept(ModTools.TITANITE_XBOW.get());
				output.accept(ModArmors.TITANITE_HELMET.get());
				output.accept(ModArmors.TITANITE_CHEST.get());
				output.accept(ModArmors.TITANITE_LEGS.get());
				output.accept(ModArmors.TITANITE_BOOTS.get());
				// Gear - Ichor
				output.accept(ModItems.FLESH_CHUNK.get());
				output.accept(ModItems.ICHOR.get());
				output.accept(ModItems.ICHOR_CRYSTAL.get());
				output.accept(ModTools.ICHOR_TOME.get());
				output.accept(ModTools.VOIDIC_TOME.get());
				output.accept(ModTools.CORRUPT_TOME.get());
				output.accept(ModTools.TITANITE_TOME.get());
				output.accept(ModTools.ICHOR_SWORD.get());
				output.accept(ModTools.ICHOR_AXE.get());
				output.accept(ModTools.ICHOR_PICKAXE.get());
				output.accept(ModTools.ICHOR_BOW.get());
				output.accept(ModTools.ICHOR_XBOW.get());
				output.accept(ModArmors.ICHOR_HELMET.get());
				output.accept(ModArmors.ICHOR_CHEST.get());
				output.accept(ModArmors.ICHOR_LEGS.get());
				output.accept(ModArmors.ICHOR_BOOTS.get());
				// Gear - Astral
				output.accept(ModItems.STRANGE_PEARL.get());
				output.accept(ModItems.ASTRAL_SHARDS.get());
				output.accept(ModItems.ASTRAL_ESSENCE.get());
				output.accept(ModItems.ASTRAL_CRYSTAL.get());
				output.accept(ModTools.ASTRAL_SWORD.get());
				output.accept(ModTools.ASTRAL_AXE.get());
				output.accept(ModTools.ASTRAL_PICKAXE.get());
				output.accept(ModTools.ASTRAL_SHOVEL.get());
				output.accept(ModTools.ASTRAL_BOW.get());
				output.accept(ModTools.ASTRAL_XBOW.get());
				output.accept(ModArmors.ASTRAL_HELMET.get());
				output.accept(ModArmors.ASTRAL_CHEST.get());
				output.accept(ModArmors.ASTRAL_LEGS.get());
				output.accept(ModArmors.ASTRAL_BOOTS.get());
			})
			.build());

	@Override
	public void init(IEventBus bus) {

	}
}
