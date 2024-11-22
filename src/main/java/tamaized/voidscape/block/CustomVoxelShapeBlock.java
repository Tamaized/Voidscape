package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CustomVoxelShapeBlock extends Block {

	private final VoxelShape shape;

	public CustomVoxelShapeBlock(VoxelShape shape, Properties properties) {
		super(properties);
		this.shape = shape;
	}

	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return shape;
	}
}
