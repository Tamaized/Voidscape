package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.block.BlockDefuser;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.block.BlockLiquifier;
import tamaized.voidscape.block.BlockPortal;
import tamaized.voidscape.entity.EntityAntiBolt;

import java.util.Random;

public class ModBlocks implements RegistryClass {

	private static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);

	public static final RegistryObject<Block> VOIDIC_CRYSTAL_ORE = REGISTRY.register("voidic_crystal_ore", () -> new Block(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()) {
		@Override
		public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
			boolean flag = super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
			world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), world.isClientSide() ? 11 : 3);
			return flag;
		}
	});
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_ORE_ITEM = ModItems.REGISTRY
			.register(VOIDIC_CRYSTAL_ORE.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_ORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> VOIDIC_CRYSTAL_BLOCK = REGISTRY.register("voidic_crystal_block", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BLOCK_ITEM = ModItems.REGISTRY
			.register(VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> FRAGILE_VOIDIC_CRYSTAL_BLOCK = REGISTRY.register("fragile_voidic_crystal_block", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.noLootTable()));
	public static final RegistryObject<Item> FRAGILE_VOIDIC_CRYSTAL_BLOCK_ITEM = ModItems.REGISTRY
			.register(FRAGILE_VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(FRAGILE_VOIDIC_CRYSTAL_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDERROCK = REGISTRY.register("thunderrock", () -> new Block(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.lightLevel(state -> 15)
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			LightningBolt lit = EntityType.LIGHTNING_BOLT.create(level);
			if (lit != null) {
				lit.moveTo(Vec3.atBottomCenterOf(pos));
				level.addFreshEntity(lit);
			}
		}

		@Override
		public boolean isRandomlyTicking(BlockState state) {
			return true;
		}
	});
	public static final RegistryObject<Item> THUNDERROCK_ITEM = ModItems.REGISTRY
			.register(THUNDERROCK.getId().getPath(), () -> new BlockItem(THUNDERROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ANTIROCK = REGISTRY.register("antirock", () -> new Block(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			EntityAntiBolt lit = ModEntities.ANTI_BOLT.get().create(level);
			if (lit != null) {
				lit.moveTo(Vec3.atBottomCenterOf(pos).subtract(0, 0.01F, 0));
				level.addFreshEntity(lit);
			}
		}

		@Override
		public boolean isRandomlyTicking(BlockState state) {
			return true;
		}
	});
	public static final RegistryObject<Item> ANTIROCK_ITEM = ModItems.REGISTRY
			.register(ANTIROCK.getId().getPath(), () -> new BlockItem(ANTIROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> NULL_BLACK = REGISTRY.register("null_black", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)));
	public static final RegistryObject<Item> NULL_BLACK_ITEM = ModItems.REGISTRY
			.register(NULL_BLACK.getId().getPath(), () -> new BlockItem(NULL_BLACK.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_WHITE = REGISTRY.register("null_white", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)));
	public static final RegistryObject<Item> NULL_WHITE_ITEM = ModItems.REGISTRY
			.register(NULL_WHITE.getId().getPath(), () -> new BlockItem(NULL_WHITE.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<BlockPortal> PORTAL = REGISTRY.register("portal", () -> new BlockPortal(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noOcclusion()
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)));

	public static final RegistryObject<Block> PLANT = REGISTRY.register("plant", () -> new BlockEtherealPlant(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.offsetType(BlockBehaviour.OffsetType.XYZ)));
	public static final RegistryObject<Item> PLANT_ITEM = ModItems.REGISTRY
			.register(PLANT.getId().getPath(), () -> new BlockItem(PLANT.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> MACHINE_CORE = REGISTRY.register("machine_core", () -> new Block(Block.Properties.of()
			.sound(SoundType.CANDLE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(1F, 1F)
			.noOcclusion()
			.isValidSpawn((t1, t2, t3, t4) -> false)){
		private static final VoxelShape SHAPE = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D);
		@Override
		public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
			return SHAPE;
		}
	});
	public static final RegistryObject<Item> MACHINE_CORE_ITEM = ModItems.REGISTRY
			.register(MACHINE_CORE.getId().getPath(), () -> new BlockItem(MACHINE_CORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> MACHINE_LIQUIFIER = REGISTRY.register("machine_liquifier", () -> new BlockLiquifier(Block.Properties.of()
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.COLOR_RED)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)));
	public static final RegistryObject<Item> MACHINE_LIQUIFIER_ITEM = ModItems.REGISTRY
			.register(MACHINE_LIQUIFIER.getId().getPath(), () -> new BlockItem(MACHINE_LIQUIFIER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> MACHINE_DEFUSER = REGISTRY.register("machine_defuser", () -> new BlockDefuser(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)));
	public static final RegistryObject<Item> MACHINE_DEFUSER_ITEM = ModItems.REGISTRY
			.register(MACHINE_DEFUSER.getId().getPath(), () -> new BlockItem(MACHINE_DEFUSER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	@Override
	public void init(IEventBus bus) {

	}
}
