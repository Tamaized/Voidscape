package tamaized.voidscape.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

import java.util.function.Supplier;

public final class ModDataSerializers implements RegistryClass {

	private static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = RegUtil.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS);

	public static final Supplier<EntityDataSerializer<Long>> LONG = REGISTRY.register("long", () -> new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, Long value) {
			buf.writeLong(value);
		}

		@Override
		public Long read(FriendlyByteBuf buf) {
			return buf.readLong();
		}

		@Override
		public Long copy(Long value) {
			return value;
		}
	});

	@Override
	public void init(IEventBus bus) {

	}

}
