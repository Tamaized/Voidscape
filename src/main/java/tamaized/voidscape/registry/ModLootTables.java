package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ModLootTables {

	public final ResourceKey<LootTable> CHEST_STRUCTURE_CHARRED_OUTPOST = key("chests/structures/charred");

	private ResourceKey<LootTable> key(String value) {
		return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Voidscape.MODID, value));
	}

}
