package tamaized.voidscape.world;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tamaized.voidscape.Voidscape;

import javax.annotation.Nullable;

public class VoidDimension extends Dimension {

	public VoidDimension(World world, DimensionType dimensionType) {
		super(world, dimensionType, 0.0F);
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		GenerationSettings settings = Voidscape.CHUNK_GENERATOR_TYPE.get().createSettings();
		settings.setDefaultBlock(Blocks.BEDROCK.getDefaultState());
		settings.setDefaultFluid(Blocks.AIR.getDefaultState());
		return Voidscape.CHUNK_GENERATOR_TYPE.get().
				create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.getConfig(this.world.getWorldInfo()).setBiome(Voidscape.BIOME.get())), settings);
	}

	@Nullable
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean p_206920_2_) {
		return null;
	}

	@Nullable
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean p_206921_3_) {
		return null;
	}

	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
		return 0;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		return Vec3d.ZERO;
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public double getVoidFogYFactor() {
		return 0;
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		return true;
	}
}
