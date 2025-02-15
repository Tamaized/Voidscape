package tamaized.voidscape.datagen.assets.blockstate;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.datagen.assets.bakedmodel.block.PortalEWBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.PortalNSBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.VoidicCrystalBlockBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class PortalBlockStateFactory extends BlockStateFactory {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private PortalNSBlockModelHolder parentNS;

	@Autowired
	private PortalEWBlockModelHolder parentEW;

	@Override
	public void make(BlockStateProvider provider) {
		provider.getVariantBuilder(blocks.functionalBlocks().PORTAL.get())
			.partialState().with(PortalBlock.AXIS, Direction.Axis.X).addModels(ConfiguredModel.builder().modelFile(parentNS.get().orElseThrow()).build())
			.partialState().with(PortalBlock.AXIS, Direction.Axis.Z).addModels(ConfiguredModel.builder().modelFile(parentEW.get().orElseThrow()).build());
	}
}