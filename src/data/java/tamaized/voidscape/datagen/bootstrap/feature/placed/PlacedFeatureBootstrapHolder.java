package tamaized.voidscape.datagen.bootstrap.feature.placed;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.DirectReferenceHolder;

import java.util.Optional;

public abstract class PlacedFeatureBootstrapHolder {

	@Nullable
	private Holder.Reference<PlacedFeature> ref;

	public ResourceKey<PlacedFeature> key() {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, name()));
	}

	public abstract String name();

	public Holder.Reference<PlacedFeature> getOrMake(BootstrapContext<PlacedFeature> context) {
		if (ref == null)
			ref = context.register(key(), make(context));
		return ref;
	}

	public Optional<Holder.Reference<PlacedFeature>> get() {
		return Optional.ofNullable(ref);
	}

	public DirectReferenceHolder<PlacedFeature> asDirectReferenceHolder() {
		return DirectReferenceHolder.of(key());
	}

	public abstract PlacedFeature make(BootstrapContext<PlacedFeature> context);

}
