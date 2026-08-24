package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class PortalBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private PortalNSBlockModelHolder portalNS;

	@Autowired
	private PortalEWBlockModelHolder portalEW;

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return portalNS.getOrBuild(provider);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public MultiVariantGenerator buildBlockState(BlockModelGenerators provider) {
		return MultiVariantGenerator
			.dispatch(blocks.functionalBlocks().PORTAL.get())
			.with(
				PropertyDispatch.initial(PortalBlock.AXIS)
					.select(Direction.Axis.X, BlockModelGenerators.plainVariant(portalNS.getOrBuild(provider)))
					.select(Direction.Axis.Z, BlockModelGenerators.plainVariant(portalEW.getOrBuild(provider)))
			);
	}
}
