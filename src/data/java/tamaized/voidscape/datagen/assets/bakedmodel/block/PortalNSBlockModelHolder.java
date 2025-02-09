package tamaized.voidscape.datagen.assets.bakedmodel.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright.CubeAllFullbrightBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class PortalNSBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CubeAllFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.getBuilder("block/portal_ns")
			.renderType(RenderType.translucent().name)
			.texture("portal", "block/portal")
			.texture("particle", "#portal")
			.element()
				.from(0, 0, 6).to(16, 16, 10)
				.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#portal").end()
				.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#portal").end()
			.end();
		// @formatter:on
	}

}
