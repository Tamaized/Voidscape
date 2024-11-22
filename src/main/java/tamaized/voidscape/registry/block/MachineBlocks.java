package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.*;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class MachineBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> MACHINE_CORE = REGISTRY.register("machine_core", () -> new CustomVoxelShapeBlock(
		Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D),
		Block.Properties.of()
			.sound(SoundType.CANDLE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(1F, 1200.0F)
			.noOcclusion()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_CORE_ITEM = REGISTRY_ITEM.register(MACHINE_CORE.getId().getPath(), () -> new BlockItem(
		MACHINE_CORE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_LIQUIFIER = REGISTRY.register("machine_liquifier", () -> new LiquifierBlock(Block.Properties.of()
		.sound(SoundType.BONE_BLOCK)
		.mapColor(MapColor.COLOR_RED)
		.strength(3F, 1200.0F)
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_LIQUIFIER_ITEM = REGISTRY_ITEM.register(MACHINE_LIQUIFIER.getId().getPath(), () -> new BlockItem(
		MACHINE_LIQUIFIER.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_DEFUSER = REGISTRY.register("machine_defuser", () -> new DefuserBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_PURPLE)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_DEFUSER_ITEM = REGISTRY_ITEM.register(MACHINE_DEFUSER.getId().getPath(), () -> new BlockItem(
		MACHINE_DEFUSER.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_GERMINATOR = REGISTRY.register("machine_germinator", () -> new GerminatorBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_LIGHT_GREEN)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_GERMINATOR_ITEM = REGISTRY_ITEM.register(MACHINE_GERMINATOR.getId().getPath(), () -> new BlockItem(
		MACHINE_GERMINATOR.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_WELL = REGISTRY.register("machine_well", () -> new WellBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_BLUE)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_WELL_ITEM = REGISTRY_ITEM.register(MACHINE_WELL.getId().getPath(), () -> new BlockItem(
		MACHINE_WELL.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_COOP = REGISTRY.register("machine_coop", () -> new CoopBlock(Block.Properties.of()
		.sound(SoundType.BONE_BLOCK)
		.mapColor(MapColor.TERRACOTTA_WHITE)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_COOP_ITEM = REGISTRY_ITEM.register(MACHINE_COOP.getId().getPath(), () -> new BlockItem(
		MACHINE_COOP.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_HATCHERY = REGISTRY.register("machine_hatchery", () -> new HatcheryBlock(Block.Properties.of()
		.sound(SoundType.STONE)
		.mapColor(MapColor.TERRACOTTA_WHITE)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_HATCHERY_ITEM = REGISTRY_ITEM.register(MACHINE_HATCHERY.getId().getPath(), () -> new BlockItem(
		MACHINE_HATCHERY.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_INFUSER = REGISTRY.register("machine_infuser", () -> new InfuserBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_ORANGE)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_INFUSER_ITEM = REGISTRY_ITEM.register(MACHINE_INFUSER.getId().getPath(), () -> new BlockItem(
		MACHINE_INFUSER.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

	public final DeferredHolder<Block, Block> MACHINE_COLLECTOR = REGISTRY.register("machine_collector", () -> new CollectorBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_PINK)
		.strength(3F, 1200.0F)
		.noOcclusion()
		.requiresCorrectToolForDrops()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> MACHINE_COLLECTOR_ITEM = REGISTRY_ITEM.register(MACHINE_COLLECTOR.getId().getPath(), () -> new BlockItem(
		MACHINE_COLLECTOR.get(),
		itemProperties.LAVA_IMMUNE.get()
	));


}
