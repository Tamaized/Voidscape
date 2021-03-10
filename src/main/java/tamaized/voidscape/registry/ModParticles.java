package tamaized.voidscape.registry;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.particle.ParticleSpellCloud;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {

	private static final DeferredRegister<ParticleType<?>> REGISTRY = RegUtil.create(ForgeRegistries.PARTICLE_TYPES);

	public static final RegistryObject<ParticleType<ParticleSpellCloudData>> SPELL_CLOUD = REGISTRY.register("spell_cloud", () -> new ParticleType<ParticleSpellCloudData>(false, new ParticleSpellCloudData.Deserializer()) {
		@Override
		public Codec<ParticleSpellCloudData> codec() {
			return ParticleSpellCloudData.codec();
		}
	});

	static void classload() {

	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent event) {
		ParticleManager particles = Minecraft.getInstance().particleEngine;

		particles.register(ModParticles.SPELL_CLOUD.get(), ParticleSpellCloud.Factory::new);
	}

	public static class ParticleSpellCloudData implements IParticleData {
		public final int r;
		public final int g;
		public final int b;

		public ParticleSpellCloudData(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Nonnull
		@Override
		public ParticleType<?> getType() {
			return ModParticles.SPELL_CLOUD.get();
		}

		public static Codec<ParticleSpellCloudData> codec() {
			return RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("r").forGetter((obj) -> obj.r), Codec.INT.fieldOf("g").forGetter((obj) -> obj.g), Codec.INT.fieldOf("b").forGetter((obj) -> obj.b)).apply(instance, ParticleSpellCloudData::new));
		}

		@Override
		public void writeToNetwork(@Nonnull PacketBuffer buf) {
			buf.writeVarInt(r);
			buf.writeVarInt(g);
			buf.writeVarInt(b);
		}

		@Nonnull
		@Override
		public String writeToString() {
			return String.format("%d %d %d", r, g, b);
		}

		public static class Deserializer implements IParticleData.IDeserializer<ParticleSpellCloudData> {
			@Nonnull
			@Override
			public ParticleSpellCloudData fromCommand(@Nonnull ParticleType<ParticleSpellCloudData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
				reader.skipWhitespace();
				int r = reader.readInt();
				reader.skipWhitespace();
				int g = reader.readInt();
				reader.skipWhitespace();
				int b = reader.readInt();
				return new ParticleSpellCloudData(r, g, b);
			}

			@Nonnull
			@Override
			public ParticleSpellCloudData fromNetwork(@Nonnull ParticleType<ParticleSpellCloudData> type, PacketBuffer buf) {
				return new ParticleSpellCloudData(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
			}
		}
	}
}
