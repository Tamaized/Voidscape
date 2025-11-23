package tamaized.voidscape.datagen.bootstrap.biome;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.feature.placed.AntispirePlacedFeatureBootstrap;
import tamaized.voidscape.datagen.bootstrap.feature.placed.EtherealFruitVoidPatchPlacedFeatureBootstrap;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModSounds;

@Component
public class AntispiresBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private AntispirePlacedFeatureBootstrap antispirePlacedFeatureBootstrap;

	@Autowired
	private EtherealFruitVoidPatchPlacedFeatureBootstrap etherealFruitVoidPatchPlacedFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.ANTISPIRES;
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
				.skyColor(0x0A010C)
				.foliageColorOverride(0x0A010C)
				.grassColorOverride(0x0A010C)
				.ambientParticle(new AmbientParticleSettings(
					ParticleTypes.ASH,
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
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, antispirePlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, etherealFruitVoidPatchPlacedFeatureBootstrap.get().orElseThrow())
				.build())
			.mobSpawnSettings(new ExtendedMobSpawnSettingsBuilder()
				.creatureGenerationProbability(0F)
				.build())
			.build();
	}
}
