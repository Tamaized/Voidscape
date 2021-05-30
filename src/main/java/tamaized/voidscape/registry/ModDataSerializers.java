package tamaized.voidscape.registry;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModDataSerializers {

	private static final DeferredRegister<DataSerializerEntry> REGISTRY = RegUtil.create(ForgeRegistries.DATA_SERIALIZERS);

	public static final IDataSerializer<Long> LONG = new IDataSerializer<Long>() {
		@Override
		public void write(PacketBuffer buf, Long value) {
			buf.writeLong(value);
		}

		@Override
		public Long read(PacketBuffer buf) {
			return buf.readLong();
		}

		@Override
		public Long copy(Long value) {
			return value;
		}
	};

	private ModDataSerializers() {

	}

	static void classload() {
		REGISTRY.register("long", () -> new DataSerializerEntry(LONG));
	}

}
