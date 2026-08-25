package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.ModelHolder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.TintedCubeAllFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;
import java.util.Optional;

@Component
public class AntirockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private TintedCubeAllFullbrightBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.spireBlocks().ANTIROCK;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	@Override
	public Identifier buildItemBlockModel(BlockModelGenerators provider) {
		Identifier id = getOrBuild(provider);
		provider.registerSimpleTintedItemModel(Objects.requireNonNull(blockForName()).get(), id, ItemModelUtils.constantTint(0x331166));
		return id;
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
		mapping.putForced(TextureSlot.ALL, new Material(Identifier.withDefaultNamespace("block/bedrock")));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Anti-Rock");
	}
}
