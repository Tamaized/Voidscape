package tamaized.voidscape.datagen.bootstrap.structure;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.structure.CharredStructure;

import java.util.Map;

@Component
public class CharredStructureBootstrap extends StructureBootstrapHolder {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModEntities entities;

	@Override
	public String name() {
		return "charred";
	}

	@Override
	public Structure make(BootstrapContext<Structure> context) {
		return new CharredStructure(new Structure.StructureSettings.Builder(HolderSet.direct(
			context.lookup(Registries.BIOME).getOrThrow(biomes.VOID)
		))
			.generationStep(GenerationStep.Decoration.RAW_GENERATION)
			.spawnOverrides(Map.of(
				MobCategory.MONSTER,
				new StructureSpawnOverride(
					StructureSpawnOverride.BoundingBoxType.STRUCTURE,
					WeightedRandomList.create(new MobSpawnSettings.SpawnerData(
						entities.VOIDS_WRATH.get(), Weight.of(100), 1, 1
					))
				)
			))
			.build());
	}
}
