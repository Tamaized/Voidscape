package tamaized.voidscape.world;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.neoforged.fml.ModList;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.world.genlayer.GenLayerBiomeStabilize;
import tamaized.voidscape.world.genlayer.GenLayerRandomWithOneMajorBiomes;
import tamaized.voidscape.world.genlayer.legacy.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class VoidscapeLayeredBiomeProvider extends BiomeSource {

	public static final Codec<VoidscapeLayeredBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			RegistryOps.retrieveGetter(Registries.BIOME),
			Codec.list(conditionalModLoadedBiome()).fieldOf("possibleBiomes").stable().forGetter(obj -> obj.possibleBiomes),
			Codec.INT.fieldOf("layerBottomDownwardsStart").stable().forGetter(obj -> obj.layerBottomDownwardsStart),
			Codec.INT.fieldOf("layerTopUpwardsStart").stable().forGetter(obj -> obj.layerTopUpwardsStart),
			GenLayerRandomWithOneMajorBiomes.CODEC.fieldOf("layerTopUpwards").stable().forGetter(obj -> obj.layerTopUpwards),
			GenLayerRandomWithOneMajorBiomes.CODEC.fieldOf("layerThreeSlicesBetween").stable().forGetter(obj -> obj.layerThreeSlicesBetween),
			GenLayerRandomWithOneMajorBiomes.CODEC.fieldOf("layerBottomDownwards").stable().forGetter(obj -> obj.layerBottomDownwards)
	).apply(instance, instance.stable(VoidscapeLayeredBiomeProvider::new)));

	public static Codec<Either<ResourceKey<Biome>, ConditionalBiomeHolder>> conditionalModLoadedBiome() {
		return Codec.either(
				ResourceKey.codec(Registries.BIOME),
				RecordCodecBuilder.create(c -> c.group(
						ResourceKey.codec(Registries.BIOME).fieldOf("biome").stable().forGetter(o -> o.biome),
						Codec.STRING.fieldOf("modid").stable().forGetter(o -> o.modid)
				).apply(c, c.stable(ConditionalBiomeHolder::new)))
		);
	}

	public record ConditionalBiomeHolder(ResourceKey<Biome> biome, String modid) {

	}

	public static List<ResourceKey<Biome>> getConditionalBiomes(List<Either<ResourceKey<Biome>, ConditionalBiomeHolder>> biomes) {
		return biomes.stream().map(e -> {
			AtomicReference<ResourceKey<Biome>> result = new AtomicReference<>();
			e.ifLeft(result::set);
			e.ifRight(r -> {
				if (ModList.get().isLoaded(r.modid())) {
					result.set(r.biome());
				}
			});
			return result.get();
		}).filter(Objects::nonNull).toList();
	}

	private final HolderGetter<Biome> registry;
	private final List<Either<ResourceKey<Biome>, ConditionalBiomeHolder>> possibleBiomes;
	private final List<ResourceKey<Biome>> possibleBiomesLoaded;
	private final int layerBottomDownwardsStart;
	private final int layerTopUpwardsStart;
	private final GenLayerRandomWithOneMajorBiomes layerTopUpwards;
	private final GenLayerRandomWithOneMajorBiomes layerThreeSlicesBetween;
	private final GenLayerRandomWithOneMajorBiomes layerBottomDownwards;

	private final int[] layers;
	private final Map<ResourceKey<Biome>, Integer> idCache = new HashMap<>();
	private final Map<Integer, Holder.Reference<Biome>> biomeCache = new HashMap<>();
	private final Supplier<Layer> genTopUpwards;
	private final Supplier<Layer> genUpper;
	private final Supplier<Layer> genMiddle;
	private final Supplier<Layer> genLower;
	private final Supplier<Layer> genBottomDownwards;
	private final Random layerMergeRandom = new Random();

	public VoidscapeLayeredBiomeProvider(
			HolderGetter<Biome> registryIn,
			List<Either<ResourceKey<Biome>, ConditionalBiomeHolder>> possibleBiomes,
			int layerBottomDownwardsStart,
			int layerTopUpwardsStart,
			GenLayerRandomWithOneMajorBiomes layerTopUpwards,
			GenLayerRandomWithOneMajorBiomes layerThreeSlicesBetween,
			GenLayerRandomWithOneMajorBiomes layerBottomDownwards) {
		super();
		this.registry = registryIn;
		this.possibleBiomes = possibleBiomes;
		this.possibleBiomesLoaded = getConditionalBiomes(possibleBiomes);
		this.possibleBiomesLoaded.forEach(this::getBiomeId); // Allocate IDs

		this.layerBottomDownwardsStart = layerBottomDownwardsStart;
		this.layerTopUpwardsStart = layerTopUpwardsStart;
		final int split = (layerTopUpwardsStart - (layerBottomDownwardsStart * 2)) / 3;
		final int sliceBottom = layerBottomDownwardsStart + split;
		final int sliceTop = layerBottomDownwardsStart + split * 2;
		this.layers = new int[]{layerBottomDownwardsStart, sliceBottom, sliceTop, layerTopUpwardsStart};

		this.layerTopUpwards = layerTopUpwards;
		this.layerThreeSlicesBetween = layerThreeSlicesBetween;
		this.layerBottomDownwards = layerBottomDownwards;
		this.genTopUpwards = Suppliers.memoize(() -> makeLayers(0L, layerTopUpwards));
		this.genUpper = Suppliers.memoize(() -> makeLayers(0L, layerThreeSlicesBetween));
		this.genMiddle = Suppliers.memoize(() -> makeLayers(1L, layerThreeSlicesBetween));
		this.genLower = Suppliers.memoize(() -> makeLayers(2L, layerThreeSlicesBetween));
		this.genBottomDownwards = Suppliers.memoize(() -> makeLayers(0L, layerBottomDownwards));
	}

	public int getLayerY(int index) {
		return layers[index];
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return possibleBiomesLoaded.stream().map(registry::get).filter(Optional::isPresent).map(Optional::get);
	}

	public int getBiomeId(ResourceKey<Biome> biome) {
		Integer id = idCache.get(biome);
		if (id != null)
			return id;
		id = idCache.size();
		idCache.put(biome, id);
		return id;
	}

	private Optional<ResourceKey<Biome>> fromId(int id) {
		return idCache.entrySet().stream().filter(e -> e.getValue() == id).map(Map.Entry::getKey).findAny();
	}

	public Holder<Biome> getBiome(int id) {
		Optional<Holder.Reference<Biome>> biome = Optional.ofNullable(biomeCache.get(id));
		if (biome.isPresent())
			return biome.get();
		Optional<ResourceKey<Biome>> key = fromId(id);
		biome = key.flatMap(registry::get);
		if (biome.isEmpty())
			throw new IllegalStateException("Unknown biome id emitted by layers: " + id);
		biomeCache.put(id, biome.get());
		return biome.get();
	}

	private <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed, GenLayerRandomWithOneMajorBiomes layer) {
		AreaFactory<T> biomes = layer.setup(this).run(seed.apply(1L));

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);

		return biomes;
	}

	public Layer makeLayers(long salt, GenLayerRandomWithOneMajorBiomes layer) {
		AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, ASMHooks.seed + salt, context), layer);
		return new Layer(areaFactory) {
			@Override
			public Holder<Biome> get(Registry<Biome> p_242936_1_, int x, int y) {
				return getBiome(area.get(x, y));
			}
		};
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int cy, int z, Climate.Sampler p_186738_) {
		return getRealNoiseBiome(x, cy << 2, z);
	}

	public Holder<Biome> getRealNoiseBiome(int x, int y, int z) {
		final int layerBottomDownwardsStart = layers[0];
		final int layerTopUpwardsStart = layers[3];
		final int m1 = layers[1];
		final int m2 = layers[2];
		layerMergeRandom.setSeed(ASMHooks.seed + (x & -4) * 25117L + (z & -4) * 151121L);
		return getBiome(

				y < (layerBottomDownwardsStart - 2) ? genBottomDownwards.get().area.get(x, z) :

						y <= (layerBottomDownwardsStart + 2) ? layerMergeRandom.nextBoolean() ? genBottomDownwards.get().area.get(x, z) : genLower.get().area.get(x, z) :

								y < (m1 - 2) ? genLower.get().area.get(x, z) :

										y <= (m1 + 2) ? (layerMergeRandom.nextBoolean() ? genLower : genMiddle).get().area.get(x, z) :

												y < (m2 - 2) ? genMiddle.get().area.get(x, z) :

														y <= (m2 + 2) ? (layerMergeRandom.nextBoolean() ? genMiddle : genUpper).get().area.get(x, z) :

																y < (layerTopUpwardsStart - 2) ? genUpper.get().area.get(x, z) :

																		y <= (layerTopUpwardsStart + 2) ? layerMergeRandom.nextBoolean() ? genTopUpwards.get().area.get(x, z) : genUpper.get().area.get(x, z) :

																				genTopUpwards.get().area.get(x, z));
	}
}