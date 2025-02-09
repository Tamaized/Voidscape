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
public class PortalEWBlockModelHolder extends BlockModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private CubeAllFullbrightBlockModelHolder parent;

	public ModelFile build(BlockModelProvider provider) {
		// @formatter:off
		return provider.getBuilder("block/portal_ew")
			.renderType(RenderType.translucent().name)
			.texture("portal", "block/portal")
			.texture("particle", "#portal")
			.element()
				.from(6, 0, 0).to(10, 16, 16)
				.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#portal").end()
				.face(Direction.WEST).uvs(0, 0, 16, 16).texture("#portal").end()
			.end();
		// @formatter:on
	}

}
