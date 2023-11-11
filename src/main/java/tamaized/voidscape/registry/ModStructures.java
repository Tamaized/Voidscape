package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;

public class ModStructures implements RegistryClass {

	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = RegUtil.create(Registries.STRUCTURE_TYPE);
	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = RegUtil.create(Registries.STRUCTURE_PIECE);

	public static class StructureTypes {

//		public static final RegistryObject<StructureType<NullStructure>> NULL = STRUCTURE_TYPES.register("null", () -> () -> NullStructure.CODEC);

		private static void classload() {

		}

	}

	public static class Pieces {

//		public static final RegistryObject<StructurePieceType> NULL_MAIN = STRUCTURE_PIECE_TYPES.register("nullmain", () -> NullStructure.Pieces.Piece::new);

		private static void classload() {

		}

	}

	@Override
	public void init(IEventBus bus) {
		StructureTypes.classload();
		Pieces.classload();
	}

}
