package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.*;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModSounds;

@Component
public class OverworldBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private SandPlacedFeatureBootstrap sandPlacedFeatureBootstrap;

	@Autowired
	private TitanitePlacedFeatureBootstrap titanitePlacedFeatureBootstrap;

	@Autowired
	private CopperPlacedFeatureBootstrap copperPlacedFeatureBootstrap;

	@Autowired
	private DiamondPlacedFeatureBootstrap diamondPlacedFeatureBootstrap;

	@Autowired
	private EmeraldPlacedFeatureBootstrap emeraldPlacedFeatureBootstrap;

	@Autowired
	private GoldPlacedFeatureBootstrap goldPlacedFeatureBootstrap;

	@Autowired
	private IronPlacedFeatureBootstrap ironPlacedFeatureBootstrap;

	@Autowired
	private LapisPlacedFeatureBootstrap lapisPlacedFeatureBootstrap;

	@Autowired
	private RedstonePlacedFeatureBootstrap redstonePlacedFeatureBootstrap;

	@Autowired
	private CoalPlacedFeatureBootstrap coalPlacedFeatureBootstrap;

	@Autowired
	private WaterPlacedFeatureBootstrap waterPlacedFeatureBootstrap;

	@Autowired
	private OakPlacedFeatureBootstrap oakPlacedFeatureBootstrap;

	@Autowired
	private CherryPlacedFeatureBootstrap cherryPlacedFeatureBootstrap;

	@Autowired
	private FlowerCherryPlacedFeatureBootstrap flowerCherryPlacedFeatureBootstrap;

	@Autowired
	private GrassPatchPlacedFeatureBootstrap grassPatchPlacedFeatureBootstrap;

	@Autowired
	private EtherealFruitOverworldPatchPlacedFeatureBootstrap etherealFruitOverworldPatchPlacedFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.OVERWORLD;
	}

	@Override
	public Biome make(BootstrapContext<Biome> context) {
		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0)
			.downfall(0F)
			.specialEffects(new BiomeSpecialEffects.Builder()
				.fogColor(0x0A010C)
				.waterColor(0X3938C9)
				.waterFogColor(0x0A010C)
				.skyColor(0XA010C)
				.foliageColorOverride(0X4C763C)
				.grassColorOverride(0X4C763C)
				.ambientParticle(new AmbientParticleSettings(
					ParticleTypes.MYCELIUM,
					0.025F
				))
				.ambientAdditionsSound(new AmbientAdditionsSettings(
					sounds.AMBIENCE,
					0.0015F
				))
				.backgroundMusic(new Music(
					sounds.MUSIC,
					12000,
					24000,
					true
				))
				.build())
			.generationSettings(new SortedFeaturesBiomeGenerationSettingsBuilder(context)
				.addFeature(GenerationStep.Decoration.RAW_GENERATION, sandPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, titanitePlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, copperPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, diamondPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, emeraldPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, goldPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, ironPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, lapisPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, redstonePlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, coalPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, coalPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.STRONGHOLDS, waterPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, oakPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, cherryPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, flowerCherryPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, grassPatchPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, etherealFruitOverworldPatchPlacedFeatureBootstrap.get().orElseThrow())
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0F)
				.addMobCharge(EntityType.SPIDER, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.SPIDER,
					Weight.of(5),
					1,
					1
				))
				.addMobCharge(EntityType.ZOMBIE, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.ZOMBIE,
					Weight.of(20),
					1,
					1
				))
				.addMobCharge(EntityType.SKELETON, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.SKELETON,
					Weight.of(10),
					1,
					1
				))
				.addMobCharge(EntityType.CREEPER, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.CREEPER,
					Weight.of(5),
					1,
					1
				))
				.build())
			.build();
	}
}
