package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.ModTextureSlots;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;
import java.util.Optional;

@Component
public class CrackedAstralrockBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.oreBlocks().CRACKED_ASTRALROCK;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	@Override
	public Identifier buildItemBlockModel(BlockModelGenerators provider) {
		Identifier id = getOrBuild(provider);
		provider.registerSimpleTintedItemModel(Objects.requireNonNull(blockForName()).get(), id, ItemModelUtils.constantTint(0x661133));
		return id;
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/cube"))
				.element(e -> e
					.from(0, 0, 0).to(16, 16, 16)
					.face(Direction.DOWN, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.DOWN).tintindex(0).lightEmission(15))
					.face(Direction.UP, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.UP).tintindex(0).lightEmission(15))
					.face(Direction.NORTH, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.NORTH).tintindex(0).lightEmission(15))
					.face(Direction.SOUTH, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.SOUTH).tintindex(0).lightEmission(15))
					.face(Direction.WEST, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.WEST).tintindex(0).lightEmission(15))
					.face(Direction.EAST, f -> f.texture(ModTextureSlots.BASE).cullface(Direction.EAST).tintindex(0).lightEmission(15)))
				.element(e -> e
					.from(0, 0, 0).to(16, 16, 16)
					.face(Direction.DOWN, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.DOWN).lightEmission(15))
					.face(Direction.UP, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.UP).lightEmission(15))
					.face(Direction.NORTH, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.NORTH).lightEmission(15))
					.face(Direction.SOUTH, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.SOUTH).lightEmission(15))
					.face(Direction.WEST, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.WEST).lightEmission(15))
					.face(Direction.EAST, f -> f.texture(ModTextureSlots.OVERLAY).cullface(Direction.EAST).lightEmission(15))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, name()), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, ModTextureSlots.BASE)
			.putForced(ModTextureSlots.BASE, new Material(Identifier.withDefaultNamespace("block/bedrock")))
			.putForced(ModTextureSlots.OVERLAY, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, name())));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Cracked Astral Rock");
	}
}
