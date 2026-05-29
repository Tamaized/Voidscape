package tamaized.voidscape.datagen.bootstrap.structureset;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

public abstract class StructureSetBootstrapHolder {

	@Nullable
	private Holder.Reference<StructureSet> ref;

	public ResourceKey<StructureSet> key() {
		return ResourceKey.create(Registries.STRUCTURE_SET, Identifier.fromNamespaceAndPath(Voidscape.MODID, name()));
	}

	public abstract String name();

	public Holder.Reference<StructureSet> getOrMake(BootstrapContext<StructureSet> context) {
		if (ref == null)
			ref = context.register(key(), make(context));
		return ref;
	}

	public Optional<Holder.Reference<StructureSet>> get() {
		return Optional.ofNullable(ref);
	}

	public abstract StructureSet make(BootstrapContext<StructureSet> context);

}
