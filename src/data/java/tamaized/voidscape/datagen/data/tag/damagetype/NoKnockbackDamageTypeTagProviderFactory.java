package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.DamageTypeTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModDamageSource;

@Component
public class NoKnockbackDamageTypeTagProviderFactory implements IDamageTypeTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModDamageSource damageSource;

	@Override
	public void make(DamageTypeTagProviderFactory.DamageTypeTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(DamageTypeTags.NO_KNOCKBACK).add(
			damageSource.VOIDIC
		);
	}
}
