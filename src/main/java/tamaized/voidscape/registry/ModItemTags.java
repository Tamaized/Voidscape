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

	private TagKey<Item> make(String key) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, key));
	}

}
