package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCubeAllFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class AstralrockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private TintedCubeAllFullbrightBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.spireBlocks().ASTRALROCK;
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
			.texture("all", provider.mcLoc("block/bedrock"));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

}
