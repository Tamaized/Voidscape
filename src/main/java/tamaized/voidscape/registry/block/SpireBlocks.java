package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
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
import tamaized.voidscape.block.LightningAttractorBlock;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class SpireBlocks {

	private final DeferredRegister<Block> REGISTRY = RegUtil.create(Registries.BLOCK);
	private final DeferredRegister<Item> REGISTRY_ITEM = RegUtil.create(Registries.ITEM);

	@Autowired
	private OreBlocks oreBlocks;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> THUNDERROCK = REGISTRY.register("thunderrock", () -> new LightningAttractorBlock.Builder<>(
		() -> EntityType.LIGHTNING_BOLT
	).build(Block.Properties.of()
		.sound(SoundType.STONE)
		.mapColor(MapColor.COLOR_BLACK)
		.strength(-1.0F, 3600000.0F)
		.noLootTable()
		.lightLevel(state -> 15)
		.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
	));
	public final Supplier<Item> THUNDERROCK_ITEM = REGISTRY_ITEM.register(THUNDERROCK.getId().getPath(), () -> new BlockItem(
		THUNDERROCK.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> ANTIROCK = REGISTRY.register("antirock", () -> new LightningAttractorBlock.Builder<>(entities.ANTI_BOLT)
		.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
		.build(
			Block.Properties.of()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
		));
	public final Supplier<Item> ANTIROCK_ITEM = REGISTRY_ITEM.register(ANTIROCK.getId().getPath(), () -> new BlockItem(
		ANTIROCK.get(),
		itemProperties.DEFAULT.get()
	));

	public final DeferredHolder<Block, Block> ASTRALROCK = REGISTRY.register("astralrock", () -> new LightningAttractorBlock.Builder<>(entities.ANTI_BOLT)
		.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
		.to(() -> oreBlocks.CRACKED_ASTRALROCK.get().defaultBlockState())
		.build(Block.Properties.of()
			.sound(SoundType.STONE)
			.mapColor(MapColor.COLOR_BLACK)
			.strength(-1.0F, 3600000.0F)
			.noLootTable()
			.isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false)
		));
	public final Supplier<Item> ASTRALROCK_ITEM = REGISTRY_ITEM.register(ASTRALROCK.getId().getPath(), () -> new BlockItem(
		ASTRALROCK.get(),
		itemProperties.DEFAULT.get()
	));

}
