package tamaized.voidscape.entity;


import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class AntiBoltEntity extends LightningBolt {

	public AntiBoltEntity(EntityType<? extends AntiBoltEntity> entityType_, Level worldIn) {
		super(entityType_, worldIn);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
