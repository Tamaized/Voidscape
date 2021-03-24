package tamaized.voidscape.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.world.genlayer.GenLayerBiomeStabilize;
import tamaized.voidscape.world.genlayer.GenLayerVoidBiomes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.LongFunction;

public class VoidscapeSeededBiomeProvider extends BiomeProvider {

	public static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(

			ModBiomes.VOID,

			ModBiomes.OVERWORLD,

			ModBiomes.NETHER,

			ModBiomes.END

	);
	public static final Codec<VoidscapeSeededBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.LONG.
			fieldOf("seed").stable().orElseGet(() -> HackyWorldGen.seed).forGetter((obj) -> obj.seed), RegistryLookupCodec.
			create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.registry)).apply(instance, instance.stable(VoidscapeSeededBiomeProvider::new)));
	private final Map<RegistryKey<Biome>, Integer> idCache = new HashMap<>();
	private final Map<Integer, Biome> biomeCache = new HashMap<>();
	private final Registry<Biome> registry;
	private final Layer genUpper;
	private final Layer genMiddle;
	private final Layer genLower;
	private final long seed;
	private final Random layerMergeRandom;

	public VoidscapeSeededBiomeProvider(long seed, Registry<Biome> registryIn) {
		super(BIOMES.stream().map(RegistryKey::location).map(registryIn::getOptional).filter(Optional::isPresent).map(opt -> opt::get));

		this.seed = seed;
		layerMergeRandom = new Random(seed);
		registry = registryIn;
		genUpper = makeLayers(seed);
		genMiddle = makeLayers(seed + 1);
		genLower = makeLayers(seed + 2);
	}

	public int getBiomeId(RegistryKey<Biome> biome) {
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

	private <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> makeLayers(LongFunction<C> seed) {
		IAreaFactory<T> biomes = GenLayerVoidBiomes.INSTANCE.setup(this).run(seed.apply(1L));

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);
		//		biomes = ZoomLayer.NORMAL.run(seed.apply(1003), biomes);
		//		biomes = ZoomLayer.NORMAL.run(seed.apply(1004), biomes);
		//		biomes = ZoomLayer.NORMAL.run(seed.apply(1005), biomes);

		return biomes;
	}

	public Layer makeLayers(long seed) {
		IAreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaLayerContext(25, seed, context));
		return new Layer(areaFactory) {
			@Override
			public Biome get(Registry<Biome> p_242936_1_, int x, int y) {
				return getBiome(area.get(x, y));
			}
		};
	}

	@Override
	protected Codec<? extends BiomeProvider> codec() {
		return CODEC;
	}

	@Override
	public BiomeProvider withSeed(long l) {
		return new VoidscapeSeededBiomeProvider(l, registry);
	}

	@Override
	public Biome getNoiseBiome(int x, int cy, int z) {
		final int y = cy << 2;
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
		final int antiSpireY = 16;
		final int thunderSpireY = 200 - antiSpireY;
		final int m = (thunderSpireY - (antiSpireY * 2)) / 3;
		final int m1 = antiSpireY + m;
		final int m2 = antiSpireY + m * 2;
		layerMergeRandom.setSeed(seed + (x & -4) * 25117 + (z & -4) * 151121);
		return getBiome((y < (antiSpireY - 4) || y > (thunderSpireY + 4)) ? (getBiomeId(ModBiomes.VOID)) :

				y <= antiSpireY ? layerMergeRandom.nextBoolean() ? getBiomeId(ModBiomes.VOID) : genLower.area.get(x, z) :

						y >= thunderSpireY ? layerMergeRandom.nextBoolean() ? getBiomeId(ModBiomes.VOID) : genUpper.area.get(x, z) :

								y < (m1 - 4) ? genLower.area.get(x, z) :

										y < m1 ? (layerMergeRandom.nextBoolean() ? genLower : genMiddle).area.get(x, z) :

												y > (m2 + 4) ? genUpper.area.get(x, z) :

														y > m2 ? (layerMergeRandom.nextBoolean() ? genUpper : genMiddle).area.get(x, z) : genMiddle.area.get(x, z));
	}
}