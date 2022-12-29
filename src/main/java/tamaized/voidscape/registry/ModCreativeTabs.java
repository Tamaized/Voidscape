package tamaized.voidscape.registry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.BlockEtherealPlant;

import java.util.function.Consumer;

public class ModCreativeTabs implements RegistryClass {

	@Override
	public void init(IEventBus bus) {
		bus.addListener((Consumer<CreativeModeTabEvent.Register>) event -> {
			event.registerCreativeModeTab(new ResourceLocation(Voidscape.MODID, "tab"), builder -> builder
					.title(Component.translatable(Voidscape.MODID + ".item_group"))
					.icon(() -> new ItemStack(ModItems.VOIDIC_CRYSTAL.get()))
					.displayItems((featureFlag, output, operator) -> {
						output.accept(ModBlocks.VOIDIC_CRYSTAL_ORE_ITEM.get());
						output.accept(ModBlocks.VOIDIC_CRYSTAL_BLOCK_ITEM.get());
						output.accept(ModBlocks.THUNDERROCK_ITEM.get());
						output.accept(ModBlocks.ANTIROCK_ITEM.get());
						output.accept(ModBlocks.EXIT_PORTAL_ITEM.get());
						output.accept(ModBlocks.NULL_BLACK_ITEM.get());
						output.accept(ModBlocks.NULL_WHITE_ITEM.get());
						output.accept(ModBlocks.PLANT_ITEM.get());
						output.accept(makePlant(BlockEtherealPlant.State.NULL));
						output.accept(makePlant(BlockEtherealPlant.State.OVERWORLD));
						output.accept(makePlant(BlockEtherealPlant.State.NETHER));
						output.accept(makePlant(BlockEtherealPlant.State.END));
						output.accept(ModItems.FRUIT.get());
						output.accept(makeFruit(BlockEtherealPlant.State.NULL));
						output.accept(makeFruit(BlockEtherealPlant.State.OVERWORLD));
						output.accept(makeFruit(BlockEtherealPlant.State.NETHER));
						output.accept(makeFruit(BlockEtherealPlant.State.END));
						output.accept(ModItems.ETHEREAL_ESSENCE.get());
						output.accept(ModItems.VOIDIC_CRYSTAL.get());
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
						output.accept(ModItems.CHARRED_BONE.get());
						output.accept(ModItems.CHARRED_WARHAMMER_HEAD.get());
						output.accept(ModTools.CHARRED_WARHAMMER.get());
						output.accept(ModItems.TENDRIL.get());
						output.accept(ModTools.CORRUPT_SWORD.get());
						output.accept(ModTools.CORRUPT_AXE.get());
						output.accept(ModTools.CORRUPT_BOW.get());
						output.accept(ModTools.CORRUPT_XBOW.get());
						output.accept(ModArmors.CORRUPT_HELMET.get());
						output.accept(ModArmors.CORRUPT_CHEST.get());
						output.accept(ModArmors.CORRUPT_LEGS.get());
						output.accept(ModArmors.CORRUPT_BOOTS.get());
					}));
		});
	}

	private ItemStack makePlant(BlockEtherealPlant.State state) {
		CompoundTag tag = new CompoundTag();
		CompoundTag stateTag = new CompoundTag();
		stateTag.putString("state", state.getSerializedName());
		tag.put(BlockItem.BLOCK_STATE_TAG, stateTag);
		ItemStack stack = new ItemStack(ModBlocks.PLANT.get());
		stack.setTag(tag);
		return stack;
	}

	private ItemStack makeFruit(BlockEtherealPlant.State state) {
		CompoundTag tag = new CompoundTag();
		CompoundTag t = new CompoundTag();
		t.putString("augment", state.getSerializedName());
		tag.put(Voidscape.MODID, t);
		ItemStack stack = new ItemStack(ModItems.FRUIT.get());
		stack.setTag(tag);
		return stack;
	}

}
