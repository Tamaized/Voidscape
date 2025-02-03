package tamaized.voidscape.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.dimension.ConfigurablePortalShape;
import tamaized.voidscape.registry.block.FunctionalBlocks;
import tamaized.voidscape.util.PortalFramePredicates;

import java.util.Optional;

@Configurable
public class VoidPortalActivatorItem extends Item {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private PortalFramePredicates portalFramePredicates;

	@Autowired
	private FunctionalBlocks functionalBlocks;

	public VoidPortalActivatorItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockPos blockpos1 = blockpos.relative(context.getClickedFace());

		Optional<ConfigurablePortalShape> configurablePortalShape = ConfigurablePortalShape.findEmptyPortalShape(
			level,
			blockpos1,
			Direction.Axis.X,
			portalFramePredicates.FRAME_TEST,
			portalFramePredicates.PORTAL_TEST,
			portalFramePredicates.IGNITER_TEST
		);
		if (configurablePortalShape.isPresent()) {
			configurablePortalShape.get().createPortalBlocks(functionalBlocks.PORTAL.get().defaultBlockState(), PortalBlock.AXIS);
			level.playSound(player, blockpos1, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.BLOCKS, 1F, 0.75F + context.getLevel().getRandom().nextFloat() * 0.5F);
			ItemStack stack = context.getItemInHand();

			if (player instanceof ServerPlayer serverPlayer) {
				advancementTriggers.ACTIVATE_PORTAL_TRIGGER.get().trigger(serverPlayer);
				if (!serverPlayer.isCreative())
					stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

}
