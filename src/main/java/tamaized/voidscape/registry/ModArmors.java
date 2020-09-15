package tamaized.voidscape.registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModArmors {

	static void classload() {

	}

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_HELMET = RegUtil.ToolAndArmorHelper.helmet(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_CHEST = RegUtil.ToolAndArmorHelper.chest(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_LEGS = RegUtil.ToolAndArmorHelper.legs(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BOOTS = RegUtil.ToolAndArmorHelper.boots(RegUtil.ArmorMaterial.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
}
