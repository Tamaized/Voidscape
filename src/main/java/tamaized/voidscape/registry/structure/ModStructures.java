package tamaized.voidscape.registry.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.structure.CharredStructure;

import java.util.function.Supplier;

@Component
public class ModStructures {

	private final DeferredRegister<StructureType<?>> REGISTRY = RegUtil.create(Registries.STRUCTURE_TYPE);

	public final Supplier<StructureType<CharredStructure>> CHARRED = REGISTRY.register("charred", () -> () -> CharredStructure.CODEC);

}
