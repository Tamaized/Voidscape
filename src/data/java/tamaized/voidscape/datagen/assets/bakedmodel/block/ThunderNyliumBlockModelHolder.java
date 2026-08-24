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
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.SideTopFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.datagen.util.ModTextureSlots;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Optional;

@Component
public class ThunderNyliumBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private SideTopFullbrightOverlayOverlayBlockModelHolder parent;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.thunderForestBiomeBlocks().THUNDER_NYLIUM;
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
			.putRef(TextureSlot.PARTICLE, ModTextureSlots.OVERLAY_TOP)
			.putForced(TextureSlot.TOP, new Material(Identifier.withDefaultNamespace("block/bedrock")))
			.putForced(TextureSlot.BOTTOM, new Material(Identifier.withDefaultNamespace("block/bedrock")))
			.putForced(TextureSlot.SIDE, new Material(Identifier.withDefaultNamespace("block/bedrock")))
			.putForced(ModTextureSlots.OVERLAY_TOP, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, name("top"))))
			.putForced(ModTextureSlots.OVERLAY_BOTTOM, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, "block/blank")))
			.putForced(ModTextureSlots.OVERLAY_SIDE, new Material(Identifier.fromNamespaceAndPath(Voidscape.MODID, name("side"))));
	}

	@Override
	public boolean hasBlockState() {
		return true;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Thunder Nylium");
	}
}
