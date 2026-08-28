package tamaized.voidscape.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.ToolAndArmorHelper;

@Component(dist = Dist.CLIENT)
public class HandRenderSelectionHandler {

	@Autowired(dist = Dist.CLIENT)
	private ToolAndArmorHelper toolAndArmorHelper;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(RenderHandEvent.class, event -> {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && !shouldRenderHand(player, event.getHand()))
				event.setCanceled(true);
		});
	}

	private boolean shouldRenderHand(LocalPlayer player, InteractionHand hand) {
		ItemStack mainHandItem = player.getMainHandItem();
		ItemStack offhandItem = player.getOffhandItem();
		boolean holdsBow = isBowLike(mainHandItem, Items.BOW) || isBowLike(offhandItem, Items.BOW);
		boolean holdsCrossbow = isBowLike(mainHandItem, Items.CROSSBOW) || isBowLike(offhandItem, Items.CROSSBOW);
		if (!holdsBow && !holdsCrossbow)
			return true;
		if (player.isUsingItem())
			return shouldRenderHandUsingItemWhileHoldingBowLike(player, hand);
		return hand == InteractionHand.MAIN_HAND || isUnchargedCrossbow(mainHandItem);
	}

	private boolean shouldRenderHandUsingItemWhileHoldingBowLike(LocalPlayer player, InteractionHand hand) {
		ItemStack usedItemStack = player.getUseItem();
		InteractionHand usedHand = player.getUsedItemHand();
		if (isBowLike(usedItemStack, Items.BOW) || isBowLike(usedItemStack, Items.CROSSBOW))
			return hand == usedHand;
		return hand == InteractionHand.MAIN_HAND || usedHand != InteractionHand.MAIN_HAND || isUnchargedCrossbow(player.getOffhandItem());
	}

	private boolean isUnchargedCrossbow(ItemStack stack) {
		return !isBowLike(stack, Items.CROSSBOW) || !CrossbowItem.isCharged(stack);
	}

	private boolean isBowLike(ItemStack stack, Item item) {
		return stack.is(item) || toolAndArmorHelper.isMyBow(stack, item);
	}

}
