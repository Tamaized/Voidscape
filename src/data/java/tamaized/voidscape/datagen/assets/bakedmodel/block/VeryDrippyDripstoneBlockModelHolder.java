package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.overlay.FullFullbrightOverlayOverlayBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class VeryDrippyDripstoneBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Nullable
	@Override
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return blocks.functionalBlocks().VERY_DRIPPY_DRIPSTONE;
	}

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent(
				name(),
				"block/pointed_dripstone"
			)
			.renderType(RenderType.cutoutMipped().name)
			.texture("cross", "block/very_drippy_dripstone")
			.texture("particle", "#cross")
			.element()
				.from(0.8F, 0, 8).to(15.2F, 16, 8)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
				.shade(false)
				.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
				.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
			.end()
			.element()
				.from(8, 0, 0.8F).to(8, 16, 15.2F)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
				.shade(false)
				.face(Direction.WEST).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
				.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
			.end();
		// @formatter:on
	}

}
