package tamaized.voidscape.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.world.genlayer.GenLayerBiomeStabilize;
import tamaized.voidscape.world.genlayer.GenLayerVoidBiomes;
import tamaized.voidscape.world.genlayer.legacy.Area;
import tamaized.voidscape.world.genlayer.legacy.AreaFactory;
import tamaized.voidscape.world.genlayer.legacy.BigContext;
import tamaized.voidscape.world.genlayer.legacy.Layer;
import tamaized.voidscape.world.genlayer.legacy.LazyArea;
import tamaized.voidscape.world.genlayer.legacy.LazyAreaContext;
import tamaized.voidscape.world.genlayer.legacy.ZoomLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.LongFunction;

public class VoidscapeSeededBiomeProvider extends BiomeSource {

	public static final List<ResourceKey<Biome>> BIOMES = ImmutableList.of(

			ModBiomes.VOID,

			ModBiomes.OVERWORLD,

			ModBiomes.NETHER,

			ModBiomes.END

	);
	public static final Codec<VoidscapeSeededBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.LONG.
			fieldOf("seed").stable().orElseGet(() -> HackyWorldGen.seed).forGetter((obj) -> obj.seed), RegistryLookupCodec.
			create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.registry)).apply(instance, instance.stable(VoidscapeSeededBiomeProvider::new)));
	private final Map<ResourceKey<Biome>, Integer> idCache = new HashMap<>();
	private final Map<Integer, Biome> biomeCache = new HashMap<>();
	private final Registry<Biome> registry;
	private final Layer genUpper;
	private final Layer genMiddle;
	private final Layer genLower;
	private final long seed;
	private final Random layerMergeRandom;

	public VoidscapeSeededBiomeProvider(long seed, Registry<Biome> registryIn) {
		super(BIOMES.stream().map(ResourceKey::location).map(registryIn::getOptional).filter(Optional::isPresent).map(opt -> opt::get));

		this.seed = seed;
		layerMergeRandom = new Random(seed);
		registry = registryIn;
		genUpper = makeLayers(seed);
		genMiddle = makeLayers(seed + 1);
		genLower = makeLayers(seed + 2);
	}

	public int getBiomeId(ResourceKey<Biome> biome) {
		Integer id = idCache.get(biome);
		if (id != null)
			return id;
		id = registry.getId(registry.get(biome));
		idCache.put(biome, id);
		return id;
	}

	public Biome getBiome(int id) {
		Biome biome = biomeCache.get(id);
		if (biome != null)
			return biome;
		biome = registry.byId(id);
		if (biome == null)
			throw new IllegalStateException("Unknown biome id emitted by layers: " + id);
		biomeCache.put(id, biome);
		return biome;
	}

	private <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed) {
		AreaFactory<T> biomes = GenLayerVoidBiomes.INSTANCE.setup(this).run(seed.apply(1L));

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);

		return biomes;
	}

	public Layer makeLayers(long seed) {
		AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context));
		return new Layer(areaFactory) {
			@Override
			public Biome get(Registry<Biome> p_242936_1_, int x, int y) {
				return getBiome(area.get(x, y));
			}
		};
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long l) {
		return new VoidscapeSeededBiomeProvider(l, registry);
	}

	@Override
	public Biome getNoiseBiome(int x, int cy, int z, Climate.Sampler p_186738_) {
		return getRealNoiseBiome(x, cy << 2, z);
	}

	public static final int[] LAYERS;

	static {
		final int antiSpireY = 32;
		final int thunderSpireY = 192 - antiSpireY;
		final int split = (thunderSpireY - (antiSpireY * 2)) / 3;
		final int sliceBottom = antiSpireY + split;
		final int sliceTop = antiSpireY + split * 2;
		LAYERS = new int[]{antiSpireY, sliceBottom, sliceTop, thunderSpireY};
	}

	public Biome getRealNoiseBiome(int x, int y, int z) {
		// Debug code to render an image of the biome layout within the ide
		/*final Map<Integer, Integer> remapColors = new HashMap<>();
		remapColors.put(getBiomeId(ModBiomes.VOID), 0x000000);
		remapColors.put(getBiomeId(ModBiomes.OVERWORLD), 0x00FF00);
		remapColors.put(getBiomeId(ModBiomes.NETHER), 0xFF0000);
		remapColors.put(getBiomeId(ModBiomes.END), 0x0000FF);
		BufferedImage imageL = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
		BufferedImage imageM = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
		BufferedImage imageU = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
		for(int ii = 0; ii < 3; ii++) {
			BufferedImage image = ii == 0 ? imageL : ii == 1 ? imageM : imageU;
			Graphics2D display = image.createGraphics();
			LazyArea area = (ii == 0 ? genLower : ii == 1 ? genMiddle : genUpper).area;
			for (int xx = 0; xx < image.getWidth(); xx++) {
				for (int zz = 0; zz < image.getHeight(); zz++) {
					int c = area.get(xx, zz);
					display.setColor(new Color(remapColors.getOrDefault(c, c)));
					display.drawRect(xx, zz, 1, 1);
				}
			}
		}
 		System.out.println("breakpoint");*/
		if (x * x + z * z <= 1225)
			return getBiome(getBiomeId(ModBiomes.NULL));
		final int antiSpireY = LAYERS[0];
		final int thunderSpireY = LAYERS[3];
		final int m1 = LAYERS[1];
		final int m2 = LAYERS[2];
		layerMergeRandom.setSeed(seed + (x & -4) * 25117L + (z & -4) * 151121L);
		return getBiome(

				y < (antiSpireY - 2) ? getBiomeId(ModBiomes.ANTI_SPIRES) :

						y <= (antiSpireY + 2) ? layerMergeRandom.nextBoolean() ? getBiomeId(ModBiomes.ANTI_SPIRES) : genLower.area.get(x, z) :

								y < (m1 - 2) ? genLower.area.get(x, z) :

										y <= (m1 + 2) ? (layerMergeRandom.nextBoolean() ? genLower : genMiddle).area.get(x, z) :

												y < (m2 - 2) ? genMiddle.area.get(x, z) :

														y <= (m2 + 2) ? (layerMergeRandom.nextBoolean() ? genMiddle : genUpper).area.get(x, z) :

																y < (thunderSpireY - 2) ? genUpper.area.get(x, z) :

																		y <= (thunderSpireY + 2) ? layerMergeRandom.nextBoolean() ? getBiomeId(ModBiomes.THUNDER_SPIRES) : genUpper.area.get(x, z) :

																				getBiomeId(ModBiomes.THUNDER_SPIRES));
	}
}