package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.neoforge.common.world.NeoForgeEnvironmentAttributes;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModDimensions;

import java.util.Optional;

@Component
public class DimensionTypeBootstrap implements IBootstrap {

	@Autowired
	private ModDimensions dimensions;

	@Nullable
	private Holder<DimensionType> VOID;

	@Override
	public RegistrySetBuilder bootstrap(RegistrySetBuilder builder) {
		return builder.add(Registries.DIMENSION_TYPE, context -> {
			HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
			VOID = context.register(
				ResourceKey.create(Registries.DIMENSION_TYPE, dimensions.VOID.identifier()),
				new DimensionType(
					true,
					false,
					false,
					false,
					1.0D,
					0,
					256,
					256,
					BlockTags.INFINIBURN_OVERWORLD,
					-0.3F,
					new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
					DimensionType.Skybox.OVERWORLD,
					CardinalLighting.Type.DEFAULT,
					EnvironmentAttributeMap.builder()
						.set(EnvironmentAttributes.CAN_START_RAID, false)
						.set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
						.set(EnvironmentAttributes.BLOCK_LIGHT_TINT, 0x40305A)
						.set(EnvironmentAttributes.NIGHT_VISION_COLOR, 0) // Prevent Night Vision
						.set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, dimensions.VOID.identifier())
						.build(),
					timelines.getOrThrow(TimelineTags.UNIVERSAL),
					Optional.empty()
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
