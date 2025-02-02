package tamaized.voidscape.registry;

import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.block.*;

@Component
public record ModBlockComponentDirectory(
	@Autowired EtherealFruitBlocks etherealFruitBlocks,
	@Autowired FunctionalBlocks functionalBlocks,
	@Autowired ImposterBlocks imposterBlocks,
	@Autowired MachineBlocks machineBlocks,
	@Autowired MaterialBlocks materialBlocks,
	@Autowired NullBiomeBlocks nullBiomeBlocks,
	@Autowired OreBlocks oreBlocks,
	@Autowired SpireBlocks spireBlocks,
	@Autowired ThunderForestBiomeBlocks thunderForestBiomeBlocks
) {

}
