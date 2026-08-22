package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModDamageSource;

@Component
public class NoKnockbackDamageTypeTagProviderFactory implements IDamageTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModDamageSource damageSource;

	@Override
	public void make(ExposedKeyTagProvider<DamageType> accessor, HolderLookup.Provider provider) {
		accessor.tag(DamageTypeTags.NO_KNOCKBACK).add(
			damageSource.VOIDIC
		);
	}
}
