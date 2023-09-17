package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.feature.ClusterFeature;
import tamaized.voidscape.world.feature.SpireFeature;
import tamaized.voidscape.world.feature.ThunderVinesFeature;
import tamaized.voidscape.world.feature.config.BooleanFeatureConfig;
import tamaized.voidscape.world.feature.config.ClusterConfig;
import tamaized.voidscape.world.feature.config.FluidFeatureConfig;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModFeatures implements RegistryClass {

	private static final DeferredRegister<Feature<?>> REGISTRY_FEATURES = RegUtil.create(ForgeRegistries.FEATURES);
	private static final DeferredRegister<PlacementModifierType<?>> REGISTRY_PLACEMENT_MOD_TYPE = RegUtil.create(Registries.PLACEMENT_MODIFIER_TYPE);

	public static final ResourceKey<ConfiguredFeature<?, ?>> THUNDER_FUNGUS = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Voidscape.MODID, "thunder_fungus"));
	public static final ResourceKey<ConfiguredFeature<?, ?>> THUNDER_FOREST_VEGETATION_BONEMEAL = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Voidscape.MODID, "thunder_forest_vegetation_bonemeal"));

	private static class SeekDownPlacementMod extends PlacementModifier {

		public static final Codec<SeekDownPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
				BOOL.fieldOf("check_below").orElse(false).forGetter(c -> c.check_below)).apply(p_242803_0_, SeekDownPlacementMod::new));

		public static RegistryObject<PlacementModifierType<SeekDownPlacementMod>> TYPE = registerPlacementModifierType("seek", () -> () -> CODEC);

		private final boolean check_below;

		public SeekDownPlacementMod(boolean val) {
			check_below = val;
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			final int y = pos.getY();
			BlockPos.MutableBlockPos seek = pos.mutable().move(Direction.UP, random.nextInt(15));
			BlockPos.MutableBlockPos check = seek.mutable().move(Direction.DOWN, 1);
			while ((check_below ? check : seek).getY() > y && seek.getY() > context.getLevel().getMinBuildHeight() && context.getLevel().getBlockState(check_below ? check : seek).isAir()) {
				seek.move(Direction.DOWN, 1);
				check.move(Direction.DOWN, 1);
			}
			return Stream.of(seek);
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE.get();
		}

	}

	private static class AirAbovePlacementMod extends PlacementModifier {

		public static final Codec<AirAbovePlacementMod> CODEC = Codec.unit(AirAbovePlacementMod::new);

		public static RegistryObject<PlacementModifierType<AirAbovePlacementMod>> TYPE = registerPlacementModifierType("air_above", () -> () -> CODEC);

		public AirAbovePlacementMod() {
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			return context.getBlockState(pos.above()).isAir() ? Stream.of(pos) : Stream.empty();
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE.get();
		}

	}

	private static class NotAirBelowPlacementMod extends PlacementModifier {

		public static final Codec<NotAirBelowPlacementMod> CODEC = Codec.unit(NotAirBelowPlacementMod::new);

		public static RegistryObject<PlacementModifierType<NotAirBelowPlacementMod>> TYPE = registerPlacementModifierType("not_air_below", () -> () -> CODEC);

		public NotAirBelowPlacementMod() {
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			return context.getBlockState(pos.below()).isAir() ? Stream.empty() : Stream.of(pos);
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE.get();
		}

	}

	private static class RandomYPlacementMod extends PlacementModifier {

		public static final Codec<RandomYPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
				INT.fieldOf("y").orElse(0).forGetter(c -> c.y)).apply(p_242803_0_, RandomYPlacementMod::new));

		public static RegistryObject<PlacementModifierType<RandomYPlacementMod>> TYPE = registerPlacementModifierType("random_y", () -> () -> CODEC);

		private final int y;

		public RandomYPlacementMod(int y) {
			this.y = y;
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			return Stream.of(pos.above(random.nextInt(y) - y / 2));
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE.get();
		}

	}

	public static final RegistryObject<Feature<BooleanFeatureConfig>> SPIRE = REGISTRY_FEATURES.register("spire", () -> new SpireFeature(BooleanFeatureConfig.CODEC));

	public static final RegistryObject<Feature<FluidFeatureConfig>> FLUID = REGISTRY_FEATURES.register("fluid", () -> new Feature<>(FluidFeatureConfig.CODEC) {
		@Override
		public boolean place(FeaturePlaceContext<FluidFeatureConfig> context) {
			context.level().setBlock(context.origin(), context.config().state.createLegacyBlock(), 2);
			context.level().scheduleTick(context.origin(), context.config().state.getType(), 0);
			return true;
		}
	});

	public static final RegistryObject<Feature<ClusterConfig>> CLUSTER = REGISTRY_FEATURES.register("cluster", () -> new ClusterFeature(ClusterConfig.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> THUNDER_VINES = REGISTRY_FEATURES.register("thunder_vines", ThunderVinesFeature::new);

	private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> registerPlacementModifierType(String name, Supplier<PlacementModifierType<P>> factory) {
		return REGISTRY_PLACEMENT_MOD_TYPE.register(name, factory);
	}

	@Override
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void init(IEventBus bus) {
		SeekDownPlacementMod.CODEC.getClass();
		AirAbovePlacementMod.CODEC.getClass();
		NotAirBelowPlacementMod.CODEC.getClass();
		RandomYPlacementMod.CODEC.getClass();
	}

}
