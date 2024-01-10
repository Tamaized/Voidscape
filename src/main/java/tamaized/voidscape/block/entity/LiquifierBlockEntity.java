package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModBlockEntities;
import tamaized.voidscape.registry.ModFluids;
import tamaized.voidscape.registry.ModItems;

public class LiquifierBlockEntity extends BlockEntity {

	public final ItemStackHandler items = new ItemStackHandler(1);
	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	private final BlockPosDirectionCapabilityCacher<IFluidHandler> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int tick;
	private int processTick;

	public LiquifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.LIQUIFIER.get(), pPos, pBlockState);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		processTick = pTag.getInt("processTick");
		items.deserializeNBT(pTag.getCompound("inventory"));
		fluids.readFromNBT(pTag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.putInt("processTick", processTick);
		pTag.put("inventory", items.serializeNBT());
		pTag.put("tank", fluids.writeToNBT(new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof LiquifierBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		entity.tick++;
		if (entity.fluids.getFluidInTank(0).getAmount() <= entity.fluids.getTankCapacity(0) - 250 && entity.items.getStackInSlot(0).is(ModItems.VOIDIC_CRYSTAL.get())) {
			entity.processTick++;
			if (entity.processTick >= 80) {
				entity.processTick = 0;
				entity.fluids.fill(new FluidStack(ModFluids.VOIDIC_SOURCE.get(), 250), IFluidHandler.FluidAction.EXECUTE);
				entity.items.getStackInSlot(0).shrink(1);
				level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
						.filter(e -> e instanceof ServerPlayer)
						.map(ServerPlayer.class::cast)
						.forEach(ModAdvancementTriggers.LIQUIFIER_TRIGGER.get()::trigger);
			}
		} else {
			entity.processTick = 0;
		}
		if (entity.tick % 20 == 0 && entity.fluids.getFluidInTank(0).getAmount() > 0 && level instanceof ServerLevel serverLevel) {
			for (Direction face : Direction.values()) {
				IFluidHandler other = entity.capabilityCache.get(Capabilities.FluidHandler.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					int amount = other.fill(new FluidStack(entity.fluids.getFluidInTank(0).getFluid(), Math.min(entity.fluids.getFluidInTank(0).getAmount(), 1000)), IFluidHandler.FluidAction.EXECUTE);
					entity.fluids.drain(amount, IFluidHandler.FluidAction.EXECUTE);
				}
				if (entity.fluids.getFluidInTank(0).getAmount() <= 0)
					break;
			}
		}
	}

}
