package tamaized.voidscape.item;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.advancement.GenericAdvancementTrigger;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.util.LevelUtil;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Configurable
public class BlockTransformerItem extends Item {

	@Autowired
	private LevelUtil levelUtil;

	private final Predicate<BlockState> from;
	private final Supplier<BlockState> to;
	private final @Nullable Supplier<GenericAdvancementTrigger> advancement;
	private final int particleCount;
	private final Supplier<ParticleOptions> particle;
	private final SoundEffect soundEffect;

	private BlockTransformerItem(
		Predicate<BlockState> from,
		Supplier<BlockState> to,
		@Nullable Supplier<GenericAdvancementTrigger> advancement,
		int particleCount,
		Supplier<ParticleOptions> particle,
		SoundEffect soundEffect,
		Properties properties
	) {
		super(properties);
		this.from = from;
		this.to = to;
		this.advancement = advancement;
		this.particleCount = particleCount;
		this.particle = particle;
		this.soundEffect = soundEffect;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (levelUtil.isInVoidDimension(context.getLevel()) && from.test(context.getLevel().getBlockState(context.getClickedPos()))) {
			context.getLevel().setBlockAndUpdate(context.getClickedPos(), to.get());
			if (context.getPlayer() == null || !context.getPlayer().isCreative())
				context.getItemInHand().shrink(1);
			context.getLevel().playSound(
				null,
				context.getClickedPos(),
				soundEffect.sound().get(),
				soundEffect.source(),
				soundEffect.volume(),
				soundEffect.pitch().apply(context.getLevel().getRandom().nextFloat())
			);
			if (context.getLevel() instanceof ServerLevel level) {
				ClientPacketSendParticles particles = new ClientPacketSendParticles();
				for (int i = 0; i < particleCount; i++)
					particles.queueParticle(
						particle.get(),
						context.getClickedPos().getX() + context.getLevel().getRandom().nextFloat(),
						context.getClickedPos().getY() + context.getLevel().getRandom().nextFloat(),
						context.getClickedPos().getZ() + context.getLevel().getRandom().nextFloat(),
						0, 0, 0);
				PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(context.getClickedPos()), particles);
			}
			if (advancement != null && context.getPlayer() instanceof ServerPlayer serverPlayer)
				advancement.get().trigger(serverPlayer);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

	public static class Builder {

		private final Predicate<BlockState> from;
		private final Supplier<BlockState> to;
		private @Nullable Supplier<GenericAdvancementTrigger> advancement;
		private int particleCount = 50;
		private Supplier<ParticleOptions> particle = () -> ParticleTypes.WITCH;
		private SoundEffect soundEffect = new SoundEffect(() -> SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1F, rand -> 0.5F + rand * 0.5F);

		public Builder(Predicate<BlockState> from, Supplier<BlockState> to) {
			this.from = from;
			this.to = to;
		}

		public Builder advancement(Supplier<GenericAdvancementTrigger> advancement) {
			this.advancement = advancement;
			return this;
		}

		public Builder particleCount(int count) {
			this.particleCount = count;
			return this;
		}

		public Builder particle(Supplier<ParticleOptions> particle) {
			this.particle = particle;
			return this;
		}

		public Builder sound(Supplier<SoundEvent> sound) {
			this.soundEffect = this.soundEffect.with(sound);
			return this;
		}

		public Builder soundSource(SoundSource source) {
			this.soundEffect = this.soundEffect.with(source);
			return this;
		}

		public Builder soundVolume(float volume) {
			this.soundEffect = this.soundEffect.with(volume);
			return this;
		}

		public Builder soundPitch(Function<Float, Float> pitch) {
			this.soundEffect = this.soundEffect.with(pitch);
			return this;
		}

		public BlockTransformerItem build(Properties properties) {
			return new BlockTransformerItem(from, to, advancement, particleCount, particle, soundEffect, properties);
		}

	}

	public record SoundEffect(Supplier<SoundEvent> sound, SoundSource source, float volume, Function<Float, Float> pitch) {

		public SoundEffect with(Supplier<SoundEvent> sound) {
			return new SoundEffect(sound, source, volume, pitch);
		}

		public SoundEffect with(SoundSource source) {
			return new SoundEffect(sound, source, volume, pitch);
		}

		public SoundEffect with(float volume) {
			return new SoundEffect(sound, source, volume, pitch);
		}

		public SoundEffect with(Function<Float, Float> pitch) {
			return new SoundEffect(sound, source, volume, pitch);
		}

	}

}
