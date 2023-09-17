package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.EtherealPlantBlock;

public class ModCreativeTabs implements RegistryClass {

	private static final DeferredRegister<CreativeModeTab> REGISTRY = RegUtil.create(Registries.CREATIVE_MODE_TAB);

	public static final RegistryObject<CreativeModeTab> TAB = REGISTRY.register("tab", () -> CreativeModeTab.builder()
			.title(Component.translatable(Voidscape.MODID + ".item_group"))
			.icon(() -> new ItemStack(ModItems.VOIDIC_CRYSTAL.get()))
			.displayItems((parameters, output) -> {
				//// Blocks
				output.accept(ModBlocks.VOIDIC_CRYSTAL_ORE_ITEM.get());
				output.accept(ModBlocks.VOIDIC_CRYSTAL_BLOCK_ITEM.get());
				output.accept(ModBlocks.THUNDERROCK_ITEM.get());
				output.accept(ModBlocks.ANTIROCK_ITEM.get());
				output.accept(ModBlocks.NULL_BLACK_ITEM.get());
				output.accept(ModBlocks.NULL_WHITE_ITEM.get());
				// Machine
				output.accept(ModBlocks.MACHINE_CORE_ITEM.get());
				output.accept(ModBlocks.MACHINE_LIQUIFIER_ITEM.get());
				output.accept(ModBlocks.MACHINE_DEFUSER_ITEM.get());
				// Biome - Thunder Forest
				output.accept(ModBlocks.THUNDER_NYLIUM_ITEM.get());
				output.accept(ModBlocks.THUNDER_ROOTS_ITEM.get());
				output.accept(ModBlocks.THUNDER_FUNGUS_ITEM.get());
				output.accept(ModBlocks.THUNDER_STEM_ITEM.get());
				output.accept(ModBlocks.THUNDER_HYPHAE_ITEM.get());
				output.accept(ModBlocks.THUNDER_PLANKS_ITEM.get());
				output.accept(ModBlocks.THUNDER_WART_ITEM.get());
				output.accept(ModBlocks.THUNDER_VINES_ITEM.get());
				// Crops
				output.accept(ModBlocks.PLANT_ITEM.get());
				output.accept(makePlant(EtherealPlantBlock.State.NULL));
				output.accept(makePlant(EtherealPlantBlock.State.OVERWORLD));
				output.accept(makePlant(EtherealPlantBlock.State.NETHER));
				output.accept(makePlant(EtherealPlantBlock.State.END));
				output.accept(ModItems.FRUIT.get());
				output.accept(makeFruit(EtherealPlantBlock.State.NULL));
				output.accept(makeFruit(EtherealPlantBlock.State.OVERWORLD));
				output.accept(makeFruit(EtherealPlantBlock.State.NETHER));
				output.accept(makeFruit(EtherealPlantBlock.State.END));
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
			})
			.build());

	private static ItemStack makePlant(EtherealPlantBlock.State state) {
		CompoundTag tag = new CompoundTag();
		CompoundTag stateTag = new CompoundTag();
		stateTag.putString("state", state.getSerializedName());
		tag.put(BlockItem.BLOCK_STATE_TAG, stateTag);
		ItemStack stack = new ItemStack(ModBlocks.PLANT.get());
		stack.setTag(tag);
		return stack;
	}

	private static ItemStack makeFruit(EtherealPlantBlock.State state) {
		CompoundTag tag = new CompoundTag();
		CompoundTag t = new CompoundTag();
		t.putString("augment", state.getSerializedName());
		tag.put(Voidscape.MODID, t);
		ItemStack stack = new ItemStack(ModItems.FRUIT.get());
		stack.setTag(tag);
		return stack;
	}

	@Override
	public void init(IEventBus bus) {

	}
}
