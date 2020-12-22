package tamaized.voidscape.registry;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

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

	static void classload() {

	}

}
