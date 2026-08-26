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
import tamaized.voidscape.registry.ModItemProperties;

@Component
public class ImposterBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> FRAGILE_VOIDIC_CRYSTAL_BLOCK = RegUtil.register(Registries.BLOCK, "fragile_voidic_crystal_block",
		(id) -> new Block(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(3F, 1200.0F)
			.noLootTable()
		)
	);
	public final DeferredHolder<Item, Item> FRAGILE_VOIDIC_CRYSTAL_BLOCK_ITEM = RegUtil.register(Registries.ITEM, FRAGILE_VOIDIC_CRYSTAL_BLOCK.getId().getPath(),
		(id) -> new BlockItem(
			FRAGILE_VOIDIC_CRYSTAL_BLOCK.get(),
			itemProperties.BLOCK_LAVA_IMMUNE.apply(id)
		)
	);

}
