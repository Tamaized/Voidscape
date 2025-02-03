package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class CubeFullbrightBlockModelHolder extends BlockModelHolder {

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.withExistingParent("block/fullbright/cube", "block/block")
				.ao(false)
				.element()
					.from(0, 0, 0).to(16, 16, 16)
					.shade(false)
					.face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).emissivity(15, 15).end()
					.face(Direction.UP).texture("#up").cullface(Direction.UP).emissivity(15, 15).end()
					.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).emissivity(15, 15).end()
					.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).emissivity(15, 15).end()
					.face(Direction.WEST).texture("#west").cullface(Direction.WEST).emissivity(15, 15).end()
					.face(Direction.EAST).texture("#east").cullface(Direction.EAST).emissivity(15, 15).end()
				.end()
			// @formatter:on
		);
	}

}
