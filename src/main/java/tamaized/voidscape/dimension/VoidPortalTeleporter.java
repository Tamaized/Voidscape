package tamaized.voidscape.dimension;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.registry.ModPOIs;
import tamaized.voidscape.registry.block.FunctionalBlocks;
import tamaized.voidscape.registry.block.ImposterBlocks;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

@Component
public final class VoidPortalTeleporter {

	@Autowired
	private ModPOIs pois;

	@Autowired
	private ImposterBlocks imposterBlocks;

	@Autowired
	private FunctionalBlocks functionalBlocks;

	public Optional<DimensionTransition> make(Entity entity, ServerLevel destination) {
		WorldBorder border = destination.getWorldBorder();
		double minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
		double minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
		double maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
		double maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
		double offset = DimensionType.getTeleportationScale(entity.level().dimensionType(), destination.dimensionType());
		BlockPos blockpos = BlockPos.containing(Mth.clamp(entity.getX() * offset, minX, maxX), entity.getY(), Mth.clamp(entity.getZ() * offset, minZ, maxZ));

		Optional<BlockUtil.FoundRectangle> destPortal = this.getExistingPortal(destination, blockpos);
		if (destPortal.isEmpty() && entity instanceof ServerPlayer) {
			destPortal = this.makePortal(destination, blockpos, entity.level().getBlockState(entity.blockPosition()).getOptionalValue(PortalBlock.AXIS).orElse(Direction.Axis.X));
		}

		return destPortal.map(portal -> NetherPortalBlock.createDimensionTransition(
			destination,
			portal,
			Direction.Axis.X,
			new Vec3(0.0D, 0.0D, 1.0D),
			entity,
			entity.getDeltaMovement(),
			entity.getYRot(),
			entity.getXRot(),
			DimensionTransition.PLAY_PORTAL_SOUND.then(transition -> transition.placePortalTicket(portal.minCorner))
		));
	}

	public Optional<BlockUtil.FoundRectangle> getExistingPortal(ServerLevel level, BlockPos pos) {
		PoiManager poimanager = level.getPoiManager();
		int i = 64;
		poimanager.ensureLoadedAndValid(level, pos, i);
		Optional<PoiRecord> optional = poimanager.getInSquare(type ->
						type.is(Objects.requireNonNull(pois.PORTAL.getKey())), pos, i, PoiManager.Occupancy.ANY)
				.sorted(Comparator.comparingDouble((ToDoubleFunction<PoiRecord>) poi ->
								poi.getPos().distSqr(pos))
						.thenComparingInt(poi ->
								poi.getPos().getY()))
				.filter(poi ->
						level.getBlockState(poi.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
				.findFirst();
		return optional.map((poi) -> {
			BlockPos blockpos = poi.getPos();
			level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
			BlockState blockstate = level.getBlockState(blockpos);
			return BlockUtil.getLargestRectangleAround(blockpos, blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (posIn) -> level.getBlockState(posIn) == blockstate);
		});
	}

	public Optional<BlockUtil.FoundRectangle> makePortal(Level level, BlockPos pos, Direction.Axis axis) {
		Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		double d0 = -1.0D;
		BlockPos blockpos = null;
		double d1 = -1.0D;
		BlockPos blockpos1 = null;
		WorldBorder border = level.getWorldBorder();
		int height = level.getHeight() - 1;
		BlockPos.MutableBlockPos mutable = pos.mutable();

		for (BlockPos.MutableBlockPos mut : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
			int j = Math.min(height, level.getHeight(Heightmap.Types.MOTION_BLOCKING, mut.getX(), mut.getZ()));
			if (border.isWithinBounds(mut) && border.isWithinBounds(mut.move(direction, 1))) {
				mut.move(direction.getOpposite(), 1);

				for(int l = j; l >= 0; --l) {
					mut.setY(l);
					if (level.isEmptyBlock(mut)) {
						int i1;
						i1 = l;
						while (l > 0 && level.isEmptyBlock(mut.move(Direction.DOWN))) {
							--l;
						}

						if (l + 4 <= height) {
							int j1 = i1 - l;
							if (j1 <= 0 || j1 >= 3) {
								mut.setY(l);
								if (this.checkRegionForPlacement(level, mut, mutable, direction, 0)) {
									double d2 = pos.distSqr(mut);
									if (this.checkRegionForPlacement(level, mut, mutable, direction, -1) && this.checkRegionForPlacement(level, mut, mutable, direction, 1) && (d0 == -1.0D || d0 > d2)) {
										d0 = d2;
										blockpos = mut.immutable();
									}

									if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
										d1 = d2;
										blockpos1 = mut.immutable();
									}
								}
							}
						}
					}
				}
			}
		}

		if (d0 == -1.0D && d1 != -1.0D) {
			blockpos = blockpos1;
			d0 = d1;
		}

		if (d0 == -1.0D) {
			blockpos = (new BlockPos(pos.getX(), Mth.clamp(pos.getY(), 70, level.getHeight() - 10), pos.getZ())).immutable();
			Direction drotated = direction.getClockWise();
			if (!border.isWithinBounds(blockpos)) {
				return Optional.empty();
			}

			for (int fOffset = -1; fOffset < 2; ++fOffset) {
				for (int fWidth = 0; fWidth < 2; ++fWidth) {
					for (int fHeight = -1; fHeight < 3; ++fHeight) {
						boolean flag = fHeight < 0;
						mutable.setWithOffset(blockpos, fWidth * direction.getStepX() + fOffset * drotated.getStepX(), fHeight, fWidth * direction.getStepZ() + fOffset * direction.getStepZ());
						level.setBlockAndUpdate(mutable, flag ? imposterBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get().defaultBlockState() : Blocks.AIR.defaultBlockState());
					}
				}
			}
		}

		for (int fWidth = -1; fWidth < 3; ++fWidth) {
			for (int fHeight = -1; fHeight < 4; ++fHeight) {
				if (fWidth == -1 || fWidth == 2 || fHeight == -1 || fHeight == 3) {
					mutable.setWithOffset(blockpos, fWidth * direction.getStepX(), fHeight, fWidth * direction.getStepZ());
					level.setBlockAndUpdate(mutable, imposterBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get().defaultBlockState());
				}
			}
		}

		BlockState portal = functionalBlocks.PORTAL.get().defaultBlockState().setValue(PortalBlock.AXIS, axis);
		for (int pWidth = 0; pWidth < 2; ++pWidth) {
			for (int pHeight = 0; pHeight < 3; ++pHeight) {
				mutable.setWithOffset(blockpos, pWidth * direction.getStepX(), pHeight, pWidth * direction.getStepZ());
				level.setBlock(mutable, portal, 18);
			}
		}

		return Optional.of(new BlockUtil.FoundRectangle(blockpos.immutable(), 2, 3));
	}

	private boolean checkRegionForPlacement(Level level, BlockPos originalPos, BlockPos.MutableBlockPos offsetPos, Direction directionIn, int offsetScale) {
		Direction direction = directionIn.getClockWise();

		for(int i = -1; i < 3; ++i) {
			for(int j = -1; j < 4; ++j) {
				offsetPos.setWithOffset(originalPos, directionIn.getStepX() * i + direction.getStepX() * offsetScale, j, directionIn.getStepZ() * i + direction.getStepZ() * offsetScale);
				if (j < 0 && !level.getBlockState(offsetPos).isSolid()) {
					return false;
				}

				if (j >= 0 && !level.isEmptyBlock(offsetPos)) {
					return false;
				}
			}
		}

		return true;
	}
}
