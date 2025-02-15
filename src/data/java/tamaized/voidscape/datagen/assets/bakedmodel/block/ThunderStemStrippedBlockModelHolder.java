package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeColumnFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;

@Component
public class ThunderStemStrippedBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CubeColumnFullbrightBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED;
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
			.texture("end", "block/thunder_stem_stripped_top")
			.texture("side", "block/thunder_stem_stripped");
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public void buildBlockState(BlockStateProvider provider) {
		provider.getVariantBuilder(Objects.requireNonNull(blockForName()).get())
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).addModels(
				ConfiguredModel.builder().modelFile(get().orElseThrow())
					.rotationX(90)
					.rotationY(90)
					.build()
			)
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).addModels(
				ConfiguredModel.builder().modelFile(get().orElseThrow()).build()
			)
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).addModels(
				ConfiguredModel.builder().modelFile(get().orElseThrow())
					.rotationX(90)
					.build()
			);
	}

}
