package tamaized.voidscape.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.item.util.UseItemActionContext;

import java.util.Objects;
import java.util.function.Consumer;

public class LingeringPotionAugmentableSpellTomeItem extends SpellTomeItem {

	public LingeringPotionAugmentableSpellTomeItem(Properties properties, int cooldown, Consumer<UseItemActionContext> action) {
		super(properties, cooldown, action);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			if (other.is(Items.LINGERING_POTION) && other.has(DataComponents.POTION_CONTENTS)) {
				stack.set(DataComponents.POTION_CONTENTS, other.get(DataComponents.POTION_CONTENTS));
				other.shrink(1);
				player.playSound(SoundEvents.BOTTLE_FILL, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
				return true;
			} else if (other.is(Items.SNOWBALL)) {
				stack.remove(DataComponents.POTION_CONTENTS);
				other.shrink(1);
				player.playSound(SoundEvents.BOTTLE_EMPTY, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
				return true;
			}
		}
		return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, display, builder, tooltipFlag);
		builder.accept(Component.empty());
		builder.accept(Component.translatable(Voidscape.MODID + ".tooltip.augment.lingering_potion")
			.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
		builder.accept(Component.empty());
		if (stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS) != PotionContents.EMPTY) {
			PotionContents.addPotionTooltip(
				Objects.requireNonNull(stack.get(DataComponents.POTION_CONTENTS)).getAllEffects(),
				builder,
				0.25F,
				context.tickRate()
			);
			builder.accept(Component.empty());
		}
	}
}
