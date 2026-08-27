package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModItemTags {

	public final TagKey<Item> THUNDER_STEMS = make("thunder_stems");

	public final TagKey<Item> VOIDIC_CRYSTAL_TOOLS = make("tools/voidic_crystal");
	public final TagKey<Item> CHARRED_TOOLS = make("tools/charred");
	public final TagKey<Item> CORRUPT_TOOLS = make("tools/corrupt");
	public final TagKey<Item> TITANITE_TOOLS = make("tools/titanite");
	public final TagKey<Item> ICHOR_TOOLS = make("tools/ichor");
	public final TagKey<Item> ASTRAL_TOOLS = make("tools/astral");

	public final TagKey<Item> REPAIR_MATERIAL_VOIDIC_CRYSTAL = make("voidic_crystal");
	public final TagKey<Item> REPAIR_MATERIAL_CHARRED = make("charred");
	public final TagKey<Item> REPAIR_MATERIAL_CORRUPT = make("corrupt");
	public final TagKey<Item> REPAIR_MATERIAL_TITANITE = make("titanite");
	public final TagKey<Item> REPAIR_MATERIAL_ICHOR = make("ichor");
	public final TagKey<Item> REPAIR_MATERIAL_ASTRAL = make("astral");

	private TagKey<Item> make(String key) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, key));
	}

}
