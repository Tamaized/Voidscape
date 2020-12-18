package tamaized.voidscape.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import tamaized.voidscape.Voidscape;

import java.util.function.Function;

public class ModDamageSource {

	public static final Function<LivingEntity, DamageSource> VOIDIC = attacker -> new EntityDamageSource(Voidscape.MODID + ".voidic", attacker).bypassArmor().setMagic();

}
