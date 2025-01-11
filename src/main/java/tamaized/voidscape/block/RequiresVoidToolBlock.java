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
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.tool.ModToolTiers;

@Configurable
public class RequiresVoidToolBlock extends Block {

	public static final TagKey<Block> NEEDS_VOIDIC_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "needs_voidic_tool"));
	public static final TagKey<Block> NEEDS_CORRUPT_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "needs_corrupt_tool"));
	public static final TagKey<Block> NEEDS_TITANITE_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "needs_titanite_tool"));
	public static final TagKey<Block> NEEDS_ICHOR_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "needs_ichor_tool"));
	public static final TagKey<Block> NEEDS_ASTRAL_TOOL = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "needs_astral_tool"));

	@Autowired
	private ModToolTiers toolTiers;

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
			int i = canHarvestBlock(pState, pLevel, pPos, pPlayer) ? 30 : 1000;
			return pPlayer.getDigSpeed(pState, pPos) / f / (float)i;
		}
	}

	@Override
	@Deprecated // No I don't think I will use TierSortingRegistry, I only want to allow MY tools!
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		Tier tier = player.getMainHandItem().isEmpty() ? null : player.getMainHandItem().getItem() instanceof TieredItem tieredItem ? tieredItem.getTier() : null;
		if (tier != null) {
			int tierLevel = tierLevel(tier);
			if (tierLevel < 0)
				return false;
			if (state.is(NEEDS_VOIDIC_TOOL)) {
				return true;
			} else if (state.is(NEEDS_CORRUPT_TOOL) && tierLevel >= 2) {
				return true;
			} else if (state.is(NEEDS_TITANITE_TOOL) && tierLevel >= 3) {
				return true;
			} else if (state.is(NEEDS_ICHOR_TOOL) && tierLevel >= 4) {
				return true;
			} else if (state.is(NEEDS_ASTRAL_TOOL) && tierLevel >= 5) {
				return true;
			}
		}
		return false;
	}

	private int tierLevel(Tier tier) {
		if (tier == toolTiers.VOIDIC_CRYSTAL)
			return 0;
		if (tier == toolTiers.CHARRED)
			return 1;
		if (tier == toolTiers.CORRUPT)
			return 2;
		if (tier == toolTiers.TITANITE)
			return 3;
		if (tier == toolTiers.ICHOR)
			return 4;
		if (tier == toolTiers.ASTRAL)
			return 5;
		return -1;
	}
}
