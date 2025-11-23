package tamaized.voidscape.datagen.data.tag.entity;

import net.minecraft.core.HolderLookup;

public interface IEntityTypeTagProviderFactory {

	void make(EntityTypeTagProviderFactory.EntityTypeTagsProviderAccessor accessor, HolderLookup.Provider provider);

}
