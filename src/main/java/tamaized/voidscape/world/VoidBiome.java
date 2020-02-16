package tamaized.voidscape.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
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
						depth(0.1F).
						scale(0.2F).
						temperature(2.0F).
						downfall(0.0F).
						waterColor(4159204).
						waterFogColor(329011).
						parent(null)

		);
		this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, new ProbabilityConfig(0.2F)));
	}
}
