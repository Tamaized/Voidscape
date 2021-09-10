package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Decoratable;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.world.BooleanFeatureConfig;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

import java.util.Random;

public class ModFeatures {

	private static final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(ForgeRegistries.FEATURES);

	public static final RegistryObject<Feature<BooleanFeatureConfig>> SPIRE = REGISTRY.register("spire", () -> new Feature<>(BooleanFeatureConfig.CODEC) {
		@Override
		public boolean place(FeaturePlaceContext<BooleanFeatureConfig> context) {
			BlockPos.MutableBlockPos pos = context.origin().mutable();
			int base = pos.getY();
			int length = context.random().nextInt(25) + 5;
			boolean canGen = false;
			while ((context.config().get() ? base + length < VoidscapeSeededBiomeProvider.LAYERS[0] : base < 255 - length) &&

					!(canGen = checkForRoom(context.level(), pos.set(pos.getX(), context.config().get() ? base + length : base, pos.getZ()), length, context.config().get())))
				base++;
			if (canGen) {
				genSpire(context.level(), pos.set(pos.getX(), base + length * (context.config().get() ? 0 : 1), pos.getZ()), length, context.random(), 5, context.config().get());
				context.level().setBlock(pos, (context.config().get() ? ModBlocks.ANTIROCK : ModBlocks.THUNDERROCK).get().defaultBlockState(), 16 | 2);
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

		private void genSpire(WorldGenLevel level, BlockPos pos, int len, Random rand, int chance, boolean invert) {
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

	public static final LazyLoadedValue<ConfiguredFeature<?, ?>> THUNDER_SPIRE_CONFIGURED = RegUtil.
			registerConfiguredFeature(SPIRE, new BooleanFeatureConfig(false), Decoratable::squared);
	public static final LazyLoadedValue<ConfiguredFeature<?, ?>> ANTI_SPIRE_CONFIGURED = RegUtil.
			registerConfiguredFeature(SPIRE, new BooleanFeatureConfig(true), Decoratable::squared);

	static void classload() {
	}

}
