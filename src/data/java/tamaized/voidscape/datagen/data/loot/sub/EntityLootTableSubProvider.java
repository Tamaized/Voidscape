package tamaized.voidscape.datagen.data.loot.sub;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.datagen.data.loot.sub.entity.IEntityLootTable;

import java.util.List;
import java.util.stream.Stream;

@Configurable
public class EntityLootTableSubProvider extends EntityLootSubProvider {

	@Directory(IEntityLootTable.class)
	private List<IEntityLootTable> lootTables;

	@Autowired
	private RegistryProvider registryProvider;

	public EntityLootTableSubProvider(HolderLookup.Provider registries) {
		super(FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		getKnownEntityTypes().filter(this::canHaveLootTable).forEach(e -> add(e, LootTable.lootTable()));
		lootTables.forEach(table -> table.add(this, this::add));
	}

	public HolderLookup.Provider registries() {
		return this.registries;
	}

	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return registryProvider.filterStreamForModFrom(registries, Registries.ENTITY_TYPE);
	}
}
