package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;

public interface IBlockTagProviderFactory {

	void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider);

}
