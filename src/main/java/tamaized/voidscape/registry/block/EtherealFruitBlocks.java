package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.EtherealPlantBlock;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class EtherealFruitBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> VOID = REGISTRY.register("ethereal_fruit_void", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_PURPLE)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> VOID_ITEM = REGISTRY_ITEM.register(VOID.getId().getPath() + "_block", () -> new BlockItem(
		VOID.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> NULL = REGISTRY.register("ethereal_fruit_null", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_LIGHT_GRAY)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> NULL_ITEM = REGISTRY_ITEM.register(NULL.getId().getPath() + "_block", () -> new BlockItem(
		NULL.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_CYAN)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> OVERWORLD_ITEM = REGISTRY_ITEM
		.register(OVERWORLD.getId().getPath() + "_block", () -> new BlockItem(
			OVERWORLD.get(),
			itemProperties.DEFAULT.get()
		));

	public final DeferredHolder<Block, Block> NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_RED)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> NETHER_ITEM = REGISTRY_ITEM
		.register(NETHER.getId().getPath() + "_block", () -> new BlockItem(
			NETHER.get(),
			itemProperties.DEFAULT.get()
		));

	public final DeferredHolder<Block, Block> END = REGISTRY.register("ethereal_fruit_end", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_PINK)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> END_ITEM = REGISTRY_ITEM
		.register(END.getId().getPath() + "_block", () -> new BlockItem(
			END.get(),
			itemProperties.DEFAULT.get()
		));

}
