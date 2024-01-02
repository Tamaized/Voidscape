package tamaized.voidscape.entity;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class AntiBoltEntity extends LightningBolt {

	public AntiBoltEntity(EntityType<? extends AntiBoltEntity> entityType_, Level worldIn) {
		super(entityType_, worldIn);
	}

}
