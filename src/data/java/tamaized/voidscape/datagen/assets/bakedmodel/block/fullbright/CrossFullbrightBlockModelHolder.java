package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class CrossFullbrightBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.getBuilder("block/fullbright/cross")
			.ao(false)
			.texture("particle", "#cross")
			.element()
				.from(0.8F, 0, 8).to(15.2F, 16, 8)
				.rotation()
					.origin(8, 8, 8)
					.axis(Direction.Axis.Y)
					.angle(45)
					.rescale(true)
				.end()
				.shade(false)
				.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
				.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
			.end()
			.element()
				.from(8, 0, 0.8F).to(8, 16, 15.2F)
				.rotation()
					.origin(8, 8, 8)
					.axis(Direction.Axis.Y)
					.angle(45)
					.rescale(true)
				.end()
				.shade(false)
				.face(Direction.WEST).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
				.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).end()
			.end();
			// @formatter:on
	}

}
