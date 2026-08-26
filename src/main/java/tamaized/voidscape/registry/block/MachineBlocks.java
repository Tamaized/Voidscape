package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
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

	public final DeferredHolder<Block, Block> MACHINE_CORE = RegUtil.register(Registries.BLOCK, "machine_core",
		(id) -> new CustomVoxelShapeBlock(
			Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D),
			Block.Properties.of()
				.setId(ResourceKey.create(Registries.BLOCK, id))
				.sound(SoundType.CANDLE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(1F, 1200.0F)
				.noOcclusion()
				.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_CORE_ITEM = RegUtil.register(Registries.ITEM, MACHINE_CORE.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_CORE.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_LIQUIFIER = RegUtil.register(Registries.BLOCK, "machine_liquifier",
		(id) -> new LiquifierBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.COLOR_RED)
			.strength(3F, 1200.0F)
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_LIQUIFIER_ITEM = RegUtil.register(Registries.ITEM, MACHINE_LIQUIFIER.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_LIQUIFIER.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_DEFUSER = RegUtil.register(Registries.BLOCK, "machine_defuser",
		(id) -> new DefuserBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_DEFUSER_ITEM = RegUtil.register(Registries.ITEM, MACHINE_DEFUSER.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_DEFUSER.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_GERMINATOR = RegUtil.register(Registries.BLOCK, "machine_germinator",
		(id) -> new GerminatorBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_GERMINATOR_ITEM = RegUtil.register(Registries.ITEM, MACHINE_GERMINATOR.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_GERMINATOR.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_WELL = RegUtil.register(Registries.BLOCK, "machine_well",
		(id) -> new WellBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLUE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_WELL_ITEM = RegUtil.register(Registries.ITEM, MACHINE_WELL.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_WELL.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_COOP = RegUtil.register(Registries.BLOCK, "machine_coop",
		(id) -> new CoopBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.BONE_BLOCK)
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_COOP_ITEM = RegUtil.register(Registries.ITEM, MACHINE_COOP.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_COOP.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_HATCHERY = RegUtil.register(Registries.BLOCK, "machine_hatchery",
		(id) -> new HatcheryBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.STONE)
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_HATCHERY_ITEM = RegUtil.register(Registries.ITEM, MACHINE_HATCHERY.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_HATCHERY.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_INFUSER = RegUtil.register(Registries.BLOCK, "machine_infuser",
		(id) -> new InfuserBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_ORANGE)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_INFUSER_ITEM = RegUtil.register(Registries.ITEM, MACHINE_INFUSER.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_INFUSER.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> MACHINE_COLLECTOR = RegUtil.register(Registries.BLOCK, "machine_collector",
		(id) -> new CollectorBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PINK)
			.strength(3F, 1200.0F)
			.noOcclusion()
			.requiresCorrectToolForDrops()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> MACHINE_COLLECTOR_ITEM = RegUtil.register(Registries.ITEM, MACHINE_COLLECTOR.getId().getPath(),
		(id) -> new BlockItem(
			MACHINE_COLLECTOR.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);


}
