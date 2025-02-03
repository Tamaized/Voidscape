package tamaized.voidscape.datagen.assets.bakedmodel.block.fullbright;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;

@Component
public class FullbrightBlockModelProviderFactory {

	@Autowired
	private CrossFullbrightBlockModelHolder crossFullbrightBlockModelHolder;

	@Autowired
	private CubeFullbrightBlockModelHolder cubeFullbrightBlockModelHolder;

	@Autowired
	private CubeAllFullbrightBlockModelHolder cubeAllFullbrightBlockModelHolder;

	@Autowired
	private CubeColumnFullbrightBlockModelHolder cubeColumnFullbrightBlockModelHolder;

	@Autowired
	private CubeOverlayFullbrightBlockModelHolder cubeOverlayFullbrightBlockModelHolder;

	@Autowired
	private CubeColumnOverlayFullbrightBlockModelHolder cubeColumnOverlayFullbrightBlockModelHolder;

	public void make(BlockModelProvider provider) {
		crossFullbrightBlockModelHolder.build(provider);
		cubeFullbrightBlockModelHolder.build(provider);
		cubeAllFullbrightBlockModelHolder.build(provider);
		cubeColumnFullbrightBlockModelHolder.build(provider);
		cubeOverlayFullbrightBlockModelHolder.build(provider);
		cubeColumnOverlayFullbrightBlockModelHolder.build(provider);
	}

}
