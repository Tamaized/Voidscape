package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.PortalBlock;
import tamaized.voidscape.block.VeryDrippyDripstoneBlock;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class FunctionalBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	public final Supplier<PortalBlock> PORTAL = RegUtil.register(Registries.BLOCK, "portal",
		(id) -> new PortalBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.AMETHYST)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noOcclusion()
			.noLootTable()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);

	public final DeferredHolder<Block, Block> VERY_DRIPPY_DRIPSTONE = RegUtil.register(Registries.BLOCK, "very_drippy_dripstone",
		(id) -> new VeryDrippyDripstoneBlock(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
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
			.isRedstoneConductor((_, _, _) -> false)
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> VERY_DRIPPY_DRIPSTONE_ITEM = RegUtil.register(Registries.ITEM, VERY_DRIPPY_DRIPSTONE.getId().getPath(),
		(id) -> new BlockItem(
			VERY_DRIPPY_DRIPSTONE.get(),
			itemProperties.LAVA_IMMUNE.apply(id)
		)
	);

}
