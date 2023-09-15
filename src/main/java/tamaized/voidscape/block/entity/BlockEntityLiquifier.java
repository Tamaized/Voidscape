package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.registry.ModBlockEntities;
import tamaized.voidscape.registry.ModFluids;
import tamaized.voidscape.registry.ModItems;

public class BlockEntityLiquifier extends BlockEntity {

    private final LazyOptional<ItemStackHandler> items = LazyOptional.of(() -> new ItemStackHandler(1));
    private final LazyOptional<FluidTank> fluids = LazyOptional.of(() -> new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get()));

    private int tick;
    private int processTick;

    public BlockEntityLiquifier(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.LIQUIFIER.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return items.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluids.cast();
        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        items.invalidate();
        fluids.invalidate();
        super.invalidateCaps();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        processTick = pTag.getInt("processTick");
        items.resolve().orElseThrow().deserializeNBT(pTag.getCompound("inventory"));
        fluids.resolve().orElseThrow().readFromNBT(pTag.getCompound("tank"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("processTick", processTick);
        pTag.put("inventory", items.resolve().orElseThrow().serializeNBT());
        pTag.put("tank", fluids.resolve().orElseThrow().writeToNBT(new CompoundTag()));
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
        if (!(be instanceof BlockEntityLiquifier entity))
            return;
        entity.tick++;
        IFluidHandler fluid = entity.fluids.resolve().orElseThrow();
        IItemHandler item = entity.items.resolve().orElseThrow();
        if (fluid.getFluidInTank(0).getAmount() < fluid.getTankCapacity(0) - 250 && item.getStackInSlot(0).is(ModItems.VOIDIC_CRYSTAL.get())) {
            entity.processTick++;
            if (entity.processTick >= 80) {
                entity.processTick = 0;
                fluid.fill(new FluidStack(ModFluids.VOIDIC_SOURCE.get(), 250), IFluidHandler.FluidAction.EXECUTE);
                item.getStackInSlot(0).shrink(1);
            }
        } else {
            entity.processTick = 0;
        }
        if (entity.tick % 20 == 0 && fluid.getFluidInTank(0).getAmount() > 0) {
            for (Direction face : Direction.values()) {
                BlockEntity other = level.getBlockEntity(blockPos.relative(face));
                if (other != null) {
                    other.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(cap -> {
                        int amount = cap.fill(new FluidStack(fluid.getFluidInTank(0).getFluid(), Math.min(fluid.getFluidInTank(0).getAmount(), 1000)), IFluidHandler.FluidAction.EXECUTE);
                        fluid.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                    });
                    if (fluid.getFluidInTank(0).getAmount() <= 0)
                        break;
                }
            }
        }
    }

}
