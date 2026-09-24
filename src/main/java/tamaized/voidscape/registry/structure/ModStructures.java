package tamaized.voidscape.registry.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.structure.CharredStructure;
import tamaized.voidscape.structure.ShroudTowerStructure;

import java.util.function.Supplier;

@Component
public class ModStructures {

	public final Supplier<StructureType<CharredStructure>> CHARRED = RegUtil.register(
		Registries.STRUCTURE_TYPE,
		"charred",
		() -> () -> CharredStructure.CODEC
	);

	public final Supplier<StructureType<ShroudTowerStructure>> SHROUD_TOWER = RegUtil.register(
		Registries.STRUCTURE_TYPE,
		"shroud_tower",
		() -> () -> ShroudTowerStructure.CODEC
	);

}
