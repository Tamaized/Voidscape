package tamaized.voidscape.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemMatchesAdvancementTrigger extends SimpleCriterionTrigger<ItemMatchesAdvancementTrigger.Instance> {

	private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
		ItemStack.CODEC.fieldOf("item").forGetter(Instance::item)
	).apply(instance, Instance::new));

	@Override
	public Codec<Instance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, instance -> instance.test(stack));
	}

	public record Instance(Optional<ContextAwarePredicate> player, ItemStack item) implements SimpleInstance {

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}

		public boolean test(ItemStack item) {
			return ItemStack.isSameItem(item, this.item);
		}
	}

}
