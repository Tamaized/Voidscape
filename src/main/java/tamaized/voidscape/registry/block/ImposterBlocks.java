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
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class ImposterBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> FRAGILE_VOIDIC_CRYSTAL_BLOCK = REGISTRY.register("fragile_voidic_crystal_block", () -> new Block(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_PURPLE)
		.strength(3F, 1200.0F)
		.noLootTable()
	));
	public final Supplier<Item> FRAGILE_VOIDIC_CRYSTAL_BLOCK_ITEM = REGISTRY_ITEM.register(FRAGILE_VOIDIC_CRYSTAL_BLOCK.getId().getPath(), () -> new BlockItem(
		FRAGILE_VOIDIC_CRYSTAL_BLOCK.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

}
