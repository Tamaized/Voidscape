package tamaized.voidscape.registry;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;

public final class ModDataSerializers {

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

	static {
		DataSerializers.registerSerializer(LONG);
	}

	private ModDataSerializers() {

	}

}
