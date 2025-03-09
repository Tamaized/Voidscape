package tamaized.voidscape.datagen.data.tag.damagetype;

import net.minecraft.core.HolderLookup;

public interface IDamageTypeTagProviderFactory {

	void make(DamageTypeTagProviderFactory.DamageTypeTagsProviderAccessor accessor, HolderLookup.Provider provider);

}
