package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModBlockEntities;
import tamaized.voidscape.registry.ModFluids;

public class CoopBlockEntity extends BlockEntity {

	public final ItemStackHandler items = new ItemStackHandler(1) {
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return stack.is(Items.EGG);
		}
	};
	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == ModFluids.VOIDIC_SOURCE.get());

	private final BlockPosDirectionCapabilityCacher<IItemHandler> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int processTick;

	public CoopBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.COOP.get(), pPos, pBlockState);
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
		if (!(be instanceof CoopBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		IFluidHandler fluid = entity.fluids;
		if (entity.processTick <= 0 && fluid.getFluidInTank(0).getAmount() > 0) {
			fluid.drain(1, IFluidHandler.FluidAction.EXECUTE);
			entity.processTick = 60 + level.getRandom().nextInt(140);
		} else if (entity.processTick > 0) {
			entity.processTick--;
			if (entity.processTick <= 0) {
				if (!ItemHandlerHelper.insertItemStacked(entity.items, new ItemStack(Items.EGG), false).isEmpty())
					entity.processTick = 200;
				else {
					level.getEntities((Entity) null, new AABB(blockPos).inflate(6D), e -> e instanceof ServerPlayer player && !player.isSpectator()).stream()
							.map(ServerPlayer.class::cast)
							.forEach(ModAdvancementTriggers.COOP_TRIGGER.get()::trigger);
				}
			}
		}
		if (!entity.items.getStackInSlot(0).isEmpty() && level instanceof ServerLevel serverLevel) {
			for (Direction face : Direction.values()) {
				IItemHandler other = entity.capabilityCache.get(Capabilities.ItemHandler.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					ItemStack item = entity.items.getStackInSlot(0);
					int count = item.getCount();
					ItemStack consumed = ItemHandlerHelper.insertItemStacked(other, item, false);
					if (!consumed.isEmpty() && count == consumed.getCount())
						continue;
					entity.items.setStackInSlot(0, consumed);
					break;
				}
			}
		}
	}

}
