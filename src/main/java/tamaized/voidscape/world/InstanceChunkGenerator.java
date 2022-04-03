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
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
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
	public ChunkGenerator withSeed(long seed) {
		return new InstanceChunkGenerator(structureSets, biomeSource.withSeed(seed), snapshot(), group());
	}

	@Override
	public Climate.Sampler climateSampler() {
		return Climate.empty();
	}

	@Override
	public void applyCarvers(WorldGenRegion p_187691_, long p_187692_, BiomeManager p_187693_, StructureFeatureManager p_187694_, ChunkAccess p_187695_, GenerationStep.Carving p_187696_) {

	}

	@Override
	public void buildSurface(WorldGenRegion p_187697_, StructureFeatureManager p_187698_, ChunkAccess chunk_) {
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
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender p_187749_, StructureFeatureManager structureManager_, ChunkAccess chunk_) {
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
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor p_156156_) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int int_, int int1_, LevelHeightAccessor accessor) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {

	}
}
