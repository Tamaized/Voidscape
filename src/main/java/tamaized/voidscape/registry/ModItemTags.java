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

	private TagKey<Item> make(String key) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, key));
	}

}
