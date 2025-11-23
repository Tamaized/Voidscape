package tamaized.voidscape.datagen.bootstrap.structureset;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.structure.CharredStructureBootstrap;

@Component
public class CharredStructureSetBootstrap extends StructureSetBootstrapHolder {

	@Autowired
	private CharredStructureBootstrap parent;

	@Override
	public String name() {
		return "charred";
	}

	@Override
	public StructureSet make(BootstrapContext<StructureSet> context) {
		return new StructureSet(
			parent.get().orElseThrow(),
			new RandomSpreadStructurePlacement(
				4, 2, RandomSpreadType.LINEAR, 21916695
			)
		);
    }
}
