package tamaized.voidscape.block.entity;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import tamaized.beanification.Autowired;
import tamaized.voidscape.capability.BlockPosDirectionCapabilityCacher;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.blockentity.ModBlockEntities;
import tamaized.voidscape.util.SingleResourceCapabilityUtil;
import tamaized.voidscape.util.TransactionUtil;

import java.util.function.Supplier;

public class WellBlockEntity extends TickableBlockEntity {

	@Autowired
	private static ModAdvancementTriggers advancementTriggers;

	@Autowired
	private static ModBlockEntities blockEntities;

	@Autowired
	private SingleResourceCapabilityUtil singleResourceCapabilityUtil;

	@Autowired
	private TransactionUtil transactionUtil;

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.Fluid.BLOCK, blockEntities.WELL.get(), (object, _) -> object.fluids.get());
	}

	public final Supplier<FluidStacksResourceHandler> fluids = Suppliers.memoize(() -> new FluidStacksResourceHandler(NonNullList.of(
		new FluidStack(Fluids.WATER, 0)
	), Integer.MAX_VALUE));

	private final BlockPosDirectionCapabilityCacher<ResourceHandler<FluidResource>> capabilityCache = new BlockPosDirectionCapabilityCacher<>();

	private int tick;

	public WellBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(blockEntities.WELL.get(), pPos, pBlockState);
	}

	@Override
	public void tick(Level level, BlockPos blockPos, BlockState blockState) {
		if (level.hasNeighborSignal(blockPos))
			return;
		if (singleResourceCapabilityUtil.amount(fluids) < Integer.MAX_VALUE)
			transactionUtil.execute(transaction -> singleResourceCapabilityUtil.insert(fluids, Integer.MAX_VALUE, transaction));
		tick++;
		if (level instanceof ServerLevel serverLevel) {
			boolean filled = false;
			for (Direction face : Direction.values()) {
				ResourceHandler<FluidResource> other = capabilityCache.get(Capabilities.Fluid.BLOCK, serverLevel, blockPos.relative(face), face.getOpposite());
				if (other != null) {
					if (transactionUtil.executeNegation(transaction -> other.insert(FluidResource.of(Fluids.WATER), Integer.MAX_VALUE, transaction), 0))
						filled = true;
				}
			}
			if (filled) {
				level.getEntities(null, new AABB(blockPos).inflate(8D)).stream()
						.filter(e -> e instanceof ServerPlayer)
						.map(ServerPlayer.class::cast)
						.forEach(advancementTriggers.WELL_TRIGGER.get()::trigger);
			}
		}
	}

}
