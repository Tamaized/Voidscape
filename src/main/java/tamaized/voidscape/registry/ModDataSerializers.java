package tamaized.voidscape.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

public final class ModDataSerializers implements RegistryClass {

	private static final DeferredRegister<DataSerializerEntry> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, Voidscape.MODID);

	public static final EntityDataSerializer<Long> LONG = new EntityDataSerializer<>() {
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
	};

	@Override
	public void init(IEventBus bus) {
		REGISTRY.register("long", () -> new DataSerializerEntry(LONG));
	}

}
