package tamaized.voidscape.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.regutil.RegistryClass;

public final class ModDataSerializers implements RegistryClass {

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
		EntityDataSerializers.registerSerializer(LONG);
	}

}
