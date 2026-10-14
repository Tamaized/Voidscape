package tamaized.voidscape.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AxiomBurst(long activeUntil, long cooldownUntil) {

	public static final Codec<AxiomBurst> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("active_until").forGetter(AxiomBurst::activeUntil),
		Codec.LONG.fieldOf("cooldown_until").forGetter(AxiomBurst::cooldownUntil)
	).apply(instance, AxiomBurst::new));

	public static final StreamCodec<ByteBuf, AxiomBurst> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_LONG,
		AxiomBurst::activeUntil,
		ByteBufCodecs.VAR_LONG,
		AxiomBurst::cooldownUntil,
		AxiomBurst::new
	);

	public static final AxiomBurst EMPTY = new AxiomBurst(0L, 0L);

	public boolean isStarted() {
		return activeUntil > 0L;
	}

	public boolean isActive(long gameTime) {
		return gameTime < activeUntil;
	}

	public boolean isCoolingDown(long gameTime) {
		return gameTime < cooldownUntil;
	}

}
