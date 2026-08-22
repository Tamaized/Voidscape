package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModDamageSource;

@Component
public class AvoidsGuardianThornsDamageTypeTagProviderFactory implements IDamageTypeTagProviderFactory {

	@Autowired
	private ModDamageSource damageSource;

	@Override
	public void make(ExposedKeyTagProvider<DamageType> accessor, HolderLookup.Provider provider) {
		accessor.tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add(
			damageSource.VOIDIC
		);
	}
}
