package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.DimensionType;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;

import javax.annotation.Nullable;
import java.util.OptionalLong;

@Component
public class DimensionTypeBootstrap implements IBootstrap {

	@Autowired
	private ModDimensions dimensions;

	@Nullable
	private Holder<DimensionType> VOID;

	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.DIMENSION_TYPE, context -> {
			VOID = context.register(
				ResourceKey.create(Registries.DIMENSION_TYPE, dimensions.VOID.location()),
				new DimensionType(
					OptionalLong.of(6000L),
					false,
					false,
					false,
					false,
					1.0D,
					false,
					false,
					0,
					256,
					256,
					BlockTags.INFINIBURN_OVERWORLD,
					dimensions.VOID.location(),
					-0.3F,
					new DimensionType.MonsterSettings(false, false, ConstantInt.ZERO, 0)
				)
			);
		});
	}

	public Holder<DimensionType> getVoid() {
		if (VOID == null)
			throw new IllegalStateException("DimensionTypeBootstrap hasn't ran yet, fix your priorities!");
		return VOID;
	}

}
