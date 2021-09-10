package tamaized.voidscape.turmoil.caps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import tamaized.voidscape.Voidscape;

public interface IAggroTable {

	ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "aggrotable");

	void tick(Mob entity);

	void addHate(LivingEntity attacker, double hate, boolean existing);

	void mulHate(LivingEntity attacker, double hate);

	void placeAtTop(LivingEntity entity);

	void remove(LivingEntity entity);

	boolean hasHate(LivingEntity entity);

}
