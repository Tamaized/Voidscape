package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.structures.NullStructure;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ModNoiseGeneratorSettings implements RegistryClass {

	private static final DeferredRegister<NoiseGeneratorSettings> REGISTRY = RegUtil.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);

	/*public static final RegistryObject<NoiseGeneratorSettings> VOID = REGISTRY.register("void", () ->
			fixSettings(new NoiseGeneratorSettings(
					NoiseSettings.create(0, 256, 1, 2),
					Blocks.BEDROCK.defaultBlockState(), Blocks.AIR.defaultBlockState(),
					new NoiseRouter(
							DensityFunctions.zero(),
							DensityFunctions.zero(),
							DensityFunctions.zero(),
							DensityFunctions.zero(),
							DensityFunctions.shiftedNoise2d(
									getDensityFunction("shift_x"),
									getDensityFunction("shift_z"),
									0.25D,
									BuiltinRegistries.NOISE.getHolderOrThrow(Noises.TEMPERATURE)
							),
							DensityFunctions.shiftedNoise2d(
									getDensityFunction("shift_x"),
									getDensityFunction("shift_z"),
									0.25D,
									BuiltinRegistries.NOISE.getHolderOrThrow(Noises.VEGETATION)
							),
							getDensityFunction("overworld/continents"),
							getDensityFunction("overworld/erosion"),
							getDensityFunction("overworld/depth"),
							getDensityFunction("overworld/ridges"),
							DensityFunctions.zero(),
							DensityFunctions.mul(
									DensityFunctions.constant(0.64D),
									DensityFunctions.interpolated(
											DensityFunctions.blendDensity(
													BlendedNoise.createUnseeded(0.04999999907253873D, 3D, 40D, 0.0166D, 1D)
											)
									)
							).squeeze(),
							DensityFunctions.zero(),
							DensityFunctions.zero(),
							DensityFunctions.zero()
					),
					SurfaceRules.sequence(
							SurfaceRules.ifTrue(
									SurfaceRules.isBiome(ModBiomes.OVERWORLD),
									SurfaceRules.sequence(
											SurfaceRules.ifTrue(
													new ModSurfaceRules.AirAboveConditionSource(),
													SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
											),
											SurfaceRules.state(Blocks.STONE.defaultBlockState())
									)
							),
							SurfaceRules.ifTrue(
									SurfaceRules.isBiome(ModBiomes.NETHER),
									SurfaceRules.sequence(
											SurfaceRules.ifTrue(
													new ModSurfaceRules.AirAboveConditionSource(),
													SurfaceRules.state(Blocks.CRIMSON_NYLIUM.defaultBlockState())
											),
											SurfaceRules.state(Blocks.NETHERRACK.defaultBlockState())
									)
							),
							SurfaceRules.ifTrue(
									SurfaceRules.isBiome(ModBiomes.END),
									SurfaceRules.state(Blocks.STONE.defaultBlockState())
							),
							SurfaceRules.ifTrue(
									SurfaceRules.isBiome(ModBiomes.NULL),
									SurfaceRules.ifTrue(
										SurfaceRules.yBlockCheck(VerticalAnchor.aboveBottom(1), 0),
											SurfaceRules.state(Blocks.AIR.defaultBlockState())
									)
							)
					),
					new ArrayList<>(),
					0,
					false,
					false,
					false,
					true
			)));*/

	@Override
	public void init(IEventBus bus) {
	}

	private static DensityFunctions.HolderHolder getDensityFunction(String key) {
		return new DensityFunctions.HolderHolder(BuiltinRegistries.DENSITY_FUNCTION.getHolderOrThrow(ResourceKey.create(Registry.DENSITY_FUNCTION_REGISTRY, new ResourceLocation(key))));
	}

	/**
	 * This is altered via ASM to use {@link ModNoiseGeneratorSettings.CorrectedNoiseSettings} instead of {@link NoiseSettings}
	 */
	private static NoiseGeneratorSettings fixSettings(NoiseGeneratorSettings settings) {
		NoiseSettings s = settings.noiseSettings();
		NoiseSettings noise = new NoiseSettings(s.minY(), s.height(),s.noiseSizeHorizontal(), s.noiseSizeVertical());
		return new NoiseGeneratorSettings(noise, settings.defaultBlock(), settings.defaultFluid(), settings.noiseRouter(), settings.surfaceRule(), settings.spawnTarget(), settings.seaLevel(), settings.disableMobGeneration(), settings.aquifersEnabled(), settings.oreVeinsEnabled(), settings.useLegacyRandomSource());
	}

	/**
	 * Extends {@link NoiseSettings)} via asm
	 */
	@SuppressWarnings("unused")
	private static class CorrectedNoiseSettings {

		private final int noiseSizeHorizontal;

		private CorrectedNoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
			this.noiseSizeHorizontal = noiseSizeHorizontal;
		}

		public int getCellWidth() {
			return noiseSizeHorizontal << 1;
		}

	}

}
