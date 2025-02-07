package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class SlabTopFullbrightBlockModelHolder extends BlockModelHolder {

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.getBuilder("block/fullbright/slab_top")
				.ao(false)
				.texture("particle", "#side")
				.element()
					.from(0, 8, 0).to(16, 16, 16)
					.shade(false)
					.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").emissivity(15, 15).end()
					.face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").emissivity(15, 15).cullface(Direction.UP).end()
					.face(Direction.NORTH).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.NORTH).end()
					.face(Direction.SOUTH).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.SOUTH).end()
					.face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.WEST).end()
					.face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.EAST).end()
				.end()
			// @formatter:on
		);
	}

}
