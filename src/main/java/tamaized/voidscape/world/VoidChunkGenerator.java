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
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.mutable.MutableObject;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Predicate;
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

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	private static final Method BeardifierMarker_INSTANCE;
	private static final MethodHandle handle_BeardifierMarker_INSTANCE;
	private static final Object object_BeardifierMarker_INSTANCE;
	private static final Field NoiseChunk_cellWidth;
	private static final MethodHandle handle_setter_NoiseChunk_cellWidth;
	private static final Field NoiseChunk_noiseSizeXZ;
	private static final MethodHandle handle_setter_NoiseChunk_noiseSizeXZ;

	static {
		Method tmp_BeardifierMarker_values = null;
		MethodHandle tmp_handle_BeardifierMarker_values = null;
		Object[] tmp_object_BeardifierMarker_values = null;
		Field tmp_NoiseChunk_cellWidth = null;
		MethodHandle tmp_handle_setter_NoiseChunk_cellWidth = null;
		Field tmp_NoiseChunk_noiseSizeXZ = null;
		MethodHandle tmp_handle_setter_NoiseChunk_noiseSizeXZ = null;
		Field tmp_NoiseChunk_FlatCache_values = null;
		MethodHandle tmp_handle_getter_NoiseChunk_FlatCache_values = null;
		try {
			tmp_BeardifierMarker_values = ObfuscationReflectionHelper.findMethod(Class.forName("net.minecraft.world.level.levelgen.DensityFunctions$BeardifierMarker"), "values");
			tmp_handle_BeardifierMarker_values= LOOKUP.unreflect(tmp_BeardifierMarker_values);
			tmp_object_BeardifierMarker_values = (Object[]) tmp_handle_BeardifierMarker_values.invoke();
			tmp_NoiseChunk_cellWidth = ObfuscationReflectionHelper.findField(NoiseChunk.class, "f_209170_");
			tmp_handle_setter_NoiseChunk_cellWidth = LOOKUP.unreflectSetter(tmp_NoiseChunk_cellWidth);
			tmp_NoiseChunk_noiseSizeXZ = ObfuscationReflectionHelper.findField(NoiseChunk.class, "f_209169_");
			tmp_handle_setter_NoiseChunk_noiseSizeXZ = LOOKUP.unreflectSetter(tmp_NoiseChunk_noiseSizeXZ);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		BeardifierMarker_INSTANCE = tmp_BeardifierMarker_values;
		handle_BeardifierMarker_INSTANCE = tmp_handle_BeardifierMarker_values;
		object_BeardifierMarker_INSTANCE = Objects.requireNonNull(tmp_object_BeardifierMarker_values)[0];
		NoiseChunk_cellWidth = tmp_NoiseChunk_cellWidth;
		handle_setter_NoiseChunk_cellWidth = tmp_handle_setter_NoiseChunk_cellWidth;
		NoiseChunk_noiseSizeXZ = tmp_NoiseChunk_noiseSizeXZ;
		handle_setter_NoiseChunk_noiseSizeXZ = tmp_handle_setter_NoiseChunk_noiseSizeXZ;
	}

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

	private void setupNoiseChunk(NoiseChunk noisechunk, int i, int cellWidth, ChunkPos chunkpos, Blender blender) {
		try {
			handle_setter_NoiseChunk_cellWidth.invokeExact(noisechunk, cellWidth);
			noisechunk.firstCellX = Math.floorDiv(chunkpos.getMinBlockX(), cellWidth);
			noisechunk.firstCellZ = Math.floorDiv(chunkpos.getMinBlockZ(), cellWidth);
			int noiseSizeXZ = QuartPos.fromBlock(i * cellWidth);
			handle_setter_NoiseChunk_noiseSizeXZ.invokeExact(noisechunk, noiseSizeXZ);
			int firstNoiseX = QuartPos.fromBlock(chunkpos.getMinBlockX());
			int firstNoiseZ = QuartPos.fromBlock(chunkpos.getMinBlockZ());
			for(int ii = 0; ii <= noiseSizeXZ; ++ii) {
				int j = firstNoiseX + ii;
				int k = QuartPos.toBlock(j);

				for(int l = 0; l <= noiseSizeXZ; ++l) {
					int i1 = firstNoiseZ + l;
					int j1 = QuartPos.toBlock(i1);
					Blender.BlendingOutput blender$blendingoutput = blender.blendOffsetAndFactor(k, j1);
					noisechunk.blendAlpha.values[ii][l] = blender$blendingoutput.alpha();
					noisechunk.blendOffset.values[ii][l] = blender$blendingoutput.blendingOffset();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public NoiseChunk createNoiseChunk(ChunkAccess chunkAccess, StructureManager structureManager, Blender blender, RandomState randomState) {
		NoiseSettings noisesettings = settings.value().noiseSettings().clampToHeightAccessor(chunkAccess);
		ChunkPos chunkpos = chunkAccess.getPos();
		int cellWidth = noisesettings.getCellWidth() >> 1;
		int i = 16 / cellWidth;
		NoiseChunk noisechunk =  new NoiseChunk(i, randomState, chunkpos.getMinBlockX(), chunkpos.getMinBlockZ(), noisesettings, Beardifier.forStructuresInChunk(structureManager, chunkAccess.getPos()), settings.value(), globalFluidPicker, blender);
		setupNoiseChunk(noisechunk, i, cellWidth, chunkpos, blender);
		return noisechunk;
	}

	@Override
	protected OptionalInt iterateNoiseColumn(LevelHeightAccessor levelHeightAccessor, RandomState random, int x, int z, @Nullable MutableObject<NoiseColumn> noiseColumnMutableObject, @Nullable Predicate<BlockState> blockStatePredicate) {
		NoiseSettings noisesettings = this.settings.value().noiseSettings().clampToHeightAccessor(levelHeightAccessor);
		int i = noisesettings.getCellHeight();
		int j = noisesettings.minY();
		int k = Mth.intFloorDiv(j, i);
		int l = Mth.intFloorDiv(noisesettings.height(), i);
		if (l <= 0) {
			return OptionalInt.empty();
		} else {
			BlockState[] ablockstate;
			if (noiseColumnMutableObject == null) {
				ablockstate = null;
			} else {
				ablockstate = new BlockState[noisesettings.height()];
				noiseColumnMutableObject.setValue(new NoiseColumn(j, ablockstate));
			}

			int i1 = noisesettings.getCellWidth() >> 1;
			int j1 = Math.floorDiv(x, i1);
			int k1 = Math.floorDiv(z, i1);
			int l1 = Math.floorMod(x, i1);
			int i2 = Math.floorMod(z, i1);
			int j2 = j1 * i1;
			int k2 = k1 * i1;
			double d0 = (double) l1 / (double) i1;
			double d1 = (double) i2 / (double) i1;
			NoiseChunk noisechunk = new NoiseChunk(1, random, j2, k2, noisesettings, (DensityFunctions.BeardifierOrMarker) object_BeardifierMarker_INSTANCE, this.settings.value(), this.globalFluidPicker, Blender.empty());
			setupNoiseChunk(noisechunk, 1, i1, new ChunkPos(x, z), Blender.empty());
			noisechunk.initializeForFirstCellX();
			noisechunk.advanceCellX(0);

			for (int l2 = l - 1; l2 >= 0; --l2) {
				noisechunk.selectCellYZ(l2, 0);

				for (int i3 = i - 1; i3 >= 0; --i3) {
					int j3 = (k + l2) * i + i3;
					double d2 = (double) i3 / (double) i;
					noisechunk.updateForY(j3, d2);
					noisechunk.updateForX(x, d0);
					noisechunk.updateForZ(z, d1);
					BlockState blockstate = noisechunk.blockStateRule.calculate(noisechunk);
					BlockState blockstate1 = blockstate == null ? this.defaultBlock : blockstate;
					if (ablockstate != null) {
						int k3 = l2 * i + i3;
						ablockstate[k3] = blockstate1;
					}

					if (blockStatePredicate != null && blockStatePredicate.test(blockstate1)) {
						noisechunk.stopInterpolation();
						return OptionalInt.of(j3 + 1);
					}
				}
			}

			noisechunk.stopInterpolation();
			return OptionalInt.empty();
		}
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
