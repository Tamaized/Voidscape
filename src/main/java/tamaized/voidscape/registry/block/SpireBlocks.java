package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.block.LightningAttractorBlock;
import tamaized.voidscape.registry.ModEntities;
import tamaized.voidscape.registry.ModItemProperties;

import java.util.function.Supplier;

@Component
public class SpireBlocks {

	@Autowired
	private OreBlocks oreBlocks;

	@Autowired
	private ModEntities entities;

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Block, Block> THUNDERROCK = RegUtil.register(Registries.BLOCK, "thunderrock",
		() -> new LightningAttractorBlock
			.Builder<>(() -> EntityType.LIGHTNING_BOLT)
			.build(Block.Properties.of()
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.lightLevel(state -> 15)
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> THUNDERROCK_ITEM = RegUtil.register(Registries.ITEM, THUNDERROCK.getId().getPath(),
		() -> new BlockItem(
			THUNDERROCK.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> ANTIROCK = RegUtil.register(Registries.BLOCK, "antirock",
		() -> new LightningAttractorBlock
			.Builder<>(entities.ANTI_BOLT)
			.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
			.build(Block.Properties.of()
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> ANTIROCK_ITEM = RegUtil.register(Registries.ITEM, ANTIROCK.getId().getPath(),
		() -> new BlockItem(
			ANTIROCK.get(),
			itemProperties.DEFAULT.get()
		)
	);

	public final DeferredHolder<Block, Block> ASTRALROCK = RegUtil.register(Registries.BLOCK, "astralrock",
		() -> new LightningAttractorBlock
			.Builder<>(entities.ANTI_BOLT)
			.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
			.to(() -> oreBlocks.CRACKED_ASTRALROCK.get().defaultBlockState())
			.build(Block.Properties.of()
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> ASTRALROCK_ITEM = RegUtil.register(Registries.ITEM, ASTRALROCK.getId().getPath(),
		() -> new BlockItem(
			ASTRALROCK.get(),
			itemProperties.DEFAULT.get()
		)
	);

}
