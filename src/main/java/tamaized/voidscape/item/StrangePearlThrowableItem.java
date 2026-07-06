package tamaized.voidscape.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tamaized.voidscape.entity.StrangePearlEntity;

public class StrangePearlThrowableItem extends Item {

	public StrangePearlThrowableItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide()) {
			StrangePearlEntity pearl = new StrangePearlEntity(pLevel, pPlayer);
			pearl.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(pearl);
		}

		pPlayer.awardStat(Stats.ITEM_USED.get(this));
		if (!pPlayer.isCreative()) {
			itemstack.shrink(1);
		}

		return InteractionResult.SUCCESS;
	}
}
