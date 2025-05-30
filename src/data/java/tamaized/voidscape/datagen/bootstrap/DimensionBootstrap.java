package tamaized.voidscape.datagen.bootstrap;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.biome.LayeredBiomeProvider;
import tamaized.voidscape.biome.genlayer.GenLayerRandomWithOneMajorBiomes;
import tamaized.voidscape.dimension.VoidChunkGenerator;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModDimensions;

import java.util.List;

@Component
public class DimensionBootstrap implements IBootstrap {

	@Autowired
	private DimensionTypeBootstrap dimensionTypeBootstrap;

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private NoiseGeneratorSettingsBootstrap noiseGeneratorSettingsBootstrap;

	@Override
	public int priority() {
		return dimensionTypeBootstrap.priority() + 1;
	}

	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.LEVEL_STEM, context -> {
			context.register(
				ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "void")),
				new LevelStem(
					dimensionTypeBootstrap.getVoid(),
					new VoidChunkGenerator(
						new LayeredBiomeProvider(
							context.lookup(Registries.BIOME),
							List.of(
								Either.left(biomes.THUNDERSPIRES),
								Either.left(biomes.THUNDER_FOREST),
								Either.left(biomes.ANTISPIRES),
								Either.left(biomes.NULL),
								Either.left(biomes.VOID),
								Either.left(biomes.OVERWORLD),
								Either.left(biomes.NETHER),
								Either.left(biomes.END),
								Either.right(new LayeredBiomeProvider.ConditionalBiomeHolder(
									biomes.AETHER,
									"aether"
								))
							),
							32,
							160,
							new GenLayerRandomWithOneMajorBiomes(
								List.of(
									Either.left(biomes.THUNDER_FOREST)
								),
								biomes.THUNDER_FOREST,
								4
							),
							new GenLayerRandomWithOneMajorBiomes(
								List.of(
									Either.left(biomes.NULL),
									Either.left(biomes.OVERWORLD),
									Either.left(biomes.NETHER),
									Either.left(biomes.END),
									Either.right(new LayeredBiomeProvider.ConditionalBiomeHolder(
										biomes.AETHER,
										"aether"
									))
								),
								biomes.VOID,
								4
							),
							new GenLayerRandomWithOneMajorBiomes(
								List.of(),
								biomes.ANTISPIRES,
								4
							)
						),
						noiseGeneratorSettingsBootstrap.getVoid(builder)
					)
				)
			);
		});
	}

}
