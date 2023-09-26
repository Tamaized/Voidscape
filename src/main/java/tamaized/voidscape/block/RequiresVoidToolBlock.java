package tamaized.voidscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModTools;

public class RequiresVoidToolBlock extends Block {

	public static final TagKey<Block> NEEDS_VOIDIC_TOOL = TagKey.create(Registries.BLOCK, new ResourceLocation(Voidscape.MODID, "needs_voidic_tool"));
	public static final TagKey<Block> NEEDS_CORRUPT_TOOL = TagKey.create(Registries.BLOCK, new ResourceLocation(Voidscape.MODID, "needs_corrupt_tool"));
	public static final TagKey<Block> NEEDS_TITANITE_TOOL = TagKey.create(Registries.BLOCK, new ResourceLocation(Voidscape.MODID, "needs_titanite_tool"));

	public RequiresVoidToolBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	@Deprecated
	public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
		float f = pState.getDestroySpeed(pLevel, pPos);
		if (f == -1.0F) {
			return 0.0F;
		} else {
			int i = canHarvestBlock(pState, pLevel, pPos, pPlayer) ? 30 : 100;
			return pPlayer.getDigSpeed(pState, pPos) / f / (float)i;
		}
	}

	@Override
	@Deprecated // No I don't think I will use TierSortingRegistry, I only want to allow MY tools!
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		Tier tier = player.getMainHandItem().isEmpty() ? null : player.getMainHandItem().getItem() instanceof TieredItem tieredItem ? tieredItem.getTier() : null;
		if (tier != null && ModTools.ItemTier.check(tier)) {
			if (state.is(NEEDS_VOIDIC_TOOL) && tier.getLevel() >= ModTools.ItemTier.VOIDIC_CRYSTAL.getLevel()) {
				return true;
			} else if (state.is(NEEDS_CORRUPT_TOOL) && tier.getLevel() >= ModTools.ItemTier.CORRUPT.getLevel()) {
				return true;
			} else if (state.is(NEEDS_TITANITE_TOOL) && tier.getLevel() >= ModTools.ItemTier.TITANITE.getLevel()) {
				return true;
			}
		}
		return false;
	}
}
