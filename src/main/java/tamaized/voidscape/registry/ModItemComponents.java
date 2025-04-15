package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;

import java.util.function.Supplier;

@Component
public class ModItemComponents {

	private final DeferredRegister<DataComponentType<?>> REGISTRY = RegUtil.create(Registries.DATA_COMPONENT_TYPE);

	public final Supplier<DataComponentType<Boolean>> FANG = REGISTRY.register("fang", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.cacheEncoding()
		.networkSynchronized(ByteBufCodecs.BOOL)
		.build());

	public final Supplier<DataComponentType<Boolean>> DRACONIC = REGISTRY.register("draconic", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.cacheEncoding()
		.networkSynchronized(ByteBufCodecs.BOOL)
		.build());

}
