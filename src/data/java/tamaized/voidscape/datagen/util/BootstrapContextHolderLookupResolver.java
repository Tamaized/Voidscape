package tamaized.voidscape.datagen.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Component
public class BootstrapContextHolderLookupResolver {

	@Nullable
	private MethodHandle holders;

	@PostConstruct
	private void setup() {
		try {
			for (Class<?> inner : RegistrySetBuilder.class.getDeclaredClasses()) {
				if (inner.getSimpleName().equals("UniversalLookup")) {
					Field field = inner.getDeclaredField("holders");
					field.trySetAccessible();
					holders = MethodHandles.lookup().unreflectGetter(field);
					break;
				}
			}
		} catch (Exception ex) {
			Voidscape.LOGGER.error(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public Optional<Map<ResourceKey<Object>, Holder.Reference<Object>>> getHolders(HolderGetter<?> provider) {
		return Optional.ofNullable(holders).map(holders -> {
			try {
				return (Map<ResourceKey<Object>, Holder.Reference<Object>>) holders.invoke(provider);
			} catch (Throwable ex) {
				Voidscape.LOGGER.error(ex);
				return null;
			}
		});
	}

}
