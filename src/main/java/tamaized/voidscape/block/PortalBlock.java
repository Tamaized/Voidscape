package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModSounds;
import tamaized.voidscape.world.ConfigurablePortalShape;

@SuppressWarnings("deprecation")
public class PortalBlock extends HalfTransparentBlock {

	public static final StatePredicate FRAME_TEST = (state, reader, pos) -> state.is(ModBlocks.VOIDIC_CRYSTAL_BLOCK.get()) || state.is(ModBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get());
	public static final StatePredicate PORTAL_TEST = (state, reader, pos) -> state.is(ModBlocks.PORTAL.get());
	public static final StatePredicate IGNITER_TEST = (state, reader, pos) -> false;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public PortalBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_AABB : X_AABB;
    }

    @Override
    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    @Deprecated
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        Direction.Axis directionAxis = facing.getAxis();
        Direction.Axis directionAxis1 = stateIn.getValue(AXIS);
        boolean flag = directionAxis1 != directionAxis && directionAxis.isHorizontal();
        return !flag && !facingState.is(this) && !(new ConfigurablePortalShape(worldIn, currentPos, directionAxis1, FRAME_TEST, PORTAL_TEST, IGNITER_TEST)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    @Deprecated
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions())
			entity.getData(ModDataAttachments.INSANITY).setInPortal(true);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, ModSounds.PORTAL.get(), SoundSource.BLOCKS, 1F, 1F, false);
        }

        double x = (float) pos.getX() + rand.nextFloat();
        double y = (float) pos.getY() + rand.nextFloat();
        double z = (float) pos.getZ() + rand.nextFloat();
        double sX = ((double) rand.nextFloat() - 0.5D) * 0.25D;
        double sY = ((double) rand.nextFloat() - 0.5D) * 0.25D;
        double sZ = ((double) rand.nextFloat() - 0.5D) * 0.25D;

        worldIn.addParticle(ParticleTypes.END_ROD, x, y, z, sX, sY, sZ);
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

}
