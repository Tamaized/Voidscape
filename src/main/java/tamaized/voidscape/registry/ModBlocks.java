package tamaized.voidscape.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.*;
import tamaized.voidscape.block.LightningAttractorBlock;
import tamaized.voidscape.entity.AntiBoltEntity;

import java.util.function.Supplier;

@Component
public class ModBlocks {

	public final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);

	public static final Supplier<PortalBlock> PORTAL = REGISTRY.register("portal", () -> new PortalBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noOcclusion()
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
	));

	// Crops

	public static final DeferredHolder<Block, Block> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final Supplier<Item> ETHEREAL_FRUIT_VOID_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_VOID.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_VOID.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final DeferredHolder<Block, Block> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_BLACK)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final Supplier<Item> ETHEREAL_FRUIT_NULL_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_NULL.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_NULL.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final DeferredHolder<Block, Block> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_CYAN)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final Supplier<Item> ETHEREAL_FRUIT_OVERWORLD_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_OVERWORLD.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_OVERWORLD.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final DeferredHolder<Block, Block> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_RED)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final Supplier<Item> ETHEREAL_FRUIT_NETHER_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_NETHER.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_NETHER.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	public static final DeferredHolder<Block, Block> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PINK)
			.noCollission()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final Supplier<Item> ETHEREAL_FRUIT_END_ITEM = ModItems.REGISTRY
			.register(ETHEREAL_FRUIT_END.getId().getPath() + "_block", () -> new BlockItem(ETHEREAL_FRUIT_END.get(), ModItems.ItemProps.DEFAULT.properties().get()));

	/// Machines

	public static final DeferredHolder<Block, Block> MACHINE_CORE = REGISTRY.register("machine_core", () -> new Block(Block.Properties.of()
			.sound(SoundType.CANDLE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(1F, 1200.0F)
			.noOcclusion()
			.isValidSpawn((t1, t2, t3, t4) -> false)) {
		private static final VoxelShape SHAPE = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D);
		@Override
		public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
			return SHAPE;
		}
	});
	public static final Supplier<Item> MACHINE_CORE_ITEM = ModItems.REGISTRY
			.register(MACHINE_CORE.getId().getPath(), () -> new BlockItem(MACHINE_CORE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_LIQUIFIER = REGISTRY.register("machine_liquifier", () -> new LiquifierBlock(Block.Properties.of()
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.COLOR_RED)
			.strength(3F, 1200.0F)
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_LIQUIFIER_ITEM = ModItems.REGISTRY
			.register(MACHINE_LIQUIFIER.getId().getPath(), () -> new BlockItem(MACHINE_LIQUIFIER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_DEFUSER = REGISTRY.register("machine_defuser", () -> new DefuserBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_DEFUSER_ITEM = ModItems.REGISTRY
			.register(MACHINE_DEFUSER.getId().getPath(), () -> new BlockItem(MACHINE_DEFUSER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_GERMINATOR = REGISTRY.register("machine_germinator", () -> new GerminatorBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_GERMINATOR_ITEM = ModItems.REGISTRY
			.register(MACHINE_GERMINATOR.getId().getPath(), () -> new BlockItem(MACHINE_GERMINATOR.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_WELL = REGISTRY.register("machine_well", () -> new WellBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLUE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_WELL_ITEM = ModItems.REGISTRY
			.register(MACHINE_WELL.getId().getPath(), () -> new BlockItem(MACHINE_WELL.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_COOP = REGISTRY.register("machine_coop", () -> new CoopBlock(Block.Properties.of()
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_COOP_ITEM = ModItems.REGISTRY
			.register(MACHINE_COOP.getId().getPath(), () -> new BlockItem(MACHINE_COOP.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_HATCHERY = REGISTRY.register("machine_hatchery", () -> new HatcheryBlock(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_HATCHERY_ITEM = ModItems.REGISTRY
			.register(MACHINE_HATCHERY.getId().getPath(), () -> new BlockItem(MACHINE_HATCHERY.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_INFUSER = REGISTRY.register("machine_infuser", () -> new InfuserBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_INFUSER_ITEM = ModItems.REGISTRY
			.register(MACHINE_INFUSER.getId().getPath(), () -> new BlockItem(MACHINE_INFUSER.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> MACHINE_COLLECTOR = REGISTRY.register("machine_collector", () -> new CollectorBlock(Block.Properties.of()
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PINK)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> MACHINE_COLLECTOR_ITEM = ModItems.REGISTRY
			.register(MACHINE_COLLECTOR.getId().getPath(), () -> new BlockItem(MACHINE_COLLECTOR.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final DeferredHolder<Block, Block> VERY_DRIPPY_DRIPSTONE = REGISTRY.register("very_drippy_dripstone", () -> new VeryDrippyDripstoneBlock(Block.Properties.of()
			.mapColor(MapColor.TERRACOTTA_PURPLE)
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASEDRUM)
			.noOcclusion()
			.sound(SoundType.POINTED_DRIPSTONE)
			.randomTicks()
			.strength(1.5F, 3.0F)
			.dynamicShape()
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.pushReaction(PushReaction.DESTROY)
			.isRedstoneConductor((state, level, pos) -> false)
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final Supplier<Item> VERY_DRIPPY_DRIPSTONE_ITEM = ModItems.REGISTRY
			.register(VERY_DRIPPY_DRIPSTONE.getId().getPath(), () -> new BlockItem(VERY_DRIPPY_DRIPSTONE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

}
