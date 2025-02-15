package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CrossFullbrightBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeColumnFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ThunderVinesBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CrossFullbrightBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_VINES;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(),
				parent.getOrBuild(provider).getLocation()
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("cross", "block/thunder_vines");
	}

	@Override
	public ModelFile buildItemBlockModel(ItemModelProvider provider) {
		return provider.withExistingParent(nameForItemBlock(), "item/generated")
			.texture("layer0", "block/thunder_vines_plant");
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}
}
