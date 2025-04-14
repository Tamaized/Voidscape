package tamaized.voidscape.datagen.bootstrap;

import com.aetherteam.aether.block.AetherBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModDimensions;
import tamaized.voidscape.registry.ModSurfaceRules;
import tamaized.voidscape.surfacerule.AirAboveConditionSource;

import javax.annotation.Nullable;
import java.util.List;

@Component
public class NoiseGeneratorSettingsBootstrap implements IBootstrap {

	@Autowired
	private ModDimensions dimensions;

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSurfaceRules surfaceRules;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	private Holder<NoiseGeneratorSettings> VOID;

	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.NOISE_SETTINGS, context -> {
			VOID = context.register(
				ResourceKey.create(Registries.NOISE_SETTINGS, dimensions.VOID.location()),
				new NoiseGeneratorSettings(
					new NoiseSettings(
						0,
						256,
						4,
						4
					),
					Blocks.BEDROCK.defaultBlockState(),
					Blocks.AIR.defaultBlockState(),
					new NoiseRouter(
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.add(
							DensityFunctions.yClampedGradient(
								256,
								140,
								-0.12D,
								0D
							),
							DensityFunctions.mul(
								DensityFunctions.constant(0.64D),
								DensityFunctions.interpolated(
									DensityFunctions.blendDensity(
										BlendedNoise.createUnseeded(
											1D,
											1D,
											80D,
											0.0166D,
											8D
										)
									)
								)
							).squeeze()
						),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero()
					),
					SurfaceRules.sequence(
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.OVERWORLD),
							SurfaceRules.sequence(
								SurfaceRules.ifTrue(
									new AirAboveConditionSource(),
									SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
								),
								SurfaceRules.state(Blocks.STONE.defaultBlockState())
							)
						),
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.NETHER),
							SurfaceRules.sequence(
								SurfaceRules.ifTrue(
									new AirAboveConditionSource(),
									SurfaceRules.state(Blocks.CRIMSON_NYLIUM.defaultBlockState())
								),
								SurfaceRules.state(Blocks.NETHERRACK.defaultBlockState())
							)
						),
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.END),
							SurfaceRules.state(Blocks.END_STONE.defaultBlockState())
						),
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.NULL),
							SurfaceRules.state(blocks.nullBiomeBlocks().NULL_BLACK.get().defaultBlockState())
						),
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.THUNDER_FOREST),
							SurfaceRules.sequence(
								SurfaceRules.ifTrue(
									new AirAboveConditionSource(),
									SurfaceRules.state(blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM.get().defaultBlockState())
								),
								SurfaceRules.state(Blocks.BEDROCK.defaultBlockState())
							)
						),
						SurfaceRules.ifTrue(
							SurfaceRules.isBiome(biomes.AETHER),
							SurfaceRules.sequence(
								SurfaceRules.ifTrue(
									new AirAboveConditionSource(),
									SurfaceRules.state(AetherBlocks.AETHER_GRASS_BLOCK.get().defaultBlockState())
								),
								SurfaceRules.state(AetherBlocks.HOLYSTONE.get().defaultBlockState())
							)
						)
					),
					List.of(),
					0,
					false,
					false,
					false,
					true
				)
			);
		});
	}

	public Holder<NoiseGeneratorSettings> getVoid(RegistrySetBuilder builder) {
		if (VOID == null)
			bootstrap(builder);
		return VOID;
	}

}
