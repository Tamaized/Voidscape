package tamaized.voidscape.datagen.data.loot;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.datagen.data.loot.sub.BlockLootTableSubProvider;
import tamaized.voidscape.datagen.data.loot.sub.ChestLootTableSubProvider;
import tamaized.voidscape.datagen.data.loot.sub.EntityLootTableSubProvider;

import java.util.List;
import java.util.Set;

@Component
public class LootTableProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	public LootTableProvider make(GatherDataEvent event) {
		return new LootTableProvider(
			event.getGenerator().getPackOutput(),
			Set.of(),
			List.of(
				new LootTableProvider.SubProviderEntry(BlockLootTableSubProvider::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(ChestLootTableSubProvider::new, LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(EntityLootTableSubProvider::new, LootContextParamSets.ENTITY)
			),
			registryProvider.retrieve(event)
		);
	}
}
