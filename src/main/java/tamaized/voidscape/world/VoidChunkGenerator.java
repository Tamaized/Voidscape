package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import tamaized.voidscape.Voidscape;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VoidChunkGenerator extends NoiseBasedChunkGenerator {

	public static final Codec<VoidChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> commonCodec(p_236091_0_).and(p_236091_0_.
			group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).
							forGetter((p_188716_) -> p_188716_.noises),

					BiomeSource.CODEC.
							fieldOf("biome_source").
							forGetter(ChunkGenerator::getBiomeSource),

					Codec.LONG.
							fieldOf("seed").orElseGet(() -> HackyWorldGen.seed).
							forGetter(gen -> gen.seed),

					NoiseGeneratorSettings.CODEC.
							fieldOf("settings").
							forGetter(VoidChunkGenerator::getDimensionSettings))).
			apply(p_236091_0_, p_236091_0_.stable(VoidChunkGenerator::new)));

	private final Registry<NormalNoise.NoiseParameters> noises;
	private final long seed;

	private VoidChunkGenerator(Registry<StructureSet> p_209106_, Registry<NormalNoise.NoiseParameters> noiseRegistry, BiomeSource biomeProvider1, long seed, Holder<NoiseGeneratorSettings> dimensionSettings) {
		super(p_209106_, noiseRegistry, biomeProvider1, dimensionSettings);
		this.noises = noiseRegistry;
		this.seed = seed;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return codec;
	}

	private Holder<NoiseGeneratorSettings> getDimensionSettings() {
		return settings;
	}

	/**
	 * Basically a copy of super with changes for Y sensitivity for our 3D biome system
	 */
	@Override
	public void applyBiomeDecoration(WorldGenLevel worldGenRegion_, ChunkAccess chunk, StructureManager structureManager_) {
		int centerX = chunk.getPos().x;
		int centerZ = chunk.getPos().z;
		int x = centerX * 16;
		int z = centerZ * 16;
		int[] yIterator = new int[]{0};
		boolean cast;
		if (cast = biomeSource instanceof VoidscapeSeededBiomeProvider) {
			final int[] layers = Arrays.stream(VoidscapeSeededBiomeProvider.LAYERS).map(i -> i + 3).toArray();
			final int[] result = new int[yIterator.length + layers.length];
			System.arraycopy(yIterator, 0, result, 0, yIterator.length);
			System.arraycopy(layers, 0, result, yIterator.length, layers.length);
			yIterator = result;
		}
		WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(seed));
		long seed = rand.setDecorationSeed(worldGenRegion_.getSeed(), x, z);
		Registry<Structure> structureRegistry = worldGenRegion_.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
		try {
			Map<Integer, List<Structure>> map = structureRegistry.stream().collect(Collectors.groupingBy(structure -> structure.step().ordinal()));
			List<FeatureSorter.StepFeatureData> list = this.featuresPerStep.get();
			int j = list.size();
			Registry<PlacedFeature> featureRegistry = worldGenRegion_.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
			int k = Math.max(GenerationStep.Decoration.values().length, j);

			for (int l = 0; l < k; ++l) {

				int i1 = 0;
				if (structureManager_.shouldGenerateStructures()) {
					for (Structure structurefeature : map.getOrDefault(l, Collections.emptyList())) {
						rand.setFeatureSeed(seed, i1, l);
						Supplier<String> supplier = () -> {
							return structureRegistry.getResourceKey(structurefeature).map(Object::toString).orElseGet(structurefeature::toString);
						};

						try {
							worldGenRegion_.setCurrentlyGenerating(supplier);
							structureManager_.startsForStructure(SectionPos.of(chunk.getPos(), chunk.getMinSection()), structurefeature).forEach((p_196726_) -> {
								p_196726_.placeInChunk(worldGenRegion_, structureManager_, this, rand, writableArea(chunk), chunk.getPos());
							});
						} catch (Exception exception) {
							CrashReport crashreport1 = CrashReport.forThrowable(exception, "Feature placement");
							crashreport1.addCategory("Feature").setDetail("Description", supplier::get);
							throw new ReportedException(crashreport1);
						}

						++i1;
					}
				}

				if (l < j) {
					IntSet intset = new IntArraySet();

					for (Holder<Biome> biome : biomeSource.possibleBiomes()) {
						List<HolderSet<PlacedFeature>> list2 = biome.value().getGenerationSettings().features();
						if (l < list2.size()) {
							HolderSet<PlacedFeature> list1 = list2.get(l);
							FeatureSorter.StepFeatureData biomesource$stepfeaturedata1 = list.get(l);
							list1.stream().map(Holder::value).forEach((p_196751_) -> {
								intset.add(biomesource$stepfeaturedata1.indexMapping().applyAsInt(p_196751_));
							});
						}
					}

					int j1 = intset.size();
					int[] aint = intset.toIntArray();
					Arrays.sort(aint);
					FeatureSorter.StepFeatureData biomesource$stepfeaturedata = list.get(l);

					for (int k1 = 0; k1 < j1; ++k1) {
						int l1 = aint[k1];
						PlacedFeature placedfeature = biomesource$stepfeaturedata.features().get(l1);
						Supplier<String> supplier1 = () -> {
							return featureRegistry.getResourceKey(placedfeature).map(Object::toString).orElseGet(placedfeature::toString);
						};
						rand.setFeatureSeed(seed, l1, l);

						try {
							worldGenRegion_.setCurrentlyGenerating(supplier1);
							for (int y : yIterator) {
								Holder<Biome> biome = cast ? ((VoidscapeSeededBiomeProvider) biomeSource).
										getRealNoiseBiome((centerX << 2) + 2, y, (centerZ << 2) + 2) : this.biomeSource.
										getNoiseBiome((centerX << 2) + 2, (y >> 2), (centerZ << 2) + 2,
												worldGenRegion_.getChunkSource() instanceof ServerChunkCache serverChunkCache ?
														serverChunkCache.randomState().sampler() :
														RandomState.create(
																worldGenRegion_.registryAccess(),
																getDimensionSettings().unwrapKey().orElse(ResourceKey.create(
																		Registry.NOISE_GENERATOR_SETTINGS_REGISTRY,
																		new ResourceLocation(Voidscape.MODID, "lol_i_guess_ill_die"))), // FIXME: need to separate the noise settings from the dimension json
																seed).sampler());
								if (biome.value().getGenerationSettings().hasFeature(placedfeature)) {
									BlockPos pos = new BlockPos(x, y, z);
									placedfeature.placeWithBiomeCheck(worldGenRegion_, this, rand, pos);
								}
							}
						} catch (Exception exception1) {
							CrashReport crashreport2 = CrashReport.forThrowable(exception1, "Feature placement");
							crashreport2.addCategory("Feature").setDetail("Description", supplier1::get);
							throw new ReportedException(crashreport2);
						}
					}
				}
			}
		} catch (Exception var14) {
			CrashReport lvt_13_1_ = CrashReport.forThrowable(var14, "Biome decoration");
			lvt_13_1_.addCategory("Generation").setDetail("CenterX", centerX).setDetail("CenterZ", centerZ).setDetail("Seed", seed);
			new ReportedException(lvt_13_1_).printStackTrace();
		}
	}

	private static BoundingBox writableArea(ChunkAccess p_187718_) { // TODO: make Y sensitive
		ChunkPos chunkpos = p_187718_.getPos();
		int i = chunkpos.getMinBlockX();
		int j = chunkpos.getMinBlockZ();
		LevelHeightAccessor levelheightaccessor = p_187718_.getHeightAccessorForGeneration();
		int k = levelheightaccessor.getMinBuildHeight() + 1;
		int l = levelheightaccessor.getMaxBuildHeight() - 1;
		return new BoundingBox(i, k, j, i + 15, l, j + 15);
	}

}
