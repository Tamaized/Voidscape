package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.structure.CharredStructure;

import java.util.function.Supplier;

public class ModStructures implements RegistryClass {

	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = RegUtil.create(Registries.STRUCTURE_TYPE);
	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = RegUtil.create(Registries.STRUCTURE_PIECE);

	public static class StructureTypes {

		public static final Supplier<StructureType<CharredStructure>> CHARRED = STRUCTURE_TYPES.register("charred", () -> () -> CharredStructure.CODEC);

		private static void classload() {

		}

	}

	public static class Pieces {

		public static final Supplier<StructurePieceType> CHARRED_MAIN = STRUCTURE_PIECE_TYPES.register("charred_pieces_main", () -> CharredStructure.Pieces.Piece::new);

		private static void classload() {

		}

	}

	@Override
	public void init(IEventBus bus) {
		StructureTypes.classload();
		Pieces.classload();
	}

}
