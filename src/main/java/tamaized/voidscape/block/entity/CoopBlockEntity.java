package tamaized.voidscape.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.beanification.BeanContext;
import tamaized.beanification.Configurable;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.capability.FilteredFluidStacksResourceHandler;
import tamaized.voidscape.capability.FilteredItemStacksResourceHandler;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.registry.fluid.ModFluids;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

@Configurable
public class CoopBlockEntity extends TickableBlockEntity {

	private static final Lazy<ModBlockEntities> blockEntities = BeanContext.injectLazy(ModBlockEntities.class);

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private ModFluids modFluids;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Item.BLOCK, blockEntities.get().COOP.get(), (object, _) -> object.items);
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.get().COOP.get(), (object, _) -> object.fluids);
	}

	public final ItemStacksResourceHandler items = new FilteredItemStacksResourceHandler(1, (_, resource) -> resource.is(Items.EGG));
	public final FluidStacksResourceHandler fluids = new FilteredFluidStacksResourceHandler(1, 10000, (_, resource) -> resource.is(modFluids.VOIDIC_SOURCE.get()));

	private final BlockPosDirectionCapabilityCacher<ResourceHandler<ItemResource>> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int processTick;

	public CoopBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.get().COOP.get(), pPos, pBlockState);
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
		fluids.deserialize(input);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("processTick", processTick);
		items.serialize(output);
		fluids.serialize(output);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		if (processTick <= 0 && singleResourceCapabilityUtil.amount(fluids) > 0) {
			if (transactionUtil.executeNegation(transaction -> singleResourceCapabilityUtil.extract(fluids, 1, transaction), 0))
				processTick = 60 + level.getRandom().nextInt(140);
		} else if (processTick > 0) {
			processTick--;
			if (processTick <= 0) {
				if (transactionUtil.executeComparing(transaction -> singleResourceCapabilityUtil.insert(items, ItemResource.of(Items.EGG), 1, transaction), 0))
					processTick = 200;
				else {
					level.getEntities((Entity) null, new AABB(blockPos).inflate(6D), e -> e instanceof ServerPlayer player && !player.isSpectator()).stream()
						.map(ServerPlayer.class::cast)
						.forEach(advancementTriggers.COOP_TRIGGER.get()::trigger);
				}
			}
		}
		if (singleResourceCapabilityUtil.amount(items) > 0 && level instanceof ServerLevel serverLevel) {
			for (Direction face : Direction.values()) {
				ResourceHandler<ItemResource> other = capabilityCache.get(Capabilities.Item.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					int count = singleResourceCapabilityUtil.amount(items);
					if (transactionUtil.executeNegation(transaction -> singleResourceCapabilityUtil.extract(
						items,
						other.insert(singleResourceCapabilityUtil.resource(items), count, transaction),
						transaction
					), 0)) {
						continue;
					}
					break;
				}
			}
		}
	}

}
