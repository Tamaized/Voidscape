package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.attribute.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.*;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModSounds;

import java.util.List;
import java.util.Optional;

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
				.waterColor(0x0A010C)
				.foliageColorOverride(0X210C02)
				.grassColorOverride(0X210C02)
				.build())
			.setAttribute(EnvironmentAttributes.FOG_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.SKY_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(
				ParticleTypes.CRIMSON_SPORE,
				0.025F
			))
			.setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
				Optional.empty(),
				Optional.empty(),
				List.of(new AmbientAdditionsSettings(
					sounds.AMBIENCE,
					0.0015D
				))
			))
			.setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(new Music(
				sounds.MUSIC,
				12000,
				24000,
				true
			)))
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
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(
					EntityType.WITHER_SKELETON,
					1,
					1
				))
				.addMobCharge(EntityType.ZOMBIFIED_PIGLIN, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(
					EntityType.ZOMBIFIED_PIGLIN,
					1,
					1
				))
				.addMobCharge(EntityType.ZOGLIN, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(
					EntityType.ZOGLIN,
					1,
					1
				))
				.addMobCharge(EntityType.GHAST, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, 25, new MobSpawnSettings.SpawnerData(
					EntityType.GHAST,
					4,
					4
				))
				.addMobCharge(EntityType.BLAZE, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(
					EntityType.BLAZE,
					1,
					1
				))
				.build())
			.build();
	}
}
