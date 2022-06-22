package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class InstanceChunkGenerator extends ChunkGenerator {

	public static final Codec<InstanceChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> commonCodec(p_236091_0_).and(p_236091_0_.
			group(BiomeSource.CODEC.
					fieldOf("biome_source").
					forGetter(ChunkGenerator::getBiomeSource), ResourceLocation.CODEC.
					fieldOf("snapshot").
					forGetter(InstanceChunkGenerator::snapshot), ResourceLocation.CODEC.
					fieldOf("instance_group").
					forGetter(InstanceChunkGenerator::group))).
			apply(p_236091_0_, p_236091_0_.stable(InstanceChunkGenerator::new)));

	private final ResourceLocation snapshot;
	private final ResourceLocation group;

	private InstanceChunkGenerator(Registry<StructureSet> p_209112_, BiomeSource biomeProvider1, ResourceLocation snapshot, ResourceLocation group) {
		super(p_209112_, Optional.empty(), biomeProvider1);
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
	public void applyCarvers(WorldGenRegion p_223043_, long p_223044_, RandomState p_223045_, BiomeManager p_223046_, StructureManager p_223047_, ChunkAccess p_223048_, GenerationStep.Carving p_223049_) {

	}

	@Override
	public void buildSurface(WorldGenRegion p_223050_, StructureManager p_223051_, RandomState p_223052_, ChunkAccess chunk_) {
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
	public void spawnOriginalMobs(WorldGenRegion p_62167_) {

	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_223209_, Blender p_223210_, RandomState p_223211_, StructureManager p_223212_, ChunkAccess chunk_) {
		return CompletableFuture.completedFuture(chunk_);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int p_223032_, int p_223033_, Heightmap.Types p_223034_, LevelHeightAccessor p_223035_, RandomState p_223036_) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int p_223028_, int p_223029_, LevelHeightAccessor p_223030_, RandomState p_223031_) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> p_223175_, RandomState p_223176_, BlockPos p_223177_) {

	}
}
