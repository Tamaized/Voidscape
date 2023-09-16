package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModBlocks;

import javax.annotation.Nullable;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class EtherealPlantBlock extends BushBlock {

	public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);
	private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);

	public EtherealPlantBlock(Properties prop) {
		super(prop);
		registerDefaultState(getStateDefinition().any().setValue(STATE, State.VOID));
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		return biomeState(state == null ? defaultBlockState() : state, context.getLevel().getBiome(context.getClickedPos()).unwrapKey().map(ResourceKey::location).orElse(new ResourceLocation("")));
	}

	public static BlockState biomeState(BlockState state, @Nullable ResourceLocation biome) {
		if (biome == null || !biome.getNamespace().equals(Voidscape.MODID))
			return state;
		return switch (biome.getPath()) {
			case "null" -> state.setValue(STATE, State.NULL);
			case "overworld" -> state.setValue(STATE, State.OVERWORLD);
			case "nether" -> state.setValue(STATE, State.NETHER);
			case "end" -> state.setValue(STATE, State.END);
			default -> state;
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(STATE);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT) ||
				state.is(Blocks.BEDROCK) ||
				state.is(BlockTags.BASE_STONE_NETHER) ||
				state.is(BlockTags.NYLIUM) ||
				state.is(Blocks.END_STONE) ||
				state.is(ModBlocks.NULL_BLACK.get());
	}

	public enum State implements StringRepresentable {
		VOID, NULL, OVERWORLD, NETHER, END;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

}
