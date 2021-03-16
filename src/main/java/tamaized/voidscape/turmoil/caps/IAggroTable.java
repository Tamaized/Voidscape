package tamaized.voidscape.turmoil.caps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

public interface IAggroTable {

	void tick(MobEntity entity);

	void addHate(LivingEntity attacker, double hate);

	void placeAtTop(LivingEntity entity);

	boolean hasHate(LivingEntity entity);

}
