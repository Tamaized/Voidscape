package tamaized.voidscape.datagen.assets.bakedmodel.block.overlay;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class BaseOverlayBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/overlay/base", "block/block")
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#up").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#west").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#east").cullface(Direction.EAST).end()
			.end()
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#overlay-down").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#overlay-up").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#overlay-north").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#overlay-south").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#overlay-west").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#overlay-east").cullface(Direction.EAST).end()
			.end();
		// @formatter:on
	}

}
