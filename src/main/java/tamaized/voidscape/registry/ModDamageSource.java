package tamaized.voidscape.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import tamaized.voidscape.Voidscape;

import java.util.function.Function;

public class ModDamageSource {

	public static boolean check(String id, DamageSource source) {
		return id.equals(source.getMsgId());
	}

	public static final String ID_VOIDIC = Voidscape.MODID + ".voidic";

	public static final Function<LivingEntity, DamageSource> VOIDIC_WITH_ENTITY = attacker -> new EntityDamageSource(ID_VOIDIC, attacker).bypassArmor().setMagic();
	public static final DamageSource VOIDIC = new DamageSource(ID_VOIDIC).bypassArmor().setMagic();

}
