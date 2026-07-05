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

	public final DeferredHolder<Block, Block> VOID = RegUtil.register(Registries.BLOCK, "ethereal_fruit_void",
		() -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final Supplier<Item> VOID_ITEM = RegUtil.register(Registries.ITEM, VOID.getId().getPath() + "_block",
		() -> new BlockItem(
			VOID.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> NULL = RegUtil.register(Registries.BLOCK, "ethereal_fruit_null",
		() -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_LIGHT_GRAY)
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final Supplier<Item> NULL_ITEM = RegUtil.register(Registries.ITEM, NULL.getId().getPath() + "_block",
		() -> new BlockItem(
			NULL.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> OVERWORLD = RegUtil.register(Registries.BLOCK, "ethereal_fruit_overworld",
		() -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_CYAN)
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final Supplier<Item> OVERWORLD_ITEM = RegUtil.register(Registries.ITEM, OVERWORLD.getId().getPath() + "_block",
		() -> new BlockItem(
			OVERWORLD.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> NETHER = RegUtil.register(Registries.BLOCK, "ethereal_fruit_nether",
		() -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_RED)
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final Supplier<Item> NETHER_ITEM = RegUtil.register(Registries.ITEM, NETHER.getId().getPath() + "_block",
		() -> new BlockItem(
			NETHER.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> END = RegUtil.register(Registries.BLOCK, "ethereal_fruit_end",
		() -> new EtherealPlantBlock(Block.Properties.of()
			.sound(SoundType.CROP)
			.mapColor(MapColor.COLOR_PINK)
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final Supplier<Item> END_ITEM = RegUtil.register(Registries.ITEM, END.getId().getPath() + "_block",
		() -> new BlockItem(
			END.get(),
			itemProperties.DEFAULT.get()
		)
	);

}
