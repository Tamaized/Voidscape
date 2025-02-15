package tamaized.voidscape.datagen.assets.blockstate;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.VoidicCrystalBlockBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class FragileVoidicCrystalBlockStateFactory extends BlockStateFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private VoidicCrystalBlockBlockModelHolder parent;

	@Override
	public void make(BlockStateProvider provider) {
		provider.simpleBlock(blocks.imposterBlocks().FRAGILE_VOIDIC_CRYSTAL_BLOCK.get(), parent.get().orElseThrow());
	}
}
