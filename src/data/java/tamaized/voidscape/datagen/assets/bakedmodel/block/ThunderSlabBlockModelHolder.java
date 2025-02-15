package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.SlabFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;

@Component
public class ThunderSlabBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private SlabFullbrightBlockModelHolder parent;

	@Autowired
	private ThunderPlanksBlockModelHolder planks;

	@Autowired
	private ThunderSlabTopBlockModelHolder slabTop;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_SLAB;
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
		provider.getVariantBuilder(Objects.requireNonNull(blockForName()).get())
			.partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(ConfiguredModel.builder().modelFile(get().orElseThrow()).build())
			.partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(ConfiguredModel.builder().modelFile(planks.get().orElseThrow()).build())
			.partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(ConfiguredModel.builder().modelFile(slabTop.get().orElseThrow()).build());
	}
}
