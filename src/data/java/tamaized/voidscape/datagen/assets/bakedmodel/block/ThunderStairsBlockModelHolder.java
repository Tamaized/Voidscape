package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.StairsFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;
import java.util.Optional;

@Component
public class ThunderStairsBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private StairsFullbrightBlockModelHolder parent;

	@Autowired
	private ThunderStairsInnerBlockModelHolder innerStairs;

	@Autowired
	private ThunderStairsOuterBlockModelHolder outerStairs;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends StairBlock> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_STAIRS;
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
			.texture("bottom", "block/thunder_planks")
			.texture("side", "block/thunder_planks")
			.texture("top", "block/thunder_planks");
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public void buildBlockState(BlockStateProvider provider) {
		provider.stairsBlock(Objects.requireNonNull(blockForName()).value(), get().orElseThrow(), innerStairs.get().orElseThrow(), outerStairs.get().orElseThrow());
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Thunder Stairs");
	}
}
