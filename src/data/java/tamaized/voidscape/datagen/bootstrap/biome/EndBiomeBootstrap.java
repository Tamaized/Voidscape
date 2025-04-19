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
public class EndBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private AmethystPlacedFeatureBootstrap amethystPlacedFeatureBootstrap;

	@Autowired
	private StrangePlacedFeatureBootstrap strangePlacedFeatureBootstrap;

	@Autowired
	private ChorusPlacedFeatureBootstrap chorusPlacedFeatureBootstrap;

	@Autowired
	private EtherealFruitEndPatchPlacedFeatureBootstrap etherealFruitEndPatchPlacedFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.END;
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
				.foliageColorOverride(0X290B2E)
				.grassColorOverride(0X290B2E)
				.ambientParticle(new AmbientParticleSettings(
					ParticleTypes.PORTAL,
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
			.generationSettings(new BiomeGenerationSettings.PlainBuilder()
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, amethystPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, strangePlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, chorusPlacedFeatureBootstrap.get().orElseThrow())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, etherealFruitEndPatchPlacedFeatureBootstrap.get().orElseThrow())
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0F)
				.addMobCharge(EntityType.ENDERMAN, 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					EntityType.ENDERMAN,
					Weight.of(20),
					1,
					1
				))
				.build())
			.build();
	}
}
