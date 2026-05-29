package tamaized.voidscape.datagen.bootstrap.feature.configured;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

public abstract class ConfiguredFeatureBootstrapHolder {

	@Nullable
	private Holder.Reference<ConfiguredFeature<?, ?>> ref;

	public ResourceKey<ConfiguredFeature<?, ?>> key() {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Voidscape.MODID, name()));
	}

	public abstract String name();

	public Holder.Reference<ConfiguredFeature<?, ?>> getOrMake(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		if (ref == null)
			ref = context.register(key(), make(context));
		return ref;
	}

	public Optional<Holder.Reference<ConfiguredFeature<?, ?>>> get() {
		return Optional.ofNullable(ref);
	}

	public abstract ConfiguredFeature<?, ?> make(BootstrapContext<ConfiguredFeature<?, ?>> context);

}
