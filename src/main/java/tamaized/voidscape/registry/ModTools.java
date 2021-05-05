package tamaized.voidscape.registry;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModTools {

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.
			sword(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOW = RegUtil.ToolAndArmorHelper.
			bow(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SHIELD = RegUtil.ToolAndArmorHelper.
			shield(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.
			axe(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.
			pickaxe(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 1D)));

	public static final RegistryObject<Item> CORRUPT_SWORD = RegUtil.ToolAndArmorHelper.
			sword(RegUtil.ItemTier.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_BOW = RegUtil.ToolAndArmorHelper.
			bow(RegUtil.ItemTier.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_XBOW = RegUtil.ToolAndArmorHelper.
			xbow(RegUtil.ItemTier.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_ARROW_DMG, AttributeModifier.Operation.ADDITION, 2D)));
	public static final RegistryObject<Item> CORRUPT_AXE = RegUtil.ToolAndArmorHelper.
			axe(RegUtil.ItemTier.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_DMG, AttributeModifier.Operation.ADDITION, 2D)));

	static void classload() {

	}

}
