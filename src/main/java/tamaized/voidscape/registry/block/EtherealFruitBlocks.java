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

	public final DeferredHolder<Block, Block> ETHEREAL_FRUIT_VOID = REGISTRY.register("ethereal_fruit_void", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_PURPLE)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> ETHEREAL_FRUIT_VOID_ITEM = REGISTRY_ITEM.register(ETHEREAL_FRUIT_VOID.getId().getPath() + "_block", () -> new BlockItem(
		ETHEREAL_FRUIT_VOID.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> ETHEREAL_FRUIT_NULL = REGISTRY.register("ethereal_fruit_null", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_LIGHT_GRAY)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> ETHEREAL_FRUIT_NULL_ITEM = REGISTRY_ITEM.register(ETHEREAL_FRUIT_NULL.getId().getPath() + "_block", () -> new BlockItem(
		ETHEREAL_FRUIT_NULL.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> ETHEREAL_FRUIT_OVERWORLD = REGISTRY.register("ethereal_fruit_overworld", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_CYAN)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> ETHEREAL_FRUIT_OVERWORLD_ITEM = REGISTRY_ITEM
		.register(ETHEREAL_FRUIT_OVERWORLD.getId().getPath() + "_block", () -> new BlockItem(
			ETHEREAL_FRUIT_OVERWORLD.get(),
			itemProperties.DEFAULT.get()
		));

	public final DeferredHolder<Block, Block> ETHEREAL_FRUIT_NETHER = REGISTRY.register("ethereal_fruit_nether", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_RED)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> ETHEREAL_FRUIT_NETHER_ITEM = REGISTRY_ITEM
		.register(ETHEREAL_FRUIT_NETHER.getId().getPath() + "_block", () -> new BlockItem(
			ETHEREAL_FRUIT_NETHER.get(),
			itemProperties.DEFAULT.get()
		));

	public final DeferredHolder<Block, Block> ETHEREAL_FRUIT_END = REGISTRY.register("ethereal_fruit_end", () -> new EtherealPlantBlock(Block.Properties.of()
		.sound(SoundType.CROP)
		.mapColor(MapColor.COLOR_PINK)
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> ETHEREAL_FRUIT_END_ITEM = REGISTRY_ITEM
		.register(ETHEREAL_FRUIT_END.getId().getPath() + "_block", () -> new BlockItem(
			ETHEREAL_FRUIT_END.get(),
			itemProperties.DEFAULT.get()
		));

}
