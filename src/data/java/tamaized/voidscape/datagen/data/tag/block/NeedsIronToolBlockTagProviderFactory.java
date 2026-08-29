package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.List;

@Component
public class NeedsIronToolBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.NEEDS_IRON_TOOL).addAll(List.of(
			blocks.machineBlocks().MACHINE_LIQUIFIER.getKey(),
			blocks.machineBlocks().MACHINE_DEFUSER.getKey(),
			blocks.machineBlocks().MACHINE_GERMINATOR.getKey(),
			blocks.machineBlocks().MACHINE_WELL.getKey(),
			blocks.machineBlocks().MACHINE_COOP.getKey(),
			blocks.machineBlocks().MACHINE_HATCHERY.getKey(),
			blocks.machineBlocks().MACHINE_INFUSER.getKey(),
			blocks.machineBlocks().MACHINE_COLLECTOR.getKey()
		));
	}
}
