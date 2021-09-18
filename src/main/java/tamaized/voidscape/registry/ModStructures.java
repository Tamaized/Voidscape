package tamaized.voidscape.registry;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidChunkGenerator;
import tamaized.voidscape.world.structures.NullStructure;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModStructures {

	public static final Map<StructureFeature<?>, StructureFeatureConfiguration> SEPARATION_SETTINGS = new HashMap<>();

	public static final StructureFeature<NoneFeatureConfiguration> NULL = new NullStructure();
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_NULL = NULL.configured(FeatureConfiguration.NONE);

	static void classload(IEventBus bus) {
		bus.addGenericListener(StructureFeature.class, (Consumer<RegistryEvent.Register<StructureFeature<?>>>) event -> {
			SEPARATION_SETTINGS.clear();

			register(event, NULL, CONFIGURED_NULL, new ResourceLocation(Voidscape.MODID, "null"), 1, 2);
		});
		MinecraftForge.EVENT_BUS.addListener((Consumer<WorldEvent.Load>) event -> {
			if (event.getWorld() instanceof ServerLevel serverWorld && ((ServerLevel) event.getWorld()).getChunkSource().generator instanceof VoidChunkGenerator) {
				Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
				tempMap.putAll(SEPARATION_SETTINGS);
				serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
			}
		});
	}

	private static void register(RegistryEvent.Register<StructureFeature<?>> event, StructureFeature<?> structure, ConfiguredStructureFeature<?, ?> config, ResourceLocation name, int min, int max) {
		event.getRegistry().register(structure.setRegistryName(name));
		StructureFeature.STRUCTURES_REGISTRY.put(name.toString(), structure);
		StructureFeatureConfiguration seperation = new StructureFeatureConfiguration(max, min, 472681346);
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).
				put(structure, seperation).build();
		SEPARATION_SETTINGS.put(structure, seperation);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(name.getNamespace(), "configured_".concat(name.getPath())), config);
		FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(structure, config);
	}

}
