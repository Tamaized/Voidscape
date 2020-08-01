package tamaized.voidscape.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class VoidBiome extends Biome {

	private static final BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	private static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(BEDROCK, BEDROCK, BEDROCK);

	public VoidBiome() {
		super(

				new Biome.Builder().
						surfaceBuilder(SurfaceBuilder.DEFAULT, SURFACE_BUILDER_CONFIG).
						precipitation(Biome.RainType.NONE).
						category(Category.NONE).
						depth(-3.0F).
						scale(1.8F).
						temperature(0.0F).
						downfall(0.0F).
						parent(null).
						func_235098_a_(ImmutableList.of(new Biome.Attributes(-0.5F, -0.5F, 0.0F, 0.0F, 1.0F))).
						func_235097_a_(((new BiomeAmbience.Builder()).
								func_235246_b_(0x000000). // waterColor
								func_235248_c_(0x000000). // waterFogColor
								func_235239_a_(0x000000). // fog color
								func_235243_a_(MoodSoundAmbience.field_235027_b_).
								func_235240_a_(new BackgroundMusicSelector(SoundEvents.MUSIC_END, 6000, 12000, false)).
								func_235238_a_())));
		this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.field_236240_b_, new ProbabilityConfig(0.2F)));
	}
}
