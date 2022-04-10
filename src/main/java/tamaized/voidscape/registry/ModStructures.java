package tamaized.voidscape.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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
		bus.addGenericListener(StructureFeature.class, (Consumer<RegistryEvent.Register<StructureFeature<?>>>) event -> {

			classloadPieces(NullStructure.Pieces.MAIN);

			register(event, new NullStructure(), new ResourceLocation(Voidscape.MODID, "null"));

		});
	}

	private static void register(RegistryEvent.Register<StructureFeature<?>> event, StructureFeature<?> structure, ResourceLocation name) {
		event.getRegistry().register(structure.setRegistryName(name));
	}

}
