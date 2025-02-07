package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class FlowerPotCrossFullbrightBlockModelHolder extends BlockModelHolder {

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.getBuilder("block/fullbright/flower_pot_cross")
				.ao(false)
				.texture("particle", provider.mcLoc("block/flower_pot"))
				.texture("flowerpot", provider.mcLoc("block/flower_pot"))
				.texture("dirt", provider.mcLoc("block/dirt"))
				.element()
					.from(5, 0, 5).to(6, 6, 11)
					.shade(false)
					.face(Direction.DOWN).uvs(5, 5, 6, 11).texture("#flowerpot").cullface(Direction.DOWN).end()
					.face(Direction.UP).uvs(5, 5, 6, 11).texture("#flowerpot").end()
					.face(Direction.NORTH).uvs(10, 10, 11, 16).texture("#flowerpot").end()
					.face(Direction.SOUTH).uvs(5, 10, 6, 16).texture("#flowerpot").end()
					.face(Direction.WEST).uvs(5, 10, 11, 16).texture("#flowerpot").end()
					.face(Direction.EAST).uvs(5, 10, 11, 16).texture("#flowerpot").end()
				.end()
				.element()
					.from(10, 0, 5).to(11, 6, 11)
					.shade(false)
					.face(Direction.DOWN).uvs(10, 5, 11, 11).texture("#flowerpot").cullface(Direction.DOWN).end()
					.face(Direction.UP).uvs(10, 5, 11, 11).texture("#flowerpot").end()
					.face(Direction.NORTH).uvs(5, 10, 6, 16).texture("#flowerpot").end()
					.face(Direction.SOUTH).uvs(10, 10, 11, 16).texture("#flowerpot").end()
					.face(Direction.WEST).uvs(5, 10, 11, 16).texture("#flowerpot").end()
					.face(Direction.EAST).uvs(5, 10, 11, 16).texture("#flowerpot").end()
				.end()
				.element()
					.from(6, 0, 5).to(10, 6, 6)
					.shade(false)
					.face(Direction.DOWN).uvs(6, 10, 10, 11).texture("#flowerpot").cullface(Direction.DOWN).end()
					.face(Direction.UP).uvs(6, 5, 10, 6).texture("#flowerpot").end()
					.face(Direction.NORTH).uvs(6, 10, 10, 16).texture("#flowerpot").end()
					.face(Direction.SOUTH).uvs(6, 10, 10, 16).texture("#flowerpot").end()
				.end()
				.element()
					.from(6, 0, 10).to(10, 6, 11)
					.shade(false)
					.face(Direction.DOWN).uvs(6, 5, 10, 6).texture("#flowerpot").cullface(Direction.DOWN).end()
					.face(Direction.UP).uvs(6, 10, 10, 11).texture("#flowerpot").end()
					.face(Direction.NORTH).uvs(6, 10, 10, 16).texture("#flowerpot").end()
					.face(Direction.SOUTH).uvs(6, 10, 10, 16).texture("#flowerpot").end()
				.end()
				.element()
					.from(6, 0, 6).to(10, 4, 10)
					.shade(false)
					.face(Direction.DOWN).uvs(6, 12, 10, 16).texture("#flowerpot").cullface(Direction.DOWN).end()
					.face(Direction.UP).uvs(6, 6, 10, 10).texture("#dirt").end()
				.end()
				.element()
					.from(2.6F, 4, 8).to(13.4F, 16, 8)
					.shade(false)
					.rotation()
						.origin(8, 8, 8)
						.axis(Direction.Axis.Y)
						.angle(45F)
						.rescale(true)
					.end()
					.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#plant").emissivity(15, 15).end()
					.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#plant").emissivity(15, 15).end()
				.end()
				.element()
					.from(8, 4, 2.6F).to(8, 16, 13.4F)
					.shade(false)
					.rotation()
						.origin(8, 8, 8)
						.axis(Direction.Axis.Y)
						.angle(45F)
						.rescale(true)
					.end()
					.face(Direction.WEST).uvs(0, 0, 16, 16).texture("#plant").emissivity(15, 15).end()
					.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#plant").emissivity(15, 15).end()
				.end()
			// @formatter:on
		);
	}

}
