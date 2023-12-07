package tamaized.voidscape.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemMatchesAdvancementTrigger extends SimpleCriterionTrigger<ItemMatchesAdvancementTrigger.Instance> {

	private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
			ItemStack.SINGLE_ITEM_CODEC.fieldOf("item").forGetter(Instance::item)
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
