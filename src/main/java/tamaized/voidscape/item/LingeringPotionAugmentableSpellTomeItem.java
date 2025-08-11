package tamaized.voidscape.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.item.util.UseItemActionContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LingeringPotionAugmentableSpellTomeItem extends SpellTomeItem {

	public LingeringPotionAugmentableSpellTomeItem(Properties properties, Supplier<Item> repairMaterial, int cooldown, Consumer<UseItemActionContext> action) {
		super(properties, repairMaterial, cooldown, action);
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

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		tooltipComponents.add(Component.empty());
		tooltipComponents.add(Component.translatable(Voidscape.MODID + ".tooltip.augment.lingering_potion")
			.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
		tooltipComponents.add(Component.empty());
		if (stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS) != PotionContents.EMPTY) {
			Objects.requireNonNull(stack.get(DataComponents.POTION_CONTENTS)).addPotionTooltip(tooltipComponents::add, 0.25F, context.tickRate());
			tooltipComponents.add(Component.empty());
		}
	}
}
