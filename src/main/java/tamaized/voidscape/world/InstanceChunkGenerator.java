package tamaized.voidscape.world;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import java.util.HashMap;
import java.util.Optional;

public class InstanceChunkGenerator extends ChunkGenerator {

	public static final Codec<InstanceChunkGenerator> codec = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.
			group(BiomeProvider.CODEC.
					fieldOf("biome_source").
					forGetter(ChunkGenerator::getBiomeSource), ResourceLocation.CODEC.
					fieldOf("snapshot").
					forGetter(InstanceChunkGenerator::snapshot), ResourceLocation.CODEC.
					fieldOf("instance_group").
					forGetter(InstanceChunkGenerator::group)).
			apply(p_236091_0_, p_236091_0_.stable(InstanceChunkGenerator::new)));

	private final ResourceLocation snapshot;
	private final ResourceLocation group;

	private InstanceChunkGenerator(BiomeProvider biomeProvider1, ResourceLocation snapshot, ResourceLocation group) {
		super(biomeProvider1, new DimensionStructuresSettings(Optional.empty(), new HashMap<>()));
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
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion_, IChunk chunk_) {
		ChunkPos pos = chunk_.getPos();
		if (pos.x == 0 && pos.z == 0) {
			BlockPos.Mutable bp = new BlockPos.Mutable();
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
	public void fillFromNoise(IWorld world_, StructureManager structureManager_, IChunk chunk_) {

	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	@Override
	public IBlockReader getBaseColumn(int int_, int int1_) {
		return new Blockreader(new BlockState[0]);
	}
}
