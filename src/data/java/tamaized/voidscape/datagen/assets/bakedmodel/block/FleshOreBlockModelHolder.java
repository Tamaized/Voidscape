package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class FleshOreBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private FullFullbrightOverlayOverlayBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(blocks.oreBlocks().FLESH_ORE),
				parent.getOrBuild(provider).getLocation()
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("base", provider.mcLoc("block/netherrack"))
			.texture("side", "block/flesh_ore")
			.texture("particle", "#base");
	}

}
