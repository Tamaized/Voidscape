package tamaized.voidscape.registry;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IDecoratable;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.world.BooleanFeatureConfig;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

import java.util.Random;

public class ModFeatures {

	private static final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(ForgeRegistries.FEATURES);

	public static final RegistryObject<Feature<BooleanFeatureConfig>> SPIRE = REGISTRY.register("spire", () -> new Feature<BooleanFeatureConfig>(BooleanFeatureConfig.CODEC) {
		@Override
		public boolean place(ISeedReader level, ChunkGenerator generator, Random rand, BlockPos bp, BooleanFeatureConfig config) {
			BlockPos.Mutable pos = bp.mutable();
			int base = pos.getY();
			int length = rand.nextInt(25) + 5;
			boolean canGen = false;
			while ((config.get() ? base + length < VoidscapeSeededBiomeProvider.LAYERS[0] : base < 255 - length) &&

					!(canGen = checkForRoom(level, pos.set(pos.getX(), config.get() ? base + length : base, pos.getZ()), length, config.get())))
				base++;
			if (canGen) {
				genSpire(level, pos.set(pos.getX(), base + length * (config.get() ? 0 : 1), pos.getZ()), length, rand, 5, config.get());
				level.setBlock(pos, (config.get() ? ModBlocks.ANTIROCK : ModBlocks.THUNDERROCK).get().defaultBlockState(), 16 | 2);
			}
			return false;
		}

		private boolean checkForRoom(ISeedReader level, BlockPos pos, int len, boolean invert) {
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

		private void genSpire(ISeedReader level, BlockPos pos, int len, Random rand, int chance, boolean invert) {
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

	public static final LazyValue<ConfiguredFeature<?, ?>> THUNDER_SPIRE_CONFIGURED = RegUtil.
			registerConfiguredFeature(SPIRE, new BooleanFeatureConfig(false), IDecoratable::squared);
	public static final LazyValue<ConfiguredFeature<?, ?>> ANTI_SPIRE_CONFIGURED = RegUtil.
			registerConfiguredFeature(SPIRE, new BooleanFeatureConfig(true), IDecoratable::squared);

	static void classload() {
	}

}
