package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class CubeOverlayFullbrightBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/fullbright/cube_overlay", "block/block")
			.ao(false)
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.shade(false)
				.face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#up").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#west").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#east").cullface(Direction.EAST).end()
			.end()
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.shade(false)
				.face(Direction.DOWN).texture("#down-overlay").cullface(Direction.DOWN).emissivity(15, 15).end()
				.face(Direction.UP).texture("#up-overlay").cullface(Direction.UP).emissivity(15, 15).end()
				.face(Direction.NORTH).texture("#north-overlay").cullface(Direction.NORTH).emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#south-overlay").cullface(Direction.SOUTH).emissivity(15, 15).end()
				.face(Direction.WEST).texture("#west-overlay").cullface(Direction.WEST).emissivity(15, 15).end()
				.face(Direction.EAST).texture("#east-overlay").cullface(Direction.EAST).emissivity(15, 15).end()
			.end();
		// @formatter:on
	}

}
