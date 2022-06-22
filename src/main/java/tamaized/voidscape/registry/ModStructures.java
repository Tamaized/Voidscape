package tamaized.voidscape.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.structures.NullStructure;

import java.util.function.Consumer;

public class ModStructures implements RegistryClass {

	private static void classloadPieces(StructurePieceType... noop) {
		// NO-OP
	}

	@Override
	public void init(IEventBus bus) {
		bus.addListener((Consumer<RegisterEvent>) event -> {

			classloadPieces(NullStructure.Pieces.MAIN);

			register(event, new NullStructure(), new ResourceLocation(Voidscape.MODID, "null"));

		});
	}

	private static void register(StructureFeature<?> structure, ResourceLocation name) {
		event.getRegistry().register(structure.setRegistryName(name));
	}

}
