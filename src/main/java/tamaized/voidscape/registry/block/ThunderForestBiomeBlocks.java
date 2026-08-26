package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.ThunderNyliumBlock;
import tamaized.voidscape.registry.feature.ModConfiguredFeatures;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class ThunderForestBiomeBlocks {

	@Autowired
	private ModItemProperties itemProperties;

	@Autowired
	private ModConfiguredFeatures configuredFeatures;

	public final DeferredHolder<Block, Block> THUNDER_NYLIUM = RegUtil.register(Registries.BLOCK, "thunder_nylium",
		(id) -> new ThunderNyliumBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.NYLIUM)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((_, _, _, _) -> false)
		)
	);
	public final Supplier<Item> THUNDER_NYLIUM_ITEM = RegUtil.register(Registries.ITEM, THUNDER_NYLIUM.getId().getPath(),
		(id) -> new BlockItem(THUNDER_NYLIUM.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final TagKey<Block> TAG_THUNDER_ROOTS_SUPPORTS = TagKey.create(
		Registries.BLOCK,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "supports_thunder_roots")
	);
	public final DeferredHolder<Block, Block> THUNDER_ROOTS = RegUtil.register(Registries.BLOCK, "thunder_roots",
		(id) -> new NetherRootsBlock(TAG_THUNDER_ROOTS_SUPPORTS, BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.ROOTS)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollision()
			.instabreak()
			.replaceable()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
		)
	);
	public final DeferredHolder<Item, BlockItem> THUNDER_ROOTS_ITEM = RegUtil.register(Registries.ITEM, THUNDER_ROOTS.getId().getPath(),
		(id) -> new BlockItem(THUNDER_ROOTS.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);
	public final DeferredHolder<Block, Block> THUNDER_ROOTS_POT = RegUtil.register(Registries.BLOCK, "thunder_roots_pot",
		(id) -> new FlowerPotBlock(
			() -> (FlowerPotBlock) Blocks.FLOWER_POT,
			THUNDER_ROOTS,
			BlockBehaviour.Properties.of()
				.setId(ResourceKey.create(Registries.BLOCK, id))
				.instabreak()
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY)
		)
	);

	public final TagKey<Block> TAG_THUNDER_FUNGUS_SUPPORTS = TagKey.create(
		Registries.BLOCK,
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "supports_thunder_roots")
	);
	public final DeferredHolder<Block, Block> THUNDER_FUNGUS = RegUtil.register(Registries.BLOCK, "thunder_fungus",
		(id) -> new NetherFungusBlock(
			configuredFeatures.THUNDER_FUNGUS,
			THUNDER_NYLIUM.get(),
			TAG_THUNDER_FUNGUS_SUPPORTS,
			BlockBehaviour.Properties.of()
				.setId(ResourceKey.create(Registries.BLOCK, id))
				.sound(SoundType.FUNGUS)
				.mapColor(MapColor.COLOR_PURPLE)
				.noCollision()
				.instabreak()
				.replaceable()
				.pushReaction(PushReaction.DESTROY)
		)
	);
	public final DeferredHolder<Item, BlockItem> THUNDER_FUNGUS_ITEM = RegUtil.register(Registries.ITEM, THUNDER_FUNGUS.getId().getPath(),
		(id) -> new BlockItem(THUNDER_FUNGUS.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_FUNGUS_POT = RegUtil.register(Registries.BLOCK, "thunder_fungus_pot",
		(id) -> new FlowerPotBlock(
			() -> (FlowerPotBlock) Blocks.FLOWER_POT,
			THUNDER_FUNGUS,
			BlockBehaviour.Properties.of()
				.setId(ResourceKey.create(Registries.BLOCK, id))
				.instabreak()
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY)
		)
	);

	public final DeferredHolder<Block, Block> THUNDER_WART = RegUtil.register(Registries.BLOCK, "thunder_wart",
		(id) -> new Block(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.WART_BLOCK)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(1.0F)
		)
	);
	public final DeferredHolder<Item, BlockItem> THUNDER_WART_ITEM = RegUtil.register(Registries.ITEM, THUNDER_WART.getId().getPath(),
		(id) -> new BlockItem(THUNDER_WART.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, GrowingPlantHeadBlock> THUNDER_VINES = RegUtil.register(Registries.BLOCK, "thunder_vines",
		(id) -> new WeepingVinesBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.WEEPING_VINES)
			.mapColor(MapColor.COLOR_PURPLE)
			.randomTicks()
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
		) {
			@Override
			protected Block getBodyBlock() {
				return THUNDER_VINES_PLANT.get();
			}
		}
	);
	public final DeferredHolder<Block, Block> THUNDER_VINES_PLANT = RegUtil.register(Registries.BLOCK, "thunder_vines_plant",
		(id) -> new WeepingVinesPlantBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.WEEPING_VINES)
			.mapColor(MapColor.COLOR_PURPLE)
			.randomTicks()
			.noCollision()
			.instabreak()
			.pushReaction(PushReaction.DESTROY)
		) {
			@Override
			protected GrowingPlantHeadBlock getHeadBlock() {
				return THUNDER_VINES.get();
			}
		}
	);
	public final DeferredHolder<Item, BlockItem> THUNDER_VINES_ITEM = RegUtil.register(Registries.ITEM, THUNDER_VINES.getId().getPath(),
		(id) -> new BlockItem(THUNDER_VINES.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_STEM = RegUtil.register(Registries.BLOCK, "thunder_stem",
		(id) -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_PURPLE)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
		) {
			@Override
			public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {
				if (ItemAbilities.AXE_STRIP == toolAction) {
					return THUNDER_STEM_STRIPPED.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
				}
				return super.getToolModifiedState(state, context, toolAction, simulate);
			}
		}
	);
	public final DeferredHolder<Item, Item> THUNDER_STEM_ITEM = RegUtil.register(Registries.ITEM, THUNDER_STEM.getId().getPath(),
		(id) -> new BlockItem(THUNDER_STEM.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_STEM_STRIPPED = RegUtil.register(Registries.BLOCK, "thunder_stem_stripped",
		(id) -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
		)
	);
	public final DeferredHolder<Item, Item> THUNDER_STEM_STRIPPED_ITEM = RegUtil.register(Registries.ITEM, THUNDER_STEM_STRIPPED.getId().getPath(),
		(id) -> new BlockItem(THUNDER_STEM_STRIPPED.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_HYPHAE = RegUtil.register(Registries.BLOCK, "thunder_hyphae",
		(id) -> new Block(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_PURPLE)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
		) {
			@Override
			public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {
				if (ItemAbilities.AXE_STRIP == toolAction) {
					return THUNDER_HYPHAE_STRIPPED.get().defaultBlockState();
				}
				return super.getToolModifiedState(state, context, toolAction, simulate);
			}
		}
	);
	public final DeferredHolder<Item, Item> THUNDER_HYPHAE_ITEM = RegUtil.register(Registries.ITEM, THUNDER_HYPHAE.getId().getPath(),
		(id) -> new BlockItem(THUNDER_HYPHAE.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_HYPHAE_STRIPPED = RegUtil.register(Registries.BLOCK, "thunder_hyphae_stripped",
		(id) -> new Block(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
		)
	);
	public final DeferredHolder<Item, Item> THUNDER_HYPHAE_STRIPPED_ITEM = RegUtil.register(Registries.ITEM, THUNDER_HYPHAE_STRIPPED.getId().getPath(),
		(id) -> new BlockItem(THUNDER_HYPHAE_STRIPPED.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final BlockSetType THUNDER_SET = new BlockSetType(
		Identifier.fromNamespaceAndPath(Voidscape.MODID, "thunder").toString(),
		true,
		true,
		true,
		BlockSetType.PressurePlateSensitivity.EVERYTHING,
		SoundType.NETHER_WOOD,
		SoundEvents.NETHER_WOOD_DOOR_CLOSE,
		SoundEvents.NETHER_WOOD_DOOR_OPEN,
		SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
		SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
		SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
		SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
		SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
		SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON
	);
	public final WoodType THUNDER_WOOD_TYPE = WoodType.register(new WoodType(THUNDER_SET.name(), THUNDER_SET));

	public final DeferredHolder<Block, Block> THUNDER_PLANKS = RegUtil.register(Registries.BLOCK, "thunder_planks",
		(id) -> new Block(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
		)
	);
	public final DeferredHolder<Item, Item> THUNDER_PLANKS_ITEM = RegUtil.register(Registries.ITEM, THUNDER_PLANKS.getId().getPath(),
		(id) -> new BlockItem(THUNDER_PLANKS.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, StairBlock> THUNDER_STAIRS = RegUtil.register(Registries.BLOCK, "thunder_stairs",
		(id) -> new StairBlock(THUNDER_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
		)
	);
	public final DeferredHolder<Item, Item> THUNDER_STAIRS_ITEM = RegUtil.register(Registries.ITEM, THUNDER_STAIRS.getId().getPath(),
		(id) -> new BlockItem(THUNDER_STAIRS.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Block, Block> THUNDER_SLAB = RegUtil.register(Registries.BLOCK, "thunder_slab",
		(id) -> new SlabBlock(BlockBehaviour.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
		));
	public final DeferredHolder<Item, Item> THUNDER_SLAB_ITEM = RegUtil.register(Registries.ITEM, THUNDER_SLAB.getId().getPath(),
		(id) -> new BlockItem(THUNDER_SLAB.get(), itemProperties.BLOCK_LAVA_IMMUNE.apply(id))
	);

	@PostConstruct
	public void init(IEventBus bus) {
		bus.addListener(FMLCommonSetupEvent.class, event -> {
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(THUNDER_ROOTS.getId(), THUNDER_ROOTS_POT);
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(THUNDER_FUNGUS.getId(), THUNDER_FUNGUS_POT);
		});
	}

}
