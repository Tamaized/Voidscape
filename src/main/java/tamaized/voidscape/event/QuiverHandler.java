package tamaized.voidscape.event;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.data.QuiverContents;
import tamaized.voidscape.item.QuiverItem;
import tamaized.voidscape.model.QuiverData;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModItemComponents;

import java.util.Optional;

@Component
public class QuiverHandler {

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModItemComponents components;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(LivingGetProjectileEvent.class, event -> {
			if (event.getEntity() instanceof Player player) {
				player.getInventory().contains(stack -> {
					if (stack.getItem() instanceof QuiverItem) {
						QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
						if (contents == null) {
							player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData());
							return false;
						}
						Optional<ItemStack> arrow = contents.view().stream()
							.filter(s -> s.is(ItemTags.ARROWS))
							.findFirst();
						if (arrow.isEmpty()) {
							player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData());
							return false;
						}
						player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData(stack, arrow.get()));
						event.setProjectileItemStack(arrow.get().copy());
						return true;
					}
					player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData());
					return false;
				});
			}
		});
	}

	public ItemStack useAmmo(ItemStack result, ItemStack bow, ItemStack ammo, LivingEntity shooter) {
		if (shooter instanceof Player player) {
			QuiverData data = player.getData(dataAttachments.QUIVER_NOCKED);
			if (data.quiver().isEmpty() || data.arrow().isEmpty())
				return result;
			QuiverContents contents = data.quiver().get(components.QUIVER_CONTENTS);
			if (contents == null)
				return result;
			if (!ItemStack.isSameItemSameComponents(ammo, data.arrow())) {
				if (ammo.isEmpty() && data.arrow().getCount() == 1) {
					QuiverContents.Mutable mutable = contents.toMutableCopy();
					mutable.shrinkFirstStack(1);
					data.quiver().set(components.QUIVER_CONTENTS, mutable.toImmutable());
					player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData());
				}
				return result;
			}
			int targetSlot = -1;
			for (int i = 0; i < contents.view().size(); i++) {
				ItemStack slot = contents.view().get(i);
				if (ItemStack.matches(slot, data.arrow())) {
					targetSlot = i;
					break;
				}
			}
			if (targetSlot >= 0) {
				QuiverContents.Mutable mutable = contents.toMutableCopy();
				mutable.set(targetSlot, ammo);
				data.quiver().set(components.QUIVER_CONTENTS, mutable.toImmutable());
			}
			player.setData(dataAttachments.QUIVER_NOCKED, new QuiverData());
		}
		return result;
	}

}
