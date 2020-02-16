package tamaized.voidscape.world;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;

public class VoidChunkGenerator extends NoiseChunkGenerator<GenerationSettings> {

	private final double[] field_222573_h = this.func_222572_j();

	public VoidChunkGenerator(IWorld world, BiomeProvider biomeProvider, GenerationSettings settings) {
		super(world, biomeProvider, 4, 8, 128, settings, false);
	}

	@Override
	public int getGroundHeight() {
		return 0;
	}

	@Override
	protected double[] getBiomeNoiseColumn(int p_222549_1_, int p_222549_2_) {
		return new double[]{0.0D, 0.0D};
	}

	@Override
	protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
		return this.field_222573_h[p_222545_5_];
	}

	private double[] func_222572_j() {
		double[] lvt_1_1_ = new double[this.noiseSizeY()];

		for(int lvt_2_1_ = 0; lvt_2_1_ < this.noiseSizeY(); ++lvt_2_1_) {
			lvt_1_1_[lvt_2_1_] = Math.cos((double)lvt_2_1_ * 3.141592653589793D * 6.0D / (double)this.noiseSizeY()) * 2.0D;
			double lvt_3_1_ = lvt_2_1_;
			if (lvt_2_1_ > this.noiseSizeY() / 2) {
				lvt_3_1_ = this.noiseSizeY() - 1 - lvt_2_1_;
			}

			if (lvt_3_1_ < 4.0D) {
				lvt_3_1_ = 4.0D - lvt_3_1_;
				lvt_1_1_[lvt_2_1_] -= lvt_3_1_ * lvt_3_1_ * lvt_3_1_ * 10.0D;
			}
		}

		return lvt_1_1_;
	}

	@Override
	protected void fillNoiseColumn(double[] p_222548_1_, int p_222548_2_, int p_222548_3_) {
		this.func_222546_a(p_222548_1_, p_222548_2_, p_222548_3_, 684.412D, 2053.236D, 8.555150000000001D, 34.2206D, 3, -10);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}
}
