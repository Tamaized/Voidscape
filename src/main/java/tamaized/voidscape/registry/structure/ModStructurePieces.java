package tamaized.voidscape.registry.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.structure.CharredStructure;
import tamaized.voidscape.structure.ShroudTowerStructure;

import java.util.function.Supplier;

@Component
public class ModStructurePieces {

	public final Supplier<StructurePieceType> CHARRED_MAIN = RegUtil.register(
		Registries.STRUCTURE_PIECE,
		"charred_pieces_main",
		() -> CharredStructure.Pieces.Piece::new
	);

	public final Supplier<StructurePieceType> SHROUD_TOWER_MAIN = RegUtil.register(
		Registries.STRUCTURE_PIECE,
		"shroud_tower_pieces_main",
		() -> ShroudTowerStructure.Pieces.Piece::new
	);

}
