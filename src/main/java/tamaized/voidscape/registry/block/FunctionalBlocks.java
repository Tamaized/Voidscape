package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
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

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final Supplier<PortalBlock> PORTAL = REGISTRY.register("portal", () -> new PortalBlock(Block.Properties.of()
		.sound(SoundType.AMETHYST)
		.mapColor(MapColor.COLOR_BLACK)
		.strength(-1.0F, 3600000.0F)
		.noOcclusion()
		.noLootTable()
		.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
	));

	public final DeferredHolder<Block, Block> VERY_DRIPPY_DRIPSTONE = REGISTRY.register("very_drippy_dripstone", () -> new VeryDrippyDripstoneBlock(Block.Properties.of()
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
	public final Supplier<Item> VERY_DRIPPY_DRIPSTONE_ITEM = REGISTRY_ITEM.register(VERY_DRIPPY_DRIPSTONE.getId().getPath(), () -> new BlockItem(
		VERY_DRIPPY_DRIPSTONE.get(),
		itemProperties.LAVA_IMMUNE.get()
	));

}
