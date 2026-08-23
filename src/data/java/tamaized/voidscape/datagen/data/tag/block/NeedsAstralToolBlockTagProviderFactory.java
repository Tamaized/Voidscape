package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.block.RequiresVoidToolBlock;

@Component
public class NeedsAstralToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(RequiresVoidToolBlock.NEEDS_ASTRAL_TOOL);
	}
}
