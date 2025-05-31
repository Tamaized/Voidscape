package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class MineableBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(BlockTags.MINEABLE_WITH_AXE).add(
			blocks.thunderForestBiomeBlocks().THUNDER_STEM.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_PLANKS.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_STAIRS.get(),
			blocks.thunderForestBiomeBlocks().THUNDER_SLAB.get()
		);

		accessor.tag(BlockTags.MINEABLE_WITH_HOE).add(
			blocks.thunderForestBiomeBlocks().THUNDER_WART.get()
		);

		accessor.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
			blocks.oreBlocks().VOIDIC_CRYSTAL_ORE.get(),
			blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get(),
			blocks.materialBlocks().CHARRED_BRICK.get(),
			blocks.oreBlocks().TITANITE_ORE.get(),
			blocks.oreBlocks().FLESH_ORE.get(),
			blocks.oreBlocks().STRANGE_ORE.get(),
			blocks.oreBlocks().CRACKED_ASTRALROCK.get(),
			blocks.machineBlocks().MACHINE_LIQUIFIER.get(),
			blocks.machineBlocks().MACHINE_DEFUSER.get(),
			blocks.machineBlocks().MACHINE_GERMINATOR.get(),
			blocks.machineBlocks().MACHINE_WELL.get(),
			blocks.machineBlocks().MACHINE_COOP.get(),
			blocks.machineBlocks().MACHINE_INFUSER.get(),
			blocks.machineBlocks().MACHINE_COLLECTOR.get()
		);
	}

}
