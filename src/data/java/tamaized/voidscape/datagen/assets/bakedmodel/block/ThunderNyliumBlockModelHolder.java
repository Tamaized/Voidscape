package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeAllFullbrightBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.SideTopFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ThunderNyliumBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private SideTopFullbrightOverlayOverlayBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM),
				parent.getOrBuild(provider).getLocation()
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("top", provider.mcLoc("block/bedrock"))
			.texture("bottom", provider.mcLoc("block/bedrock"))
			.texture("side", provider.mcLoc("block/bedrock"))
			.texture("overlay-top", "block/thunder_nylium_top")
			.texture("overlay-bottom", "block/blank")
			.texture("overlay-side", "block/thunder_nylium_side")
			.texture("particle", "#overlay-top");
	}

}
