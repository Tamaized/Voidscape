package tamaized.voidscape.world;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.BlockPortal;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModPOIs;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public final class VoidPortalTeleporter implements ITeleporter {

	public static final VoidPortalTeleporter INSTANCE = new VoidPortalTeleporter();

	private VoidPortalTeleporter() {

	}

	@Override
	public Entity placeEntity(Entity oldEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		oldEntity.fallDistance = 0;
		return repositionEntity.apply(false);
	}

	@Nullable
	@Override
	public PortalInfo getPortalInfo(Entity oldEntity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		if (!Voidscape.checkForVoidDimension(oldEntity.level()) && !Voidscape.checkForVoidDimension(destWorld)) {
			return null;
		} else {
			WorldBorder border = destWorld.getWorldBorder();
			double minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
			double minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
			double maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
			double maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
			double offset = DimensionType.getTeleportationScale(oldEntity.level().dimensionType(), destWorld.dimensionType());
			BlockPos blockpos = BlockPos.containing(Mth.clamp(oldEntity.getX() * offset, minX, maxX), oldEntity.getY(), Mth.clamp(oldEntity.getZ() * offset, minZ, maxZ));
			return this.getPortalLogic(destWorld, oldEntity, blockpos).map((portalresult) -> {
				return PortalShape.createPortalInfo(destWorld, portalresult, Direction.Axis.X, new Vec3(0.0D, 0.0D, 1.0D), oldEntity, oldEntity.getDeltaMovement(), oldEntity.getYRot(), oldEntity.getXRot());
			}).orElse(null);
		}
	}

	private Optional<BlockUtil.FoundRectangle> getPortalLogic(ServerLevel level, Entity entity, BlockPos pos) {
		Optional<BlockUtil.FoundRectangle> existing = this.getExistingPortal(level, pos);
		if (entity instanceof ServerPlayer) {
			if (existing.isPresent()) {
				return existing;
			} else {
				return this.makePortal(level, pos, entity.level().getBlockState(entity.blockPosition()).getOptionalValue(BlockPortal.AXIS).orElse(Direction.Axis.X));
			}
		} else {
			return existing;
		}
	}

	public Optional<BlockUtil.FoundRectangle> getExistingPortal(ServerLevel level, BlockPos pos) {
		PoiManager poimanager = level.getPoiManager();
		int i = 64;
		poimanager.ensureLoadedAndValid(level, pos, i);
		Optional<PoiRecord> optional = poimanager.getInSquare(type ->
						type.is(ModPOIs.PORTAL.getKey()), pos, i, PoiManager.Occupancy.ANY)
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
						for(i1 = l; l > 0 && level.isEmptyBlock(mut.move(Direction.DOWN)); --l) {
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

		//Place the frame blocks
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
						level.setBlockAndUpdate(mutable, flag ? ModBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get().defaultBlockState() : Blocks.AIR.defaultBlockState());
					}
				}
			}
		}

		for (int fWidth = -1; fWidth < 3; ++fWidth) {
			for (int fHeight = -1; fHeight < 4; ++fHeight) {
				if (fWidth == -1 || fWidth == 2 || fHeight == -1 || fHeight == 3) {
					mutable.setWithOffset(blockpos, fWidth * direction.getStepX(), fHeight, fWidth * direction.getStepZ());
					level.setBlockAndUpdate(mutable, ModBlocks.FRAGILE_VOIDIC_CRYSTAL_BLOCK.get().defaultBlockState());
				}
			}
		}

		//Place the portal blocks
		BlockState portal = ModBlocks.PORTAL.get().defaultBlockState().setValue(BlockPortal.AXIS, axis);
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
