package tamaized.voidscape.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.structures.NullStructure;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModStructures implements RegistryClass {

	public static final Map<StructureFeature<?>, StructureFeatureConfiguration> SEPARATION_SETTINGS = new HashMap<>();

	public static final StructureFeature<NoneFeatureConfiguration> NULL = new NullStructure();
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_NULL = NULL.configured(FeatureConfiguration.NONE);

	private static void classloadPieces(StructurePieceType... noop) {
		// NO-OP
	}

	@Override
	public void init(IEventBus bus) {
		classloadPieces(NullStructure.Pieces.MAIN);
		bus.addGenericListener(StructureFeature.class, (Consumer<RegistryEvent.Register<StructureFeature<?>>>) event -> {
			SEPARATION_SETTINGS.clear();

			register(event, NULL, CONFIGURED_NULL, new ResourceLocation(Voidscape.MODID, "null"), 0, 1);
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<WorldEvent.Load>) event -> {
			if (event.getWorld() instanceof ServerLevel serverWorld && serverWorld.getChunkSource().getGenerator() instanceof VoidChunkGenerator) {
				StructureSettings settings = serverWorld.getChunkSource().getGenerator().getSettings();
				Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(settings.structureConfig());
				tempMap.putAll(SEPARATION_SETTINGS);
				serverWorld.getChunkSource().getGenerator().getSettings().structureConfig = tempMap;
				HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tmpMap = new HashMap<>();
				associateBiomeToConfiguredStructure(tmpMap, CONFIGURED_NULL, ModBiomes.NULL);
				ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
				settings.configuredStructures.entrySet().stream().filter(entry -> !tmpMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);
				tmpMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));
				settings.configuredStructures = tempStructureToMultiMap.build();
			}
		});
	}

	@SafeVarargs
	private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> map, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome>... biomeRegistryKey) {
		map.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
		HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = map.get(configuredStructureFeature.feature);
		for (ResourceKey<Biome> biome : biomeRegistryKey) {
			if (configuredStructureToBiomeMultiMap.containsValue(biome)) {
				Voidscape.LOGGER.error("""
								    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
								    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
								    The two conflicting ConfiguredStructures are: {}, {}
								    The biome that is attempting to be shared: {}
								""",
						BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
						BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biome).findFirst().get().getKey()),
						biome
				);
			} else {
				configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biome);
			}
		}
	}

	private static void register(RegistryEvent.Register<StructureFeature<?>> event, StructureFeature<?> structure, ConfiguredStructureFeature<?, ?> config, ResourceLocation name, int min, int max) {
		event.getRegistry().register(structure.setRegistryName(name));
		StructureFeature.STRUCTURES_REGISTRY.put(name.toString(), structure);
		StructureFeatureConfiguration seperation = new StructureFeatureConfiguration(max, min, 0);
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).
				put(structure, seperation).build();
		SEPARATION_SETTINGS.put(structure, seperation);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(name.getNamespace(), "configured_".concat(name.getPath())), config);
		//		FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(structure, config);
	}

}
