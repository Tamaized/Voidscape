package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.attribute.*;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.*;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModSounds;

import java.util.List;
import java.util.Optional;

@Component
public class ThunderForestBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ThunderFungusPlacedFeatureBootstrap thunderFungusPlacedFeatureBootstrap;

	@Autowired
	private EtherealFruitVoidPatchPlacedFeatureBootstrap etherealFruitVoidPatchPlacedFeatureBootstrap;

	@Autowired
	private ThunderFungusBlockPlacedFeatureBootstrap thunderFungusBlockPlacedFeatureBootstrap;

	@Autowired
	private ThunderRootsPlacedFeatureBootstrap thunderRootsPlacedFeatureBootstrap;

	@Autowired
	private ThunderVinesPlacedFeatureBootstrap thunderVinesPlacedFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.THUNDER_FOREST;
	}

	@Override
	public Biome make(BootstrapContext<Biome> context) {
		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0)
			.downfall(0F)
			.specialEffects(new BiomeSpecialEffects.Builder()
				.waterColor(0x0A010C)
				.foliageColorOverride(0X53198A)
				.grassColorOverride(0X53198A)
				.build())
			.setAttribute(EnvironmentAttributes.FOG_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.SKY_COLOR, 0x0A010C)
			.setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(
				ParticleTypes.WITCH,
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
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, thunderFungusPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, etherealFruitVoidPatchPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, thunderFungusBlockPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, thunderRootsPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, thunderVinesPlacedFeatureBootstrap.get().orElseThrow())
				.build())
			.mobSpawnSettings(new ExtendedMobSpawnSettingsBuilder()
				.creatureGenerationProbability(0F)
				.addMobCharge(entities.VOIDLING.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(
					entities.VOIDLING.get(),
					1,
					4
				))
				.build())
			.build();
	}
}
