package tamaized.voidscape.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

public class ModBiomes implements RegistryClass {

	private static final DeferredRegister<Biome> REGISTRY = RegUtil.create(ForgeRegistries.BIOMES);

	public static final ResourceKey<Biome> VOID = register("void");
	public static final ResourceKey<Biome> OVERWORLD = register("overworld");
	public static final ResourceKey<Biome> NETHER = register("nether");
	public static final ResourceKey<Biome> END = register("end");
	public static final ResourceKey<Biome> THUNDER_SPIRES = register("thunderspires");
	public static final ResourceKey<Biome> ANTI_SPIRES = register("antispires");
	public static final ResourceKey<Biome> NULL = register("null");

	public static final ResourceKey<Biome> PAWN = register("pawn");
	public static final ResourceKey<Biome> PSYCHOSIS = register("psychosis");

	private static ResourceKey<Biome> register(String id) {
		REGISTRY.register(id, () -> new Biome.BiomeBuilder().
				precipitation(Biome.Precipitation.NONE).
				temperature(0).
				downfall(0).
				specialEffects(new BiomeSpecialEffects.Builder().fogColor(0).skyColor(0).waterFogColor(0).waterColor(0).build()).
				mobSpawnSettings(MobSpawnSettings.EMPTY).
				generationSettings(BiomeGenerationSettings.EMPTY).
				temperatureAdjustment(Biome.TemperatureModifier.NONE).
				build());
		return ResourceKey.create(Registries.BIOME, new ResourceLocation(Voidscape.MODID, id));
	}

	@Override
	public void init(IEventBus bus) {

	}

}
