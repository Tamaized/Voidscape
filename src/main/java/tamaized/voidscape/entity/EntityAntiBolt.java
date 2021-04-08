package tamaized.voidscape.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityAntiBolt extends LightningBoltEntity {

	public EntityAntiBolt(EntityType<? extends EntityAntiBolt> entityType_, World worldIn) {
		super(entityType_, worldIn);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
