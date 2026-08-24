package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.ModelHolder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.SlabFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;
import java.util.Optional;

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

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, name()), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putForced(TextureSlot.BOTTOM, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/thunder_planks")))
			.putForced(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/thunder_planks")))
			.putForced(TextureSlot.TOP, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/thunder_planks")));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public MultiVariantGenerator buildBlockState(BlockModelGenerators provider) {
		return MultiVariantGenerator
			.dispatch(Objects.requireNonNull(blockForName()).get())
			.with(
				PropertyDispatch.initial(SlabBlock.TYPE)
					.select(SlabType.BOTTOM, BlockModelGenerators.plainVariant(getOrBuild(provider)))
					.select(SlabType.DOUBLE, BlockModelGenerators.plainVariant(planks.getOrBuild(provider)))
					.select(SlabType.TOP, BlockModelGenerators.plainVariant(slabTop.getOrBuild(provider)))
			);
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Thunder Slab");
	}
}
