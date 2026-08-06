package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import tamaized.beanification.Autowired;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.registry.item.MaterialItems;

public class LiquifierBlockEntity extends BlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private static ModFluids modFluids;

	@Autowired
	private static MaterialItems materialItems;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntities.LIQUIFIER.get(), (object, context) -> object.items);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntities.LIQUIFIER.get(), (object, context) -> object.fluids);
	}

	public final ItemStackHandler items = new ItemStackHandler(1) {
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return stack.is(materialItems.VOIDIC_CRYSTAL.get());
		}
	};
	public final FluidTank fluids = new FluidTank(10000, fluidStack -> fluidStack.getFluid() == modFluids.VOIDIC_SOURCE.get());

	private final BlockPosDirectionCapabilityCacher<IFluidHandler> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int tick;
	private int processTick;

	public LiquifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.LIQUIFIER.get(), pPos, pBlockState);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		IItemHandler items = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
		if (items != null)
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(0));
		super.preRemoveSideEffects(pos, state);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		processTick = tag.getInt("processTick");
		items.deserializeNBT(registries, tag.getCompound("inventory"));
		fluids.readFromNBT(registries, tag.getCompound("tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("processTick", processTick);
		tag.put("inventory", items.serializeNBT(registries));
		tag.put("tank", fluids.writeToNBT(registries, new CompoundTag()));
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity be) {
		if (!(be instanceof LiquifierBlockEntity entity) || level.hasNeighborSignal(blockPos))
			return;
		entity.tick++;
		if (entity.fluids.getFluidInTank(0).getAmount() <= entity.fluids.getTankCapacity(0) - 250 && entity.items.getStackInSlot(0).is(materialItems.VOIDIC_CRYSTAL.get())) {
			entity.processTick++;
			if (entity.processTick >= 80) {
				entity.processTick = 0;
				entity.fluids.fill(new FluidStack(modFluids.VOIDIC_SOURCE.get(), 250), IFluidHandler.FluidAction.EXECUTE);
				entity.items.getStackInSlot(0).shrink(1);
				level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
						.filter(e -> e instanceof ServerPlayer)
						.map(ServerPlayer.class::cast)
						.forEach(advancementTriggers.LIQUIFIER_TRIGGER.get()::trigger);
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
