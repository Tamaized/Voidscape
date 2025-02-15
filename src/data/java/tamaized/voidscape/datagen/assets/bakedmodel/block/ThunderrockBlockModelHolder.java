package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ThunderrockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.spireBlocks().THUNDERROCK;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	public ModelFile build(BlockModelProvider provider) {
		return provider.withExistingParent(
				name(),
				"block/cube_all"
			)
			.texture("all", provider.mcLoc("block/bedrock"))
			.texture("particle", "#all");
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

}
