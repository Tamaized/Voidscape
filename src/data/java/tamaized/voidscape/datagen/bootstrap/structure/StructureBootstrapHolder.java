package tamaized.voidscape.datagen.bootstrap.structure;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

public abstract class StructureBootstrapHolder {

	@Nullable
	private Holder.Reference<Structure> ref;

	public ResourceKey<Structure> key() {
		return ResourceKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Voidscape.MODID, name()));
	}

	public abstract String name();

	public Holder.Reference<Structure> getOrMake(BootstrapContext<Structure> context) {
		if (ref == null)
			ref = context.register(key(), make(context));
		return ref;
	}

	public Optional<Holder.Reference<Structure>> get() {
		return Optional.ofNullable(ref);
	}

	public abstract Structure make(BootstrapContext<Structure> context);

}
