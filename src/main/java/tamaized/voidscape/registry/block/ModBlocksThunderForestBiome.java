package tamaized.voidscape.registry.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.AxeItem;
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
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import tamaized.regutil.RegistryClass;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.ThunderNyliumBlock;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModFeatures;
import tamaized.voidscape.registry.ModItems;

import java.util.function.Consumer;

public class ModBlocksThunderForestBiome implements RegistryClass {


	public static final RegistryObject<Block> THUNDER_NYLIUM = ModBlocks.REGISTRY.register("thunder_nylium", () -> new ThunderNyliumBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.NYLIUM)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((t1, t2, t3, t4) -> false)
	));
	public static final RegistryObject<Item> THUNDER_NYLIUM_ITEM = ModItems.REGISTRY
			.register(THUNDER_NYLIUM.getId().getPath(), () -> new BlockItem(THUNDER_NYLIUM.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_ROOTS = ModBlocks.REGISTRY.register("thunder_roots", () -> new RootsBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.ROOTS)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.replaceable()
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XYZ)
	));
	public static final RegistryObject<Item> THUNDER_ROOTS_ITEM = ModItems.REGISTRY
			.register(THUNDER_ROOTS.getId().getPath(), () -> new BlockItem(THUNDER_ROOTS.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));
	public static final RegistryObject<Block> THUNDER_ROOTS_POT = ModBlocks.REGISTRY.register("thunder_roots_pot", () -> new FlowerPotBlock(
			() -> (FlowerPotBlock) Blocks.FLOWER_POT,
			THUNDER_ROOTS,
			BlockBehaviour.Properties.of()
					.instabreak()
					.noOcclusion()
					.pushReaction(PushReaction.DESTROY)
	));

	public static final RegistryObject<Block> THUNDER_FUNGUS = ModBlocks.REGISTRY.register("thunder_fungus", () -> new FungusBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.FUNGUS)
			.mapColor(MapColor.COLOR_PURPLE)
			.noCollission()
			.instabreak()
			.replaceable()
			.pushReaction(PushReaction.DESTROY),
			ModFeatures.THUNDER_FUNGUS,
			THUNDER_NYLIUM.get()
	));
	public static final RegistryObject<Item> THUNDER_FUNGUS_ITEM = ModItems.REGISTRY
			.register(THUNDER_FUNGUS.getId().getPath(), () -> new BlockItem(THUNDER_FUNGUS.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_FUNGUS_POT = ModBlocks.REGISTRY.register("thunder_fungus_pot", () -> new FlowerPotBlock(
			() -> (FlowerPotBlock) Blocks.FLOWER_POT,
			THUNDER_FUNGUS,
			BlockBehaviour.Properties.of()
					.instabreak()
					.noOcclusion()
					.pushReaction(PushReaction.DESTROY)
	));

	public static final RegistryObject<Block> THUNDER_WART = ModBlocks.REGISTRY.register("thunder_wart", () -> new Block(BlockBehaviour.Properties.of()
			.sound(SoundType.WART_BLOCK)
			.mapColor(MapColor.COLOR_PURPLE)
			.strength(1.0F)
	));
	public static final RegistryObject<Item> THUNDER_WART_ITEM = ModItems.REGISTRY
			.register(THUNDER_WART.getId().getPath(), () -> new BlockItem(THUNDER_WART.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<GrowingPlantHeadBlock> THUNDER_VINES = ModBlocks.REGISTRY.register("thunder_vines", () -> new WeepingVinesBlock(BlockBehaviour.Properties.of()
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
	public static final RegistryObject<Block> THUNDER_VINES_PLANT = ModBlocks.REGISTRY.register("thunder_vines_plant", () -> new WeepingVinesPlantBlock(BlockBehaviour.Properties.of()
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
	public static final RegistryObject<Item> THUNDER_VINES_ITEM = ModItems.REGISTRY
			.register(THUNDER_VINES.getId().getPath(), () -> new BlockItem(THUNDER_VINES.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_STEM = ModBlocks.REGISTRY.register("thunder_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_PURPLE)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
	) {
		@Override
		public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
			if (ToolActions.AXE_STRIP == toolAction) {
				return THUNDER_STEM_STRIPPED.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
			}
			return super.getToolModifiedState(state, context, toolAction, simulate);
		}
	});
	public static final RegistryObject<Item> THUNDER_STEM_ITEM = ModItems.REGISTRY
			.register(THUNDER_STEM.getId().getPath(), () -> new BlockItem(THUNDER_STEM.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_STEM_STRIPPED = ModBlocks.REGISTRY.register("thunder_stem_stripped", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
	));
	public static final RegistryObject<Item> THUNDER_STEM_STRIPPED_ITEM = ModItems.REGISTRY
			.register(THUNDER_STEM_STRIPPED.getId().getPath(), () -> new BlockItem(THUNDER_STEM_STRIPPED.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_HYPHAE = ModBlocks.REGISTRY.register("thunder_hyphae", () -> new Block(BlockBehaviour.Properties.of()
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_PURPLE)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
	) {
		@Override
		public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
			if (ToolActions.AXE_STRIP == toolAction) {
				return THUNDER_HYPHAE_STRIPPED.get().defaultBlockState();
			}
			return super.getToolModifiedState(state, context, toolAction, simulate);
		}
	});
	public static final RegistryObject<Item> THUNDER_HYPHAE_ITEM = ModItems.REGISTRY
			.register(THUNDER_HYPHAE.getId().getPath(), () -> new BlockItem(THUNDER_HYPHAE.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_HYPHAE_STRIPPED = ModBlocks.REGISTRY.register("thunder_hyphae_stripped", () -> new Block(BlockBehaviour.Properties.of()
			.sound(SoundType.STEM)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
	));
	public static final RegistryObject<Item> THUNDER_HYPHAE_STRIPPED_ITEM = ModItems.REGISTRY
			.register(THUNDER_HYPHAE_STRIPPED.getId().getPath(), () -> new BlockItem(THUNDER_HYPHAE_STRIPPED.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final BlockSetType THUNDER_SET = new BlockSetType(
			new ResourceLocation(Voidscape.MODID, "thunder").toString(),
			true,
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
	public static final WoodType THUNDER_WOOD_TYPE = WoodType.register(new WoodType(THUNDER_SET.name(), THUNDER_SET));

	public static final RegistryObject<Block> THUNDER_PLANKS = ModBlocks.REGISTRY.register("thunder_planks", () -> new Block(BlockBehaviour.Properties.of()
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
	));
	public static final RegistryObject<Item> THUNDER_PLANKS_ITEM = ModItems.REGISTRY
			.register(THUNDER_PLANKS.getId().getPath(), () -> new BlockItem(THUNDER_PLANKS.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_STAIRS = ModBlocks.REGISTRY.register("thunder_stairs", () -> new StairBlock(() -> THUNDER_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of()
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
	));
	public static final RegistryObject<Item> THUNDER_STAIRS_ITEM = ModItems.REGISTRY
			.register(THUNDER_STAIRS.getId().getPath(), () -> new BlockItem(THUNDER_STAIRS.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));

	public static final RegistryObject<Block> THUNDER_SLAB = ModBlocks.REGISTRY.register("thunder_slab", () -> new SlabBlock(BlockBehaviour.Properties.of()
			.sound(SoundType.NETHER_WOOD)
			.mapColor(MapColor.COLOR_CYAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
	));
	public static final RegistryObject<Item> THUNDER_SLAB_ITEM = ModItems.REGISTRY
			.register(THUNDER_SLAB.getId().getPath(), () -> new BlockItem(THUNDER_SLAB.get(), ModItems.ItemProps.LAVA_IMMUNE.properties().get()));


	@Override
	public void init(IEventBus bus) {
		bus.addListener((Consumer<FMLCommonSetupEvent>) event -> {
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocksThunderForestBiome.THUNDER_ROOTS.getId(), ModBlocksThunderForestBiome.THUNDER_ROOTS_POT);
			((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocksThunderForestBiome.THUNDER_FUNGUS.getId(), ModBlocksThunderForestBiome.THUNDER_FUNGUS_POT);
		});
	}

}
