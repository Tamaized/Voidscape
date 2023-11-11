package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import tamaized.regutil.RegUtil;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.block.*;
import tamaized.voidscape.entity.AntiBoltEntity;
import tamaized.voidscape.registry.block.ModBlocksThunderForestBiome;

public class ModBlocks implements RegistryClass {

	public static final DeferredRegister<Block> REGISTRY = RegUtil.create(ForgeRegistries.BLOCKS);

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
			.requiresCorrectToolForDrops()
	));
	public static final RegistryObject<Item> VOIDIC_CRYSTAL_BLOCK_ITEM = ModItems.REGISTRY
			.register(VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(VOIDIC_CRYSTAL_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> FRAGILE_VOIDIC_CRYSTAL_BLOCK = REGISTRY.register("fragile_voidic_crystal_block", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.noLootTable()
	));
	public static final RegistryObject<Item> FRAGILE_VOIDIC_CRYSTAL_BLOCK_ITEM = ModItems.REGISTRY
			.register(FRAGILE_VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(FRAGILE_VOIDIC_CRYSTAL_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> TITANITE_ORE = REGISTRY.register("titanite_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
			.sound(SoundType.NETHER_GOLD_ORE)
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.strength(4F, 6F)
			.requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> TITANITE_ORE_ITEM = ModItems.REGISTRY
			.register(TITANITE_ORE.getId().getPath(), () -> new BlockItem(TITANITE_ORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> FLESH_ORE = REGISTRY.register("flesh_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
			.sound(SoundType.HONEY_BLOCK)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(4F, 9F)
			.requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> FLESH_ORE_ITEM = ModItems.REGISTRY
			.register(FLESH_ORE.getId().getPath(), () -> new BlockItem(FLESH_ORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> FLESH_BLOCK = REGISTRY.register("flesh_block", () -> new Block(Block.Properties.of()
			.sound(SoundType.HONEY_BLOCK)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(2F, 2F)));
	public static final RegistryObject<Item> FLESH_BLOCK_ITEM = ModItems.REGISTRY
			.register(FLESH_BLOCK.getId().getPath(), () -> new BlockItem(FLESH_BLOCK.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> STRANGE_ORE = REGISTRY.register("strange_ore", () -> new RequiresVoidToolBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PINK)
			.strength(4F, 12F)
			.requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> STRANGE_ORE_ITEM = ModItems.REGISTRY
			.register(STRANGE_ORE.getId().getPath(), () -> new BlockItem(STRANGE_ORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));


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
			AntiBoltEntity lit = ModEntities.ANTI_BOLT.get().create(level);
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

	public static final RegistryObject<Block> ASTRALROCK = REGISTRY.register("astralrock", () -> new Block(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)) {
		@Override
		public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			if (random.nextBoolean() || level.players().stream().noneMatch(p -> pos.distSqr(p.blockPosition()) <= 10000))
				return;
			AntiBoltEntity lit = ModEntities.ANTI_BOLT.get().create(level);
			if (lit != null) {
				lit.moveTo(Vec3.atBottomCenterOf(pos).subtract(0, 0.01F, 0));
				level.addFreshEntity(lit);
				level.setBlockAndUpdate(pos, CRACKED_ASTRALROCK.get().defaultBlockState());
			}
		}

		@Override
		public boolean isRandomlyTicking(BlockState state) {
			return true;
		}
	});
	public static final RegistryObject<Item> ASTRALROCK_ITEM = ModItems.REGISTRY
			.register(ASTRALROCK.getId().getPath(), () -> new BlockItem(ASTRALROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> CRACKED_ASTRALROCK = REGISTRY.register("cracked_astralrock", () -> new RequiresVoidToolBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(4F, 12F)
			.requiresCorrectToolForDrops()
	));
	public static final RegistryObject<Item> CRACKED_ASTRALROCK_ITEM = ModItems.REGISTRY
			.register(CRACKED_ASTRALROCK.getId().getPath(), () -> new BlockItem(CRACKED_ASTRALROCK.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> NULL_BLACK = REGISTRY.register("null_black", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
	));
	public static final RegistryObject<Item> NULL_BLACK_ITEM = ModItems.REGISTRY
			.register(NULL_BLACK.getId().getPath(), () -> new BlockItem(NULL_BLACK.get(), ModItems.ItemProps.DEFAULT.properties().get()));
	public static final RegistryObject<Block> NULL_WHITE = REGISTRY.register("null_white", () -> new Block(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> true)
	));
	public static final RegistryObject<Item> NULL_WHITE_ITEM = ModItems.REGISTRY
			.register(NULL_WHITE.getId().getPath(), () -> new BlockItem(NULL_WHITE.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<PortalBlock> PORTAL = REGISTRY.register("portal", () -> new PortalBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noOcclusion()
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
	));

	// Crops

	public static final RegistryObject<Block> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> ETHEREAL_FRUIT_VOID_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_VOID.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_VOID.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_BLACK)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> ETHEREAL_FRUIT_NULL_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_NULL.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_NULL.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_CYAN)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> ETHEREAL_FRUIT_OVERWORLD_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_OVERWORLD.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_OVERWORLD.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_RED)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> ETHEREAL_FRUIT_NETHER_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_NETHER.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_NETHER.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final RegistryObject<Block> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PINK)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> ETHEREAL_FRUIT_END_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_END.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_END.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	/// Machines

	public static final RegistryObject<Block> MACHINE_CORE = REGISTRY.register("machine_core", () -> new Block(Block.Properties.of()
			.sound(SoundType.CANDLE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(1F, 1F)
			.noOcclusion()
			.isValidSpawn((t1, t2, t3, t4) -> false)) {
		private static final VoxelShape SHAPE = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D);
		@Override
		public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
			return SHAPE;
		}
	});
	public static final RegistryObject<Item> MACHINE_CORE_ITEM = ModItems.REGISTRY
			.register(MACHINE_CORE.getId().getPath(), () -> new BlockItem(MACHINE_CORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> MACHINE_LIQUIFIER = REGISTRY.register("machine_liquifier", () -> new LiquifierBlock(Block.Properties.of()
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.COLOR_RED)
			.strength(3F, 3F)
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final RegistryObject<Item> MACHINE_LIQUIFIER_ITEM = ModItems.REGISTRY
			.register(MACHINE_LIQUIFIER.getId().getPath(), () -> new BlockItem(MACHINE_LIQUIFIER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> MACHINE_DEFUSER = REGISTRY.register("machine_defuser", () -> new DefuserBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 3F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final RegistryObject<Item> MACHINE_DEFUSER_ITEM = ModItems.REGISTRY
			.register(MACHINE_DEFUSER.getId().getPath(), () -> new BlockItem(MACHINE_DEFUSER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	@Override
	public void init(IEventBus bus) {
		new ModBlocksThunderForestBiome().init(bus);
	}
}
