package tamaized.voidscape.turmoil.caps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;

public interface IAggroTable {

	ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "aggrotable");

	void tick(MobEntity entity);

	void addHate(LivingEntity attacker, double hate, boolean existing);

	void mulHate(LivingEntity attacker, double hate);

	void placeAtTop(LivingEntity entity);

	void remove(LivingEntity entity);

	boolean hasHate(LivingEntity entity);

}
