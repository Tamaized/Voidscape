package tamaized.voidscape.util.tool;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModAdvancementTriggers;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@Component
public class MultiBlockBreak {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	public final int THREE_BY_THREE_RADIUS = 1;
	public final int FIVE_BY_FIVE_RADIUS = 2;

	private final Map<UUID, Direction> LAST_HIT_BLOCK_FACE = new HashMap<>();
	private static boolean running = false;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void postConstruct(IEventBus bus) {
		bus.addListener(PlayerInteractEvent.LeftClickBlock.class, event -> LAST_HIT_BLOCK_FACE.put(event.getEntity().getUUID(), event.getFace()));
		bus.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> LAST_HIT_BLOCK_FACE.remove(event.getEntity().getUUID()));
	}

	public boolean doBreak(int radius, ItemStack stack, BlockPos pos, Player pl, BooleanSupplier delegate) {
		return doBreak(radius, stack, pos, pl, s -> false, delegate);
	}

	public synchronized boolean doBreak(int radius, ItemStack stack, BlockPos pos, Player pl, Predicate<ItemStack> shouldDelegate, BooleanSupplier delegate) {
		if (running || pl.level().isClientSide || !(pl instanceof final ServerPlayer player) || player.isShiftKeyDown() || shouldDelegate.test(stack))
			return delegate.getAsBoolean();
		final Item item = stack.getItem();
		final ServerLevel level = player.serverLevel();
		final BlockState oState = level.getBlockState(pos);
		if (!item.isCorrectToolForDrops(stack, oState))
			return false;
		final float hardness = oState.getDestroySpeed(level, pos);
		List<BlockPos> area = new ArrayList<>();
		switch (LAST_HIT_BLOCK_FACE.get(player.getUUID())) {
			case DOWN, UP -> {
				for (int x = -radius; x <= radius; x++)
					for (int z = -radius; z <= radius; z++)
						area.add(pos.offset(x, 0, z));
			}
			case EAST, WEST -> {
				for (int y = -radius; y <= radius; y++)
					for (int z = -radius; z <= radius; z++)
						area.add(pos.offset(0, y, z));
			}
			case NORTH, SOUTH -> {
				for (int y = -radius; y <= radius; y++)
					for (int x = -radius; x <= radius; x++)
						area.add(pos.offset(x, y, 0));
			}
			default -> area.add(pos);
		}
		if (area.size() > 1) // TODO: This should not be here
			advancementTriggers.THREE_BY_THREE.get().trigger(player, stack);
		// Using TCon's hardness division check
		running = true;
		area.stream().map(p -> Pair.of(p, level.getBlockState(p))).filter(p -> {
			final BlockState state = p.right();
			if (state.isAir())
				return false;
			final float h = state.getDestroySpeed(level, p.left());
			if (h < 0)
				return false;
			return (hardness == 0 ? h == 0 : h / hardness <= 3) && item.isCorrectToolForDrops(stack, state);
		}).forEach(p -> {
			final BlockPos blockPos = p.left();
			final BlockState state = p.right().getBlock().playerWillDestroy(level, blockPos, p.right(), player);
			BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(level, player.gameMode.getGameModeForPlayer(), player, blockPos, state);
			if (!event.isCanceled()) {
				if (player.isCreative()) {
					removeBlock(level, player, blockPos, false);
				} else {
					BlockEntity blockentity = level.getBlockEntity(blockPos);
					ItemStack cloneStack = stack.copy();
					boolean flag1 = state.canHarvestBlock(level, blockPos, player);
					stack.mineBlock(level, state, blockPos, player);
					if (stack.isEmpty() && !cloneStack.isEmpty())
						EventHooks.onPlayerDestroyItem(player, cloneStack, InteractionHand.MAIN_HAND);
					boolean flag = removeBlock(level, player, blockPos, flag1);

					if (flag && flag1) {
						state.getBlock().playerDestroy(level, player, blockPos, state, blockentity, cloneStack);
					}

					level.globalLevelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(state));
					player.connection.send(new ClientboundBlockUpdatePacket(level, blockPos));

				}
			}
		});
		running = false;
		return true;
	}

	private boolean removeBlock(Level level, Player player, BlockPos pos, boolean canHarvest) {
		BlockState state = level.getBlockState(pos);
		boolean removed = state.onDestroyedByPlayer(level, pos, player, canHarvest, level.getFluidState(pos));
		if (removed)
			state.getBlock().destroy(level, pos, state);
		return removed;
	}

}
