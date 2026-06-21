package tamaized.voidscape.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GenericAdvancementTrigger extends SimpleCriterionTrigger<GenericAdvancementTrigger.Instance> {

	private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player)
	).apply(instance, Instance::new));

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	@Override
	public Codec<Instance> codec() {
		return CODEC;
	}

	public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}
	}

}
