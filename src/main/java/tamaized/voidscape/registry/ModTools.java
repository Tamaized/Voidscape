package tamaized.voidscape.registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModTools {

	public static final RegistryObject<Item> VOIDIC_CRYSTAL_SWORD = RegUtil.ToolAndArmorHelper.sword(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_AXE = RegUtil.ToolAndArmorHelper.axe(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_PICKAXE = RegUtil.ToolAndArmorHelper.pickaxe(RegUtil.ItemTier.VOIDIC_CRYSTAL, RegUtil.ItemProps.VOIDIC_CRYSTAL.get());

	static void classload() {

	}

}
