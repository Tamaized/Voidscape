package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.block.BlockModelHolder;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;
import java.util.Optional;

@Component
public class VeryDrippyDripstoneBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.functionalBlocks().VERY_DRIPPY_DRIPSTONE;
	}

	@Override
	public boolean hasStandardBlockItem() {
		return true;
	}

	@Override
	public Identifier buildItemBlockModel(BlockModelGenerators provider) {
		Identifier id = ModelTemplates.FLAT_ITEM
			.extend()
			.itemLayerFaceData("layer0", new ExtraFaceData(ExtraFaceData.DEFAULT.color(), 15, ExtraFaceData.DEFAULT.ambientOcclusion()))
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, t -> t
				.translation(-1, -1, 0)
				.rotation(0, 100, 0)
				.scale(0.9F))
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, t -> t
				.translation(0, -2, 0)
				.rotation(0, 100, 0)
				.scale(0.9F))
			.build()
			.create(
				Identifier.fromNamespaceAndPath(Voidscape.MODID, nameForItemBlock()),
				new TextureMapping().put(TextureSlot.LAYER0, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, nameForItemBlock()))),
				provider.modelOutput
			);
		provider.registerSimpleItemModel(Objects.requireNonNull(blockForName()).get(), id);
		return id;
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended(m -> m
				.parent(Identifier.withDefaultNamespace("block/pointed_dripstone"))
				.element(e -> e
					.from(0.8F, 0, 8).to(15.2F, 16, 8)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45)
						.rescale(true))
					.shade(false)
					.face(Direction.NORTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15))
					.face(Direction.SOUTH, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15)))
				.element(e -> e
					.from(8, 0, 0.8F).to(8, 16, 15.2F)
					.rotation(r -> r
						.origin(8, 8, 8)
						.singleAxis(Direction.Axis.Y, 45)
						.rescale(true))
					.shade(false)
					.face(Direction.WEST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15))
					.face(Direction.EAST, f -> f.uvs(0, 0, 16, 16).texture(TextureSlot.CROSS).lightEmission(15))))
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, name()), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putRef(TextureSlot.PARTICLE, TextureSlot.CROSS)
			.putForced(TextureSlot.CROSS, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, name())));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Very Drippy Dripstone");
	}
}
