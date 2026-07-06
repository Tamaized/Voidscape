package tamaized.voidscape.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import tamaized.beanification.Autowired;
import tamaized.voidscape.registry.ModParticles;

public class ParticleTypeSpellCloud extends ParticleType<ParticleTypeSpellCloud.Options> {

	public ParticleTypeSpellCloud() {
		super(false);
	}

	@Override
	public MapCodec<Options> codec() {
		return Options.CODEC;
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, Options> streamCodec() {
		return Options.STREAM_CODEC;
	}

	public record Options(int color) implements ParticleOptions {

		public static MapCodec<Options> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
				Codec.INT.fieldOf("color").forGetter((obj) -> obj.color)
			).apply(instance, Options::new)
		);

		public static StreamCodec<? super RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.of(
			(buf, o) -> buf.writeInt(o.color),
			buf -> new Options(buf.readInt())
		);

		@Autowired
		private static ModParticles particles;

		@Override
		public ParticleType<?> getType() {
			return particles.SPELL_CLOUD.get();
		}

	}

}
