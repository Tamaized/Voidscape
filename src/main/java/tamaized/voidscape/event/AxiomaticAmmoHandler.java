package tamaized.voidscape.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModAttributes;

@Component
public class AxiomaticAmmoHandler {

	@Autowired
	private ModAttributes attributes;

	public int processAmmoUse(int ammoToUse, ItemStack weapon, LivingEntity holder) {
		if (ammoToUse > 0 && isCrossbow(weapon) && holder.getRandom().nextDouble() < holder.getAttributeValue(attributes.AXIOMATIC))
			return 0;
		return ammoToUse;
	}

	private boolean isCrossbow(ItemStack stack) {
		return stack.getItem() instanceof CrossbowItem || stack.is(Tags.Items.TOOLS_CROSSBOW);
	}

}
