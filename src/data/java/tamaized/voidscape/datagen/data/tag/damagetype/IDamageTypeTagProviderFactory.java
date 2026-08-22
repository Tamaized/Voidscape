package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.damagesource.DamageType;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;

public interface IDamageTypeTagProviderFactory {

	void make(ExposedKeyTagProvider<DamageType> accessor, HolderLookup.Provider provider);

}
