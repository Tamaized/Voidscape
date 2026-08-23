package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.util.NamespaceUtils;

@Component
public class DamageTypeBootstrap implements IBootstrap {

	@Autowired
	private NamespaceUtils namespaceUtils;

	@Autowired
	private ModDamageSource damageTypes;

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.DAMAGE_TYPE, context -> {
			context.register(damageTypes.VOIDIC, new DamageType(namespaceUtils.dot(damageTypes.VOIDIC), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
		});
	}

}
