package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;

public interface IBlockTagProviderFactory {

	void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider);

}
