package tamaized.voidscape.block.entity;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.capability.FilteredItemStacksResourceHandler;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.registry.item.MaterialItems;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.function.Supplier;

@Configurable
public class LiquifierBlockEntity extends TickableBlockEntity {

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private MaterialItems materialItems;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Item.BLOCK, blockEntities.LIQUIFIER.get(), (object, _) -> object.items);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.LIQUIFIER.get(), (object, _) -> object.fluids.get());
	}

	public final ItemStacksResourceHandler items = new FilteredItemStacksResourceHandler(1, (_, resource) -> resource.is(materialItems.VOIDIC_CRYSTAL));
	public final Supplier<FluidStacksResourceHandler> fluids = Suppliers.memoize(() -> new FluidStacksResourceHandler(NonNullList.of(
		new FluidStack(modFluids.VOIDIC_SOURCE.get(), 0)
	), 10000));

	private final BlockPosDirectionCapabilityCacher<ResourceHandler<FluidResource>> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int tick;
	private int processTick;

	public LiquifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.LIQUIFIER.get(), pPos, pBlockState);
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		if (level == null)
			return;
		Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), singleResourceCapabilityUtil.asItemStack(items));
		super.preRemoveSideEffects(pos, state);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		processTick = input.getIntOr("processTick", 0);
		items.deserialize(input);
		fluids.get().deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		items.serialize(output);
		fluids.get().serialize(output);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		tick++;
		if (singleResourceCapabilityUtil.amount(fluids) <= singleResourceCapabilityUtil.capacity(fluids) - 250 &&
			items.getResource(0).is(materialItems.VOIDIC_CRYSTAL.get())
		) {
			processTick++;
			if (processTick >= 80) {
				processTick = 0;
				transactionUtil.run(transaction -> {
					singleResourceCapabilityUtil.insert(fluids.get(), 250, transaction);
					singleResourceCapabilityUtil.extract(items, 1, transaction);
				});
				level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
						.filter(e -> e instanceof ServerPlayer)
						.map(ServerPlayer.class::cast)
						.forEach(advancementTriggers.LIQUIFIER_TRIGGER.get()::trigger);
			}
		} else {
			processTick = 0;
		}
		if (tick % 20 == 0 && singleResourceCapabilityUtil.amount(fluids) > 0 && level instanceof ServerLevel serverLevel) {
			for (Direction face : Direction.values()) {
				ResourceHandler<FluidResource> other = capabilityCache.get(Capabilities.Fluid.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					transactionUtil.run(transaction -> {
						int amount = other.insert(fluids.get().getResource(0), Math.min(singleResourceCapabilityUtil.amount(fluids), 1000), transaction);
						singleResourceCapabilityUtil.extract(fluids, amount, transaction);
					});
				}
				if (singleResourceCapabilityUtil.amount(fluids) <= 0)
					break;
			}
		}
	}

}
