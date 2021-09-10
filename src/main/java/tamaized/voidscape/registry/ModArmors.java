package tamaized.voidscape.registry;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

public class ModArmors {

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_CHEST = RegUtil.ToolAndArmorHelper.
			chest(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_LEGS = RegUtil.ToolAndArmorHelper.
			legs(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 1D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.05D)));

	public static final RegistryObject<Item> CORRUPT_HELMET = RegUtil.ToolAndArmorHelper.
			helmet(RegUtil.ArmorMaterial.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_VISIBILITY, AttributeModifier.Operation.MULTIPLY_BASE, 0.15D)));
	public static final RegistryObject<Item> CORRUPT_CHEST = RegUtil.ToolAndArmorHelper.
			chest(RegUtil.ArmorMaterial.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D)));
	public static final RegistryObject<Item> CORRUPT_LEGS = RegUtil.ToolAndArmorHelper.
			legs(RegUtil.ArmorMaterial.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D)));
	public static final RegistryObject<Item> CORRUPT_BOOTS = RegUtil.ToolAndArmorHelper.
			boots(RegUtil.ArmorMaterial.CORRUPT, RegUtil.ItemProps.VOIDIC_CRYSTAL.get(), RegUtil.makeAttributeFactory(RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_RES, AttributeModifier.Operation.ADDITION, 2D), RegUtil.
					AttributeData.make(ModAttributes.VOIDIC_INFUSION_RES, AttributeModifier.Operation.MULTIPLY_BASE, 0.1D)));

	static void classload() {

	}

}
