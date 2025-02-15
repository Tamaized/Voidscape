package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
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
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeColumnOverlayFullbrightBlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.OuterStairsFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;

@Component
public class ThunderStemBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CubeColumnOverlayFullbrightBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_STEM;
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
			.texture("end", "block/thunder_stem_top")
			.texture("end-overlay", "block/thunder_stem_top_overlay")
			.texture("side", "block/thunder_stem")
			.texture("side-overlay", "block/thunder_stem_overlay");
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
