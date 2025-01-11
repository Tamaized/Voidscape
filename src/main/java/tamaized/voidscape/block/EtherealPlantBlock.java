package tamaized.voidscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.registry.block.NullBiomeBlocks;

@Configurable
public class EtherealPlantBlock extends BushBlock {

	public static final MapCodec<EtherealPlantBlock> CODEC = simpleCodec(EtherealPlantBlock::new);

	private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);

	@Autowired
	private NullBiomeBlocks nullBiomeBlocks;

	public EtherealPlantBlock(Properties prop) {
		super(prop);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT) ||
				state.is(Blocks.BEDROCK) ||
				state.is(BlockTags.BASE_STONE_NETHER) ||
				state.is(BlockTags.NYLIUM) ||
				state.is(Blocks.END_STONE) ||
				state.is(nullBiomeBlocks.NULL_BLACK.get());
	}
}
