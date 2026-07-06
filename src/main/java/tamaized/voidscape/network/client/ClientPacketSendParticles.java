package tamaized.voidscape.network.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;

public record ClientPacketSendParticles(List<QueuedParticle> queuedParticles) implements CustomPacketPayload {

	public static final Type<ClientPacketSendParticles> ID = new Type<>(Identifier.fromNamespaceAndPath(Voidscape.MODID, "s2c_send_particles"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientPacketSendParticles> CODEC = StreamCodec.ofMember(ClientPacketSendParticles::write, ClientPacketSendParticles::new);

	public ClientPacketSendParticles() {
		this(new ArrayList<>());
	}

	public ClientPacketSendParticles(RegistryFriendlyByteBuf packet) {
		this();
		int size = packet.readInt();
		for (int i = 0; i < size; i++) {
			this.queuedParticles.add(new QueuedParticle(ParticleTypes.STREAM_CODEC.decode(packet), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble()));
		}
	}

	public void write(RegistryFriendlyByteBuf packet) {
		packet.writeInt(this.queuedParticles.size());
		for (QueuedParticle queuedParticle : this.queuedParticles) {
			ParticleTypes.STREAM_CODEC.encode(packet, queuedParticle.particleOptions);
			packet.writeDouble(queuedParticle.x);
			packet.writeDouble(queuedParticle.y);
			packet.writeDouble(queuedParticle.z);
			packet.writeDouble(queuedParticle.x2);
			packet.writeDouble(queuedParticle.y2);
			packet.writeDouble(queuedParticle.z2);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public void queueParticle(ParticleOptions particleOptions, double x, double y, double z, double x2, double y2, double z2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, x, y, z, x2, y2, z2));
	}

	public void queueParticle(ParticleOptions particleOptions, Vec3 xyz, Vec3 xyz2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z));
	}

	public static void handle(ClientPacketSendParticles payload, IPayloadContext context) {
		if (!(context.player().level() instanceof ClientLevel level))
			return;
		context.enqueueWork(() -> payload.queuedParticles.forEach(queuedParticle -> level.addParticle(queuedParticle.particleOptions, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2)));
	}

	private record QueuedParticle(ParticleOptions particleOptions, double x, double y, double z, double x2, double y2, double z2) {
	}
}
