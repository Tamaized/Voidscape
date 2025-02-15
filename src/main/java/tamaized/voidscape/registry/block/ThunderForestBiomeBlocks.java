package tamaized.voidscape.registry.block;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
import net.neoforged.neoforge.registries.DeferredRegister;
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

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	public final DeferredHolder<Block, Block> THUNDER_NYLIUM = REGISTRY.register("thunder_nylium", () -> new ThunderNyliumBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.NYLIUM)
		.mapColor(MapColor.COLOR_PURPLE)
		.strength(-1.0F, 3600000.0F)
		.noLootTable()
		.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public final Supplier<Item> THUNDER_NYLIUM_ITEM = REGISTRY_ITEM
		.register(THUNDER_NYLIUM.getId().getPath(), () -> new BlockItem(THUNDER_NYLIUM.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_ROOTS = REGISTRY.register("thunder_roots", () -> new RootsBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.ROOTS)
		.mapColor(MapColor.COLOR_PURPLE)
		.noCollission()
		.instabreak()
		.replaceable()
		.pushReaction(PushReaction.DESTROY)
		.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public final Supplier<Item> THUNDER_ROOTS_ITEM = REGISTRY_ITEM
		.register(THUNDER_ROOTS.getId().getPath(), () -> new BlockItem(THUNDER_ROOTS.get(), itemProperties.LAVA_IMMUNE.get()));
	public final DeferredHolder<Block, Block> THUNDER_ROOTS_POT = REGISTRY.register("thunder_roots_pot", () -> new FlowerPotBlock(
		() -> (FlowerPotBlock) Blocks.FLOWER_POT,
		THUNDER_ROOTS,
		BlockBehaviour.Properties.of()
			.instabreak()
			.noOcclusion()
			.pushReaction(PushReaction.DESTROY)
	));

	public final DeferredHolder<Block, Block> THUNDER_FUNGUS = REGISTRY.register("thunder_fungus", () -> new FungusBlock(
		configuredFeatures.THUNDER_FUNGUS,
		THUNDER_NYLIUM.get(),
		BlockBehaviour.Properties.of()
			.sound(SoundType.FUNGUS)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.replaceable()
			.pushReaction(PushReaction.DESTROY)
	));
	public final Supplier<Item> THUNDER_FUNGUS_ITEM = REGISTRY_ITEM
		.register(THUNDER_FUNGUS.getId().getPath(), () -> new BlockItem(THUNDER_FUNGUS.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_FUNGUS_POT = REGISTRY.register("thunder_fungus_pot", () -> new FlowerPotBlock(
		() -> (FlowerPotBlock) Blocks.FLOWER_POT,
		THUNDER_FUNGUS,
		BlockBehaviour.Properties.of()
			.instabreak()
			.noOcclusion()
			.pushReaction(PushReaction.DESTROY)
	));

	public final DeferredHolder<Block, Block> THUNDER_WART = REGISTRY.register("thunder_wart", () -> new Block(BlockBehaviour.Properties.of()
		.sound(SoundType.WART_BLOCK)
		.mapColor(MapColor.COLOR_PURPLE)
		.strength(1.0F)
	));
	public final Supplier<Item> THUNDER_WART_ITEM = REGISTRY_ITEM
		.register(THUNDER_WART.getId().getPath(), () -> new BlockItem(THUNDER_WART.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, GrowingPlantHeadBlock> THUNDER_VINES = REGISTRY.register("thunder_vines", () -> new WeepingVinesBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.WEEPING_VINES)
		.mapColor(MapColor.COLOR_PURPLE)
		.randomTicks()
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
	) {
		@Override
		protected Block getBodyBlock() {
			return THUNDER_VINES_PLANT.get();
		}
	});
	public final DeferredHolder<Block, Block> THUNDER_VINES_PLANT = REGISTRY.register("thunder_vines_plant", () -> new WeepingVinesPlantBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.WEEPING_VINES)
		.mapColor(MapColor.COLOR_PURPLE)
		.randomTicks()
		.noCollission()
		.instabreak()
		.pushReaction(PushReaction.DESTROY)
	) {
		@Override
		protected GrowingPlantHeadBlock getHeadBlock() {
			return THUNDER_VINES.get();
		}
	});
	public final Supplier<Item> THUNDER_VINES_ITEM = REGISTRY_ITEM
		.register(THUNDER_VINES.getId().getPath(), () -> new BlockItem(THUNDER_VINES.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_STEM = REGISTRY.register("thunder_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
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
	});
	public final Supplier<Item> THUNDER_STEM_ITEM = REGISTRY_ITEM
		.register(THUNDER_STEM.getId().getPath(), () -> new BlockItem(THUNDER_STEM.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_STEM_STRIPPED = REGISTRY.register("thunder_stem_stripped", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.STEM)
		.mapColor(MapColor.COLOR_CYAN)
		.instrument(NoteBlockInstrument.BASS)
		.strength(2.0F)
	));
	public final Supplier<Item> THUNDER_STEM_STRIPPED_ITEM = REGISTRY_ITEM
		.register(THUNDER_STEM_STRIPPED.getId().getPath(), () -> new BlockItem(THUNDER_STEM_STRIPPED.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_HYPHAE = REGISTRY.register("thunder_hyphae", () -> new Block(BlockBehaviour.Properties.of()
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
	});
	public final Supplier<Item> THUNDER_HYPHAE_ITEM = REGISTRY_ITEM
		.register(THUNDER_HYPHAE.getId().getPath(), () -> new BlockItem(THUNDER_HYPHAE.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_HYPHAE_STRIPPED = REGISTRY.register("thunder_hyphae_stripped", () -> new Block(BlockBehaviour.Properties.of()
		.sound(SoundType.STEM)
		.mapColor(MapColor.COLOR_CYAN)
		.instrument(NoteBlockInstrument.BASS)
		.strength(2.0F)
	));
	public final Supplier<Item> THUNDER_HYPHAE_STRIPPED_ITEM = REGISTRY_ITEM
		.register(THUNDER_HYPHAE_STRIPPED.getId().getPath(), () -> new BlockItem(THUNDER_HYPHAE_STRIPPED.get(), itemProperties.LAVA_IMMUNE.get()));

	public final BlockSetType THUNDER_SET = new BlockSetType(
		ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "thunder").toString(),
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

	public final DeferredHolder<Block, Block> THUNDER_PLANKS = REGISTRY.register("thunder_planks", () -> new Block(BlockBehaviour.Properties.of()
		.sound(SoundType.NETHER_WOOD)
		.mapColor(MapColor.COLOR_CYAN)
		.instrument(NoteBlockInstrument.BASS)
		.strength(2.0F, 3.0F)
	));
	public final Supplier<Item> THUNDER_PLANKS_ITEM = REGISTRY_ITEM
		.register(THUNDER_PLANKS.getId().getPath(), () -> new BlockItem(THUNDER_PLANKS.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, StairBlock> THUNDER_STAIRS = REGISTRY.register("thunder_stairs", () -> new StairBlock(THUNDER_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of()
		.sound(SoundType.NETHER_WOOD)
		.mapColor(MapColor.COLOR_CYAN)
		.instrument(NoteBlockInstrument.BASS)
		.strength(2.0F, 3.0F)
	));
	public final Supplier<Item> THUNDER_STAIRS_ITEM = REGISTRY_ITEM
		.register(THUNDER_STAIRS.getId().getPath(), () -> new BlockItem(THUNDER_STAIRS.get(), itemProperties.LAVA_IMMUNE.get()));

	public final DeferredHolder<Block, Block> THUNDER_SLAB = REGISTRY.register("thunder_slab", () -> new SlabBlock(BlockBehaviour.Properties.of()
		.sound(SoundType.NETHER_WOOD)
		.mapColor(MapColor.COLOR_CYAN)
		.instrument(NoteBlockInstrument.BASS)
		.strength(2.0F, 3.0F)
	));
	public final Supplier<Item> THUNDER_SLAB_ITEM = REGISTRY_ITEM
		.register(THUNDER_SLAB.getId().getPath(), () -> new BlockItem(THUNDER_SLAB.get(), itemProperties.LAVA_IMMUNE.get()));

	@PostConstruct
	public void init(IEventBus bus) {
		bus.addListener(FMLCommonSetupEvent.class, event -> {
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(THUNDER_ROOTS.getId(), THUNDER_ROOTS_POT);
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(THUNDER_FUNGUS.getId(), THUNDER_FUNGUS_POT);
		});
	}

}
