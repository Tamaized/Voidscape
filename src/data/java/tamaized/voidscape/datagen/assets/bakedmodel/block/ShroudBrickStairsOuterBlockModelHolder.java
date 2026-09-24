package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.data.models.BlockModelGenerators;
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
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.OuterStairsFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.datagen.util.ModTextureSlots;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Optional;

@Component
public class ShroudBrickStairsOuterBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private OuterStairsFullbrightOverlayOverlayBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.materialBlocks().SHROUD_BRICK_STAIRS;
	}

	@Override
	public Optional<ModelHolder<BlockModelGenerators>> parent() {
		return Optional.of(parent);
	}

	@Override
	public Identifier finalize(BlockModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		return model
			.buildExtended()
			.create(Identifier.fromNamespaceAndPath(Voidscape.MODID, name("outer")), textures(), provider.modelOutput);
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
		mapping
			.putForced(TextureSlot.BOTTOM, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick")))
			.putForced(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick")))
			.putForced(TextureSlot.TOP, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick")))
			.putForced(ModTextureSlots.OVERLAY_BOTTOM, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick_overlay")))
			.putForced(ModTextureSlots.OVERLAY_SIDE, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick_overlay")))
			.putForced(ModTextureSlots.OVERLAY_TOP, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/shroud_brick_overlay")));
	}
}
