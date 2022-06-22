package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;
import tamaized.voidscape.world.featureconfig.BooleanFeatureConfig;
import tamaized.voidscape.world.featureconfig.ClusterConfig;
import tamaized.voidscape.world.featureconfig.FluidFeatureConfig;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModFeatures implements RegistryClass {

	private static final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(ForgeRegistries.FEATURES);

	private static class SeekDownPlacementMod extends PlacementModifier {

		public static final Codec<SeekDownPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
				BOOL.fieldOf("check_below").orElse(false).forGetter(c -> c.check_below)).apply(p_242803_0_, SeekDownPlacementMod::new));

		public static PlacementModifierType<SeekDownPlacementMod> TYPE;

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
			return TYPE;
		}

	}

	private static class AirAbovePlacementMod extends PlacementModifier {

		public static final Codec<AirAbovePlacementMod> CODEC = Codec.unit(AirAbovePlacementMod::new);

		public static PlacementModifierType<AirAbovePlacementMod> TYPE;

		public AirAbovePlacementMod() {
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			return context.getBlockState(pos.above()).isAir() ? Stream.of(pos) : Stream.empty();
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE;
		}

	}

	private static class NotAirBelowPlacementMod extends PlacementModifier {

		public static final Codec<NotAirBelowPlacementMod> CODEC = Codec.unit(NotAirBelowPlacementMod::new);

		public static PlacementModifierType<NotAirBelowPlacementMod> TYPE;

		public NotAirBelowPlacementMod() {
		}

		@Override
		public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
			return context.getBlockState(pos.below()).isAir() ? Stream.empty() : Stream.of(pos);
		}

		@Override
		public PlacementModifierType<?> type() {
			return TYPE;
		}

	}

	private static class RandomYPlacementMod extends PlacementModifier {

		public static final Codec<RandomYPlacementMod> CODEC = RecordCodecBuilder.create((p_242803_0_) -> p_242803_0_.group(Codec.
				INT.fieldOf("y").orElse(0).forGetter(c -> c.y)).apply(p_242803_0_, RandomYPlacementMod::new));

		public static PlacementModifierType<RandomYPlacementMod> TYPE;

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
			return TYPE;
		}

	}

	public static final RegistryObject<Feature<BooleanFeatureConfig>> SPIRE = REGISTRY.register("spire", () -> new Feature<>(BooleanFeatureConfig.CODEC) {
		@Override
		public boolean place(FeaturePlaceContext<BooleanFeatureConfig> context) {
			BlockPos.MutableBlockPos pos = context.origin().mutable();
			if (pos.getX() <= 48 && pos.getX() >= -48 && pos.getZ() <= 48 && pos.getZ() >= -48)
				return false;
			int base = pos.getY();
			int length = context.random().nextInt(25) + 5;
			boolean canGen = false;
			while ((context.config().get() ? base + length < VoidscapeSeededBiomeProvider.LAYERS[0] : base < 255 - length) &&

					!(canGen = checkForRoom(context.level(), pos.set(pos.getX(), context.config().get() ? base + length : base, pos.getZ()), length, context.config().get())))
				base++;
			if (canGen) {
				genSpire(context.level(), pos.set(pos.getX(), base + length * (context.config().get() ? 0 : 1), pos.getZ()), length, context.random(), 5, context.config().get());
				context.level().setBlock(pos, (context.config().get() ? ModBlocks.ANTIROCK : ModBlocks.THUNDERROCK).get().defaultBlockState(), 16 | 2);
				return true;
			}
			return false;
		}

		private boolean checkForRoom(WorldGenLevel level, BlockPos pos, int len, boolean invert) {
			if (!level.getBlockState(pos).is(Blocks.BEDROCK))
				return false;
			for (int i = 0; i < len; i++) {
				BlockPos p = invert ? pos.below(i + 1) : pos.above(i + 1);
				if (p.getY() <= 0)
					return false;
				if (!level.getBlockState(p).isAir())
					return false;
			}
			return true;
		}

		private void genSpire(WorldGenLevel level, BlockPos pos, int len, RandomSource rand, int chance, boolean invert) {
			final int flag = 16 | 2;
			for (int y = len; y > 0; y--) {
				level.setBlock(invert ? pos.above(len - y) : pos.below(len - y), Blocks.BEDROCK.defaultBlockState(), flag);
				if (rand.nextInt(chance) == 0) {
					Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
					if (level.getBlockState(pos.relative(dir)).isAir()) {
						genSpire(level, (invert ? pos.above(len - y) : pos.below(len - y)).relative(dir), y, rand, chance * 2, invert);
					}
				}
			}
		}
	});

	public static final RegistryObject<Feature<FluidFeatureConfig>> FLUID = REGISTRY.register("fluid", () -> new Feature<>(FluidFeatureConfig.CODEC) {
		@Override
		public boolean place(FeaturePlaceContext<FluidFeatureConfig> context) {
			context.level().setBlock(context.origin(), context.config().state.createLegacyBlock(), 2);
			context.level().scheduleTick(context.origin(), context.config().state.getType(), 0);
			return true;
		}
	});

	public static final RegistryObject<Feature<ClusterConfig>> CLUSTER = REGISTRY.register("cluster", () -> new Feature<>(ClusterConfig.CODEC) {
		@Override
		public boolean place(FeaturePlaceContext<ClusterConfig> context) {
			boolean flag = false;
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						BlockPos pos = context.origin().offset(x, y, z);
						if (context.random().nextFloat() <= context.config().chance && place(context.level(), pos, context.config().provider.getState(context.random(), pos), context.config().predicate))
							flag = true;
					}
				}
			}
			return flag;
		}

		private boolean place(WorldGenLevel level, BlockPos pos, BlockState state, BlockPredicate predicate) {
			if (predicate.test(level, pos) && state.canSurvive(level, pos)) {
				if (state.getBlock() instanceof DoublePlantBlock) {
					if (!level.isEmptyBlock(pos.above())) {
						return false;
					}
					DoublePlantBlock.placeAt(level, state, pos, 2);
				} else {
					level.setBlock(pos, state, 2);
				}
				return true;
			} else {
				return false;
			}
		}
	});

	@Override
	public void init(IEventBus bus) {
		bus.addListener((Consumer<RegisterEvent>) event -> {
			SeekDownPlacementMod.TYPE = registerPlacementMod("seek", SeekDownPlacementMod.CODEC);
			AirAbovePlacementMod.TYPE = registerPlacementMod("air_above", AirAbovePlacementMod.CODEC);
			NotAirBelowPlacementMod.TYPE = registerPlacementMod("not_air_below", NotAirBelowPlacementMod.CODEC);
			RandomYPlacementMod.TYPE = registerPlacementMod("random_y", RandomYPlacementMod.CODEC);
		});
	}

	private static <T extends PlacementModifier> PlacementModifierType<T> registerPlacementMod(String name, Codec<T> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(Voidscape.MODID, name), () -> codec);
	}

}
