package tamaized.voidscape.registry;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;

public class ModBiomes {

	private static final DeferredRegister<Biome> REGISTRY = RegUtil.create(ForgeRegistries.BIOMES);

	public static final RegistryKey<Biome> VOID = register("void");
	public static final RegistryKey<Biome> OVERWORLD = register("overworld");
	public static final RegistryKey<Biome> NETHER = register("nether");
	public static final RegistryKey<Biome> END = register("end");

	private static RegistryKey<Biome> register(String id) {
		REGISTRY.register(id, () -> new Biome.Builder().
				precipitation(Biome.RainType.NONE).
				biomeCategory(Biome.Category.NONE).
				depth(0).
				scale(0).
				temperature(0).
				downfall(0).
				specialEffects(new BiomeAmbience.Builder().fogColor(0).skyColor(0).waterFogColor(0).waterColor(0).build()).
				mobSpawnSettings(MobSpawnInfo.EMPTY).
				generationSettings(BiomeGenerationSettings.EMPTY).
				temperatureAdjustment(Biome.TemperatureModifier.NONE).
				build());
		return RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Voidscape.MODID, id));
	}

	public static void classload() {

	}

}
