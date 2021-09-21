package tamaized.voidscape.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import tamaized.voidscape.registry.ModEntities;

public class EntityNullServant extends Mob {

	public EntityNullServant(Level level) {
		this(ModEntities.NULL_SERVANT.get(), level);
	}

	public EntityNullServant(EntityType<? extends EntityNullServant> type, Level level) {
		super(type, level);
	}

}
