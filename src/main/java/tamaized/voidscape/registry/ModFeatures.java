package tamaized.voidscape.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.world.BooleanFeatureConfig;
import tamaized.voidscape.world.VoidscapeSeededBiomeProvider;

import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ModFeatures {

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
		public Stream<BlockPos> getPositions(PlacementContext context, Random random, BlockPos pos) {
			BlockPos.MutableBlockPos seek = pos.mutable().move(Direction.UP, random.nextInt(15));
			BlockPos.MutableBlockPos check = seek.mutable().move(Direction.DOWN, 1);
			while (seek.getY() > context.getLevel().getMinBuildHeight() && context.getLevel().getBlockState(check_below ? check : seek).isAir()) {
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

	static void classload(IEventBus bus) {
		bus.addGenericListener(Feature.class, (Consumer<RegistryEvent.Register<Feature<?>>>) event -> {
			SeekDownPlacementMod.TYPE = registerPlacementMod("seek", SeekDownPlacementMod.CODEC);
		});
	}

	private static <T extends PlacementModifier> PlacementModifierType<T> registerPlacementMod(String name, Codec<T> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(Voidscape.MODID, name), () -> codec);
	}

}
