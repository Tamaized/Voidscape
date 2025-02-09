package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class StairsFullbrightBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/fullbright/stairs", "block/block")
			.ao(false)
			.texture("particle", "#side")
			.transforms()
				.transform(ItemDisplayContext.GUI)
					.rotation(30, 135, 0)
					.translation(0, 0, 0)
					.scale(0.625F)
				.end()
				.transform(ItemDisplayContext.HEAD)
					.rotation(0, -90, 0)
					.translation(0, 0, 0)
					.scale(1)
				.end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
					.rotation(75, -135, 0)
					.translation(0, 2.5F, 0)
					.scale(0.375F)
				.end()
			.end()
			.element()
				.from(0, 0, 0).to(16, 8, 16)
				.shade(false)
				.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").emissivity(15, 15).cullface(Direction.DOWN).end()
				.face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").emissivity(15, 15).end()
				.face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#side").emissivity(15, 15).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#side").emissivity(15, 15).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 8, 16, 16).texture("#side").emissivity(15, 15).cullface(Direction.WEST).end()
				.face(Direction.EAST).uvs(0, 8, 16, 16).texture("#side").emissivity(15, 15).cullface(Direction.EAST).end()
			.end()
			.element()
				.from(8, 8, 0).to(16, 16, 16)
				.shade(false)
				.face(Direction.UP).uvs(8, 0, 16, 16).texture("#top").emissivity(15, 15).cullface(Direction.UP).end()
				.face(Direction.NORTH).uvs(0, 0, 8, 8).texture("#side").emissivity(15, 15).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(8, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).end()
				.face(Direction.EAST).uvs(0, 0, 16, 8).texture("#side").emissivity(15, 15).cullface(Direction.EAST).end()
			.end();
		// @formatter:on
	}

}
