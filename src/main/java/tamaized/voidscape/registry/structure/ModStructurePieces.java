package tamaized.voidscape.registry.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.structure.CharredStructure;

import java.util.function.Supplier;

@Component
public class ModStructurePieces {

	public final Supplier<StructurePieceType> CHARRED_MAIN = RegUtil.register(Registries.STRUCTURE_PIECE, "charred_pieces_main", () -> CharredStructure.Pieces.Piece::new);

}
