package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;

@Component
public class CrackedAstralrockBlockModelHolder extends BlockModelHolder {

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.withExistingParent("block/cracked_astralrock", "block/cube")
			.renderType(RenderType.cutoutMipped().name)
			.texture("base", provider.mcLoc("block/bedrock"))
			.texture("overlay", "block/cracked_astralrock")
			.texture("particle", "#base")
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#base").cullface(Direction.DOWN).tintindex(0).emissivity(15, 15).end()
				.face(Direction.UP).texture("#base").cullface(Direction.UP).tintindex(0).emissivity(15, 15).end()
				.face(Direction.NORTH).texture("#base").cullface(Direction.NORTH).tintindex(0).emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#base").cullface(Direction.SOUTH).tintindex(0).emissivity(15, 15).end()
				.face(Direction.WEST).texture("#base").cullface(Direction.WEST).tintindex(0).emissivity(15, 15).end()
				.face(Direction.EAST).texture("#base").cullface(Direction.EAST).tintindex(0).emissivity(15, 15).end()
			.end()
			.element()
				.from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#base").cullface(Direction.DOWN).emissivity(15, 15).end()
				.face(Direction.UP).texture("#base").cullface(Direction.UP).emissivity(15, 15).end()
				.face(Direction.NORTH).texture("#base").cullface(Direction.NORTH).emissivity(15, 15).end()
				.face(Direction.SOUTH).texture("#base").cullface(Direction.SOUTH).emissivity(15, 15).end()
				.face(Direction.WEST).texture("#base").cullface(Direction.WEST).emissivity(15, 15).end()
				.face(Direction.EAST).texture("#base").cullface(Direction.EAST).emissivity(15, 15).end()
			.end();
		// @formatter:on
	}

}
