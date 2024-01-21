package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModBlockEntities;

public class WellBlockEntity extends BlockEntity {

	public final FluidTank fluids = new FluidTank(Integer.MAX_VALUE, fluidStack -> fluidStack.getFluid() == Fluids.WATER);

	private final BlockPosDirectionCapabilityCacher<IFluidHandler> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int tick;

	public WellBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.WELL.get(), pPos, pBlockState);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof WellBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		if (entity.fluids.getFluidAmount() < Integer.MAX_VALUE)
			entity.fluids.fill(new FluidStack(Fluids.WATER, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
		entity.tick++;
		if (level instanceof ServerLevel serverLevel) {
			boolean filled = false;
			for (Direction face : Direction.values()) {
				IFluidHandler other = entity.capabilityCache.get(Capabilities.FluidHandler.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					if (other.fill(new FluidStack(Fluids.WATER, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE) > 0)
						filled = true;
				}
			}
			if (filled) {
				level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
						.filter(e -> e instanceof ServerPlayer)
						.map(ServerPlayer.class::cast)
						.forEach(ModAdvancementTriggers.WELL_TRIGGER.get()::trigger);
			}
		}
	}

}
