package tamaized.voidscape.network.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ClientPacketSendParticles(List<QueuedParticle> queuedParticles) implements CustomPacketPayload {

	public static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "sendparticles");
    
    public ClientPacketSendParticles() {
		this(new ArrayList<>());
    }

	public ClientPacketSendParticles(FriendlyByteBuf packet) {
		this();
		int size = packet.readInt();
		for (int i = 0; i < size; i++) {
			Optional<Holder.Reference<ParticleType<?>>> type = BuiltInRegistries.PARTICLE_TYPE.getHolder(packet.readResourceKey(Registries.PARTICLE_TYPE));
			if (type.isEmpty())
				break; // Fail silently and end execution entirely. Due to Type serialization we now have completely unknown data in the pipeline without any way to safely read it all
			this.queuedParticles.add(new QueuedParticle(readParticle(type.get().value(), packet), packet.readBoolean(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble()));
		}
	}

    public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
    }

    public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 xyz, Vec3 xyz2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z));
    }

    @Override
    public void write(FriendlyByteBuf packet) {
        packet.writeInt(this.queuedParticles.size());
        for (QueuedParticle queuedParticle : this.queuedParticles) {
            packet.writeResourceKey(BuiltInRegistries.PARTICLE_TYPE.getResourceKey(queuedParticle.particleOptions.getType()).orElseThrow());
            queuedParticle.particleOptions.writeToNetwork(packet);
            packet.writeBoolean(queuedParticle.b);
            packet.writeDouble(queuedParticle.x);
            packet.writeDouble(queuedParticle.y);
            packet.writeDouble(queuedParticle.z);
            packet.writeDouble(queuedParticle.x2);
            packet.writeDouble(queuedParticle.y2);
            packet.writeDouble(queuedParticle.z2);
        }
    }

    private <T extends ParticleOptions> T readParticle(ParticleType<T> particleType, FriendlyByteBuf buf) {
        return particleType.getDeserializer().fromNetwork(particleType, buf);
    }

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(ClientPacketSendParticles payload, PlayPayloadContext context) {
		context.player().ifPresent(player -> {
			if (!(player.level() instanceof ClientLevel level))
				return;
			payload.queuedParticles.forEach(queuedParticle -> level.addParticle(queuedParticle.particleOptions, queuedParticle.b, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2));
		});
	}

    private record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2,
                                  double y2, double z2) {
    }
}
