package tamaized.voidscape.registry;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.client.particle.ParticleSpellCloud;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ModParticles implements RegistryClass {

	private static final DeferredRegister<ParticleType<?>> REGISTRY = RegUtil.create(ForgeRegistries.PARTICLE_TYPES);

	public static final RegistryObject<ParticleType<ParticleSpellCloudData>> SPELL_CLOUD = REGISTRY.register("spell_cloud", () -> new ParticleType<>(false, new ParticleSpellCloudData.Deserializer()) {
		@Override
		public Codec<ParticleSpellCloudData> codec() {
			return ParticleSpellCloudData.codec();
		}
	});

	@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"}) // Classloading issues
	@Override
	public void init(IEventBus bus) {
		bus.addListener((Consumer<RegisterParticleProvidersEvent>) event -> {
			event.registerSpriteSet(ModParticles.SPELL_CLOUD.get(), new ParticleEngine.SpriteParticleRegistration<>() {
				@Override
				public ParticleProvider<ParticleSpellCloudData> create(SpriteSet s) {
					return new ParticleSpellCloud.Factory(s);
				}
			});
		});
	}

	public record ParticleSpellCloudData(int color) implements ParticleOptions {

		@Nonnull
		@Override
		public ParticleType<?> getType() {
			return ModParticles.SPELL_CLOUD.get();
		}

		public static Codec<ParticleSpellCloudData> codec() {
			return RecordCodecBuilder.create((instance) -> instance.group(
					Codec.INT.fieldOf("color").forGetter((obj) -> obj.color)
			).apply(instance, ParticleSpellCloudData::new));
		}

		@Override
		public void writeToNetwork(@Nonnull FriendlyByteBuf buf) {
			buf.writeInt(color);
		}

		@Nonnull
		@Override
		public String writeToString() {
			return String.format("%d", color);
		}

		public static class Deserializer implements ParticleOptions.Deserializer<ParticleSpellCloudData> {
			@Nonnull
			@Override
			public ParticleSpellCloudData fromCommand(@Nonnull ParticleType<ParticleSpellCloudData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
				reader.skipWhitespace();
				int color = reader.readInt();
				return new ParticleSpellCloudData(color);
			}

			@Nonnull
			@Override
			public ParticleSpellCloudData fromNetwork(@Nonnull ParticleType<ParticleSpellCloudData> type, FriendlyByteBuf buf) {
				return new ParticleSpellCloudData(buf.readInt());
			}
		}
	}
}
