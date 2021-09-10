package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class InstanceChunkGenerator extends ChunkGenerator {

	public static final Codec<InstanceChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeSource.CODEC.
					fieldOf("biome_source").
					forGetter(ChunkGenerator::getBiomeSource), ResourceLocation.CODEC.
					fieldOf("snapshot").
					forGetter(InstanceChunkGenerator::snapshot), ResourceLocation.CODEC.
					fieldOf("instance_group").
					forGetter(InstanceChunkGenerator::group)).
			apply(p_236091_0_, p_236091_0_.stable(InstanceChunkGenerator::new)));

	private final ResourceLocation snapshot;
	private final ResourceLocation group;

	private InstanceChunkGenerator(BiomeSource biomeProvider1, ResourceLocation snapshot, ResourceLocation group) {
		super(biomeProvider1, new StructureSettings(Optional.empty(), new HashMap<>()));
		this.snapshot = snapshot;
		this.group = group;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return codec;
	}

	public ResourceLocation snapshot() {
		return snapshot;
	}

	public ResourceLocation group() {
		return group;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new InstanceChunkGenerator(biomeSource.withSeed(seed), snapshot(), group());
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion_, ChunkAccess chunk_) {
		ChunkPos pos = chunk_.getPos();
		if (pos.x == 0 && pos.z == 0) {
			BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
			for (int x = 0; x < 32; x++)
				for (int z = 0; z < 32; z++) {
					bp.set(x, 60, z);
					chunk_.setBlockState(bp, Blocks.BEDROCK.defaultBlockState(), false);
				}
		}
	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureManager_, ChunkAccess chunk_) {
		return CompletableFuture.completedFuture(chunk_);
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor p_156156_) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int int_, int int1_, LevelHeightAccessor accessor) {
		return new NoiseColumn(0, new BlockState[0]);
	}
}
