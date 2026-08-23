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
public class MineableBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.MINEABLE_WITH_AXE).addAll(List.of(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_PLANKS.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_STAIRS.getKey(),
			blocks.thunderForestBiomeBlocks().THUNDER_SLAB.getKey()
		));

		accessor.tag(BlockTags.MINEABLE_WITH_HOE).add(
			blocks.thunderForestBiomeBlocks().THUNDER_WART.getKey()
		);

		accessor.tag(BlockTags.MINEABLE_WITH_PICKAXE).addAll(List.of(
			blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.getKey(),
			blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.getKey(),
			blocks.materialBlocks().CHARRED_BRICK.getKey(),
			blocks.oreBlocks().TITANITE_ORE.getKey(),
			blocks.oreBlocks().FLESH_ORE.getKey(),
			blocks.oreBlocks().STRANGE_ORE.getKey(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.getKey(),
			blocks.machineBlocks().MACHINE_LIQUIFIER.getKey(),
			blocks.machineBlocks().MACHINE_DEFUSER.getKey(),
			blocks.machineBlocks().MACHINE_GERMINATOR.getKey(),
			blocks.machineBlocks().MACHINE_WELL.getKey(),
			blocks.machineBlocks().MACHINE_COOP.getKey(),
			blocks.machineBlocks().MACHINE_INFUSER.getKey(),
			blocks.machineBlocks().MACHINE_COLLECTOR.getKey()
		));
	}
}
