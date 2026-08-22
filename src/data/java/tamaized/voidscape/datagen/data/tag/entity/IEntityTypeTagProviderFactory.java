package tamaized.voidscape.datagen.data.tag.entity;

import net.minecraft.core.HolderLookup;
import tamaized.datagenutil.data.tag.ExposedEntityTypeTagProvider;

public interface IEntityTypeTagProviderFactory {

	void make(ExposedEntityTypeTagProvider accessor, HolderLookup.Provider provider);

}
