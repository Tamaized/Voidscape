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
public class NetherBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private SoulSandPlacedFeatureBootstrap soulSandPlacedFeatureBootstrap;

	@Autowired
	private QuartzPlacedFeatureBootstrap quartzPlacedFeatureBootstrap;

	@Autowired
	private NetherGoldPlacedFeatureBootstrap netherGoldPlacedFeatureBootstrap;

	@Autowired
	private DebrisPlacedFeatureBootstrap debrisPlacedFeatureBootstrap;

	@Autowired
	private FleshPlacedFeatureBootstrap fleshPlacedFeatureBootstrap;

	@Autowired
	private CrimsonFungusPlacedFeatureBootstrap crimsonFungusPlacedFeatureBootstrap;

	@Autowired
	private LavaPlacedFeatureBootstrap lavaPlacedFeatureBootstrap;

	@Autowired
	private CrimsonFungusBlockPlacedFeatureBootstrap crimsonFungusBlockPlacedFeatureBootstrap;

	@Autowired
	private CrimsonRootsPlacedFeatureBootstrap crimsonRootsPlacedFeatureBootstrap;

	@Autowired
	private EtherealFruitNetherPatchPlacedFeatureBootstrap etherealFruitNetherPatchPlacedFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.NETHER;
	}

	@Override
	public Biome make(BootstrapContext<Biome> context) {
		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0)
			.downfall(0F)
			.specialEffects(new BiomeSpecialEffects.Builder()
				.fogColor(0x0A010C)
				.waterColor(0x0A010C)
				.waterFogColor(0x0A010C)
				.skyColor(0XA010C)
				.foliageColorOverride(0X210C02)
				.grassColorOverride(0X210C02)
				.ambientParticle(new AmbientParticleSettings(
					ParticleTypes.CRIMSON_SPORE,
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
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, soulSandPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, quartzPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, netherGoldPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, debrisPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, fleshPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, crimsonFungusPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, lavaPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, crimsonFungusBlockPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, crimsonRootsPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, etherealFruitNetherPatchPlacedFeatureBootstrap.get().orElseThrow())
				.build())
			.mobSpawnSettings(new ExtendedMobSpawnSettingsBuilder()
				.creatureGenerationProbability(0F)
				.addMobCharge(EntityType.WITHER_SKELETON, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.WITHER_SKELETON,
					Weight.of(5),
					1,
					1
				))
				.addMobCharge(EntityType.ZOMBIFIED_PIGLIN, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.ZOMBIFIED_PIGLIN,
					Weight.of(20),
					1,
					1
				))
				.addMobCharge(EntityType.ZOGLIN, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.ZOGLIN,
					Weight.of(10),
					1,
					1
				))
				.addMobCharge(EntityType.GHAST, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.GHAST,
					Weight.of(25),
					4,
					4
				))
				.addMobCharge(EntityType.BLAZE, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.BLAZE,
					Weight.of(5),
					1,
					1
				))
				.build())
			.build();
	}
}
