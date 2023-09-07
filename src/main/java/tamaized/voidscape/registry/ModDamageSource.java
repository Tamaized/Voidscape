package tamaized.voidscape.registry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;

import java.util.function.Function;

public class ModDamageSource implements RegistryClass {

	public static boolean check(String id, DamageSource source) {
		return id.equals(source.getMsgId());
	}

	public static final String ID_VOIDIC = Voidscape.MODID + ".voidic";

//	public static final Function<LivingEntity, DamageSource> VOIDIC_WITH_ENTITY = attacker -> new EntityDamageSource(ID_VOIDIC, attacker).bypassArmor().setMagic();
//	public static final DamageSource VOIDIC = new DamageSource(ID_VOIDIC).bypassArmor().setMagic();

	@Override
	public void init(IEventBus bus) {

	}

}
