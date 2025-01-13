package tamaized.voidscape.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;

@Component
public class BowFovModifierHandler {

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ComputeFovModifierEvent.class, event -> {
			ItemStack itemstack = event.getPlayer().getUseItem();
			if (event.getPlayer().isUsingItem()) {
				if (RegUtil.isMyBow(itemstack, Items.BOW)) {
					float f1 = (float) event.getPlayer().getTicksUsingItem() / 20.0F;
					f1 = f1 > 1.0F ? 1.0F : f1 * f1;
					event.setNewFovModifier((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0D, event.getFovModifier() * (1.0F - f1 * 0.15F)));
				}
			}
		});
	}

}
