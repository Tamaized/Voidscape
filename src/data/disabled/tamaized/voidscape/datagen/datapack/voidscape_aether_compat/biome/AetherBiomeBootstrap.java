package tamaized.voidscape.datagen.datapack.voidscape_aether_compat.biome;

import com.aetherteam.aether.client.particle.AetherParticleTypes;
import com.aetherteam.aether.entity.AetherEntityTypes;
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
import tamaized.voidscape.datagen.bootstrap.biome.IBiomeBootstrap;
import tamaized.voidscape.datagen.bootstrap.biome.SortedFeaturesBiomeGenerationSettingsBuilder;
import tamaized.voidscape.datagen.datapack.voidscape_aether_compat.feature.configured.*;
import tamaized.voidscape.registry.ModBiomes;
import tamaized.voidscape.registry.ModSounds;

@Component
public class AetherBiomeBootstrap implements IBiomeBootstrap {

	@Autowired
	private ModBiomes biomes;

	@Autowired
	private ModSounds sounds;

	@Autowired
	private AetherDirtOreFeatureBootstrap aetherDirtOreFeatureBootstrap;

	@Autowired
	private IcestoneOreFeatureBootstrap icestoneOreFeatureBootstrap;

	@Autowired
	private AmbrosiumOreFeatureBootstrap ambrosiumOreFeatureBootstrap;

	@Autowired
	private ZaniteOreBuriedFeatureBootstrap zaniteOreBuriedFeatureBootstrap;

	@Autowired
	private GravititeOreBuriedFeatureBootstrap gravititeOreBuriedFeatureBootstrap;

	@Autowired
	private GravititeOreFeatureBootstrap gravititeOreFeatureBootstrap;

	@Autowired
	private SkyrootMeadowTreesFeatureBootstrap skyrootMeadowTreesFeatureBootstrap;

	@Autowired
	private AetherGrassPatchFeatureBootstrap aetherGrassPatchFeatureBootstrap;

	@Autowired
	private AetherTallGrassPatchFeatureBootstrap aetherTallGrassPatchFeatureBootstrap;

	@Autowired
	private WhiteFlowerPatchFeatureBootstrap whiteFlowerPatchFeatureBootstrap;

	@Autowired
	private PurpleFlowerPatchFeatureBootstrap purpleFlowerPatchFeatureBootstrap;

	@Autowired
	private BerryBushPatchFeatureBootstrap berryBushPatchFeatureBootstrap;

	@Override
	public ResourceKey<Biome> key() {
		return biomes.AETHER;
	}

	@Override
	public Biome make(BootstrapContext<Biome> context) {
		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0)
			.downfall(0F)
			.specialEffects(new BiomeSpecialEffects.Builder()
				.fogColor(0X9393BC)
				.waterColor(0X3F76E4)
				.waterFogColor(0X50533)
				.skyColor(0XC0C0FF)
				.foliageColorOverride(0XB1FFCB)
				.grassColorOverride(0XB1FFCB)
				.ambientParticle(new AmbientParticleSettings(
					AetherParticleTypes.AETHER_PORTAL.get(),
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
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, aetherDirtOreFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, icestoneOreFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ambrosiumOreFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, zaniteOreBuriedFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, gravititeOreBuriedFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, gravititeOreFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, skyrootMeadowTreesFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, aetherGrassPatchFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, aetherTallGrassPatchFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, whiteFlowerPatchFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, purpleFlowerPatchFeatureBootstrap.asDirectReferenceHolder())
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, berryBushPatchFeatureBootstrap.asDirectReferenceHolder())
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0F)
				.addMobCharge(AetherEntityTypes.AERBUNNY.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.AERBUNNY.get(),
					Weight.of(20),
					4,
					4
				))
				.addMobCharge(AetherEntityTypes.BLUE_SWET.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.BLUE_SWET.get(),
					Weight.of(20),
					2,
					2
				))
				.addMobCharge(AetherEntityTypes.FLYING_COW.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.FLYING_COW.get(),
					Weight.of(20),
					4,
					4
				))
				.addMobCharge(AetherEntityTypes.GOLDEN_SWET.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.GOLDEN_SWET.get(),
					Weight.of(20),
					2,
					2
				))
				.addMobCharge(AetherEntityTypes.PHYG.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.PHYG.get(),
					Weight.of(20),
					4,
					4
				))
				.addMobCharge(AetherEntityTypes.SHEEPUFF.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.SHEEPUFF.get(),
					Weight.of(20),
					4,
					4
				))
				.addMobCharge(AetherEntityTypes.AECHOR_PLANT.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.AECHOR_PLANT.get(),
					Weight.of(20),
					1,
					1
				))
				.addMobCharge(AetherEntityTypes.COCKATRICE.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.COCKATRICE.get(),
					Weight.of(20),
					1,
					1
				))
				.addMobCharge(AetherEntityTypes.ZEPHYR.get(), 0.7F, 0.15F)
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
					AetherEntityTypes.ZEPHYR.get(),
					Weight.of(20),
					4,
					4
				))
				.build())
			.build();
	}
}
