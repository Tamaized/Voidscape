package tamaized.voidscape.network.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.network.NetworkMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientPacketSendParticles implements NetworkMessages.IMessage<ClientPacketSendParticles> {
    
    private final List<QueuedParticle> queuedParticles = new ArrayList<>();

    public ClientPacketSendParticles() {

    }

    public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
    }

    public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 xyz, Vec3 xyz2) {
        this.queuedParticles.add(new QueuedParticle(particleOptions, b, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z));
    }

    @Override
    public void handle(@Nullable Player player) {
        if (player == null || !(player.level() instanceof ClientLevel level))
            return;
        queuedParticles.forEach(queuedParticle -> level.addParticle(queuedParticle.particleOptions, queuedParticle.b, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2));
    }

    @Override
    public void toBytes(FriendlyByteBuf packet) {
        packet.writeInt(this.queuedParticles.size());
        for (QueuedParticle queuedParticle : this.queuedParticles) {
            packet.writeResourceKey(ForgeRegistries.PARTICLE_TYPES.getResourceKey(queuedParticle.particleOptions.getType()).orElseThrow());
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

    @Override
    public ClientPacketSendParticles fromBytes(FriendlyByteBuf packet) {
        int size = packet.readInt();
        for (int i = 0; i < size; i++) {
            Optional<Holder<ParticleType<?>>> type = ForgeRegistries.PARTICLE_TYPES.getHolder(packet.readResourceKey(ForgeRegistries.PARTICLE_TYPES.getRegistryKey()));
            if (type.isEmpty())
                break; // Fail silently and end execution entirely. Due to Type serialization we now have completely unknown data in the pipeline without any way to safely read it all
            this.queuedParticles.add(new QueuedParticle(readParticle(type.get().get(), packet), packet.readBoolean(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble(), packet.readDouble()));
        }
        return this;
    }

    private <T extends ParticleOptions> T readParticle(ParticleType<T> particleType, FriendlyByteBuf buf) {
        return particleType.getDeserializer().fromNetwork(particleType, buf);
    }

    private record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2,
                                  double y2, double z2) {
    }
}
