package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.block.BlockModelHolder;

@Component
public class TintedCrossFullbrightBlockModelHolder extends BlockModelHolder {

	public void build(BlockModelProvider provider) {
		set(
			// @formatter:off
			provider.getBuilder("block/fullbright/tinted_cross")
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
					.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).tintindex(0).end()
					.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).tintindex(0).end()
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
					.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).tintindex(0).end()
					.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#cross").emissivity(15, 15).tintindex(0).end()
				.end()
			// @formatter:on
		);
	}

}
