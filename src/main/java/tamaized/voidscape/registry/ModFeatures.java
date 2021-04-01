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
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class ModFeatures {

	private static final DeferredRegister<Feature<?>> REGISTRY = RegUtil.create(ForgeRegistries.FEATURES);

	public static final RegistryObject<Feature<NoFeatureConfig>> THUNDER_SPIRE = REGISTRY.register("thunderspire", () -> new Feature<NoFeatureConfig>(NoFeatureConfig.CODEC) {
		@Override
		public boolean place(ISeedReader level, ChunkGenerator generator, Random rand, BlockPos bp, NoFeatureConfig config) {
			BlockPos.Mutable pos = bp.mutable();
			int base = pos.getY();
			int length = rand.nextInt(25) + 5;
			while (base < 255 - length && !checkForRoom(level, pos.set(pos.getX(), base, pos.getZ()), length))
				base++;
			if (checkForRoom(level, pos, length)) {
				genSpire(level, pos.set(pos.getX(), base + length, pos.getZ()), length, rand, 5);
				level.setBlock(pos, ModBlocks.THUNDERROCK.get().defaultBlockState(), 16 | 2);
			}
			return false;
		}

		private boolean checkForRoom(ISeedReader level, BlockPos pos, int len) {
			if (!level.getBlockState(pos).is(Blocks.BEDROCK))
				return false;
			for (int i = 0; i < len; i++) {
				if (!level.getBlockState(pos.above(i + 1)).isAir())
					return false;
			}
			return true;
		}

		private void genSpire(ISeedReader level, BlockPos pos, int len, Random rand, int chance) {
			final int flag = 16 | 2;
			for (int y = len; y > 0; y--) {
				level.setBlock(pos.below(len - y), Blocks.BEDROCK.defaultBlockState(), flag);
				if (rand.nextInt(chance) == 0) {
					Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
					if (level.getBlockState(pos.relative(dir)).isAir()) {
						genSpire(level, pos.below(len - y).relative(dir), y, rand, chance * 2);
					}
				}
			}
		}
	});

	public static final LazyValue<ConfiguredFeature<?, ?>> THUNDER_SPIRE_CONFIGURED = RegUtil.
			registerConfiguredFeature(THUNDER_SPIRE, NoFeatureConfig.INSTANCE, IDecoratable::squared);

	static void classload() {
	}

}
