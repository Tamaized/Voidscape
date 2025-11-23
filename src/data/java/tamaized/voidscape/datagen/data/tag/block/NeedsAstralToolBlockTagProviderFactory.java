package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import tamaized.beanification.Component;
import tamaized.voidscape.block.RequiresVoidToolBlock;

@Component
public class NeedsAstralToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(RequiresVoidToolBlock.NEEDS_ASTRAL_TOOL);
	}

}
