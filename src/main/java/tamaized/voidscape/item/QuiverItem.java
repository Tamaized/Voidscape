package tamaized.voidscape.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.data.QuiverContents;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.tooltip.QuiverTooltip;

import java.util.Optional;
import java.util.function.Consumer;

public class QuiverItem extends Item {

	private static final int BAR_COLOR = ARGB.colorFromFloat(1F, 0.4F, 0.4F, 1.0F);

	@Autowired
	private static ModItemComponents components;

	public QuiverItem(Properties properties) {
		super(properties.stacksTo(1).component(components.QUIVER_CONTENTS, QuiverContents.EMPTY));
	}

	public static float getFullnessDisplay(ItemStack stack) {
		QuiverContents contents = stack.getOrDefault(components.QUIVER_CONTENTS, QuiverContents.EMPTY);
		return contents.fullPercentage();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
			return false;
		} else {
			QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
			if (contents == null) {
				return false;
			} else {
				ItemStack itemstack = slot.getItem();
				QuiverContents.Mutable mutable = contents.toMutableCopy();
				if (itemstack.isEmpty()) {
					this.playRemoveOneSound(player);
					ItemStack itemstack1 = mutable.removeOneStack();
					if (!itemstack1.isEmpty()) {
						ItemStack itemstack2 = slot.safeInsert(itemstack1);
						mutable.tryInsert(itemstack2);
					}
				} else if (itemstack.canFitInsideContainerItems() && itemstack.is(ItemTags.ARROWS)) {
					ItemStack result = mutable.tryInsert(itemstack);
					if (result.getCount() != itemstack.getCount()) {
						slot.set(result);
						this.playInsertSound(player);
					}
				}

				stack.set(components.QUIVER_CONTENTS, mutable.toImmutable());
				return true;
			}
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(
		ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access
	) {
		if (!other.is(ItemTags.ARROWS))
			return false;
		if (stack.getCount() != 1) return false;
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
			if (contents == null) {
				return false;
			} else {
				QuiverContents.Mutable mutable = contents.toMutableCopy();
				if (other.isEmpty()) {
					ItemStack itemstack = mutable.removeOneStack();
					if (!itemstack.isEmpty()) {
						this.playRemoveOneSound(player);
						access.set(itemstack);
					}
				} else {
					ItemStack result = mutable.tryInsert(other);
					if (result.getCount() != other.getCount()) {
						other.setCount(result.getCount());
						this.playInsertSound(player);
					}
				}

				stack.set(components.QUIVER_CONTENTS, mutable.toImmutable());
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see {@link net.minecraft.world.item.Item#useOn(net.minecraft.world.item.context.UseOnContext)}.
	 */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
		ItemStack itemstack = player.getItemInHand(usedHand);
		if (dropContents(itemstack, player)) {
			this.playDropContentsSound(player);
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.FAIL;
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		QuiverContents contents = stack.getOrDefault(components.QUIVER_CONTENTS, QuiverContents.EMPTY);
		return contents.fullPercentage() > 0F;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		QuiverContents contents = stack.getOrDefault(components.QUIVER_CONTENTS, QuiverContents.EMPTY);
		return Math.min(1 + (int) (contents.fullPercentage() * 12F), 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	private static boolean dropContents(ItemStack stack, Player player) {
		QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
		if (contents != null && !contents.isEmpty()) {
			stack.set(components.QUIVER_CONTENTS, QuiverContents.EMPTY);
			if (player instanceof ServerPlayer) {
				contents.view().forEach(itemstack -> player.drop(itemstack, true));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		return !display.shows(components.QUIVER_CONTENTS.get())
			? Optional.empty()
			: Optional.ofNullable(stack.get(components.QUIVER_CONTENTS)).map(QuiverTooltip::new);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable(Voidscape.MODID + ".tooltip.quiver").withStyle(
			ChatFormatting.DARK_GRAY
		));
		/*QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
		if (contents != null) {
			int i = Mth.mulAndTruncate(contents.weight(), 64);
			tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", i, 64).withStyle(ChatFormatting.GRAY));
		}*/
	}

	@Override
	public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
		QuiverContents contents = itemEntity.getItem().get(components.QUIVER_CONTENTS);
		if (contents != null) {
			itemEntity.getItem().set(components.QUIVER_CONTENTS, QuiverContents.EMPTY);
			ItemUtils.onContainerDestroyed(itemEntity, contents.view().stream());
		}
	}

	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void playDropContentsSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
}
