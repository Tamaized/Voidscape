package tamaized.voidscape.registry;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModTools implements RegistryClass {

	static class ItemTier {
		static final RegUtil.ItemTier VOIDIC_CRYSTAL = new RegUtil.ItemTier("voidic_crystal", 5, 2538, 9.5F, 5F, 17, () -> Ingredient.of(ModItems.VOIDIC_CRYSTAL.get()));
		static final RegUtil.ItemTier CORRUPT = new RegUtil.ItemTier("corrupt", 6, 3041, 10.0F, 6F, 19, () -> Ingredient.of(ModItems.VOIDIC_CRYSTAL.get()));
	}

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.
			sword(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOW = RegUtil.ToolAndArmorHelper.
			bow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SHIELD = RegUtil.ToolAndArmorHelper.
			shield(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.
			axe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.
			pickaxe(ItemTier.VOIDIC_CRYSTAL, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));

	public static final RegistryObject<Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.
			sword(ItemTier.CORRUPT, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.
			bow(ItemTier.CORRUPT, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(ItemTier.CORRUPT, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.
			axe(ItemTier.CORRUPT, ModItems.ItemProps.VOIDIC_CRYSTAL.properties().get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 3D)));

	@Override
	public void init(IEventBus bus) {

	}

}
