package tamaized.voidscape.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
		(id) -> new LightningAttractorBlock
			.Builder<>(() -> EntityType.LIGHTNING_BOLT)
			.build(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.lightLevel(_ -> 15)
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> THUNDERROCK_ITEM = RegUtil.register(Registries.ITEM, THUNDERROCK.getId().getPath(),
		(id) -> new BlockItem(
			THUNDERROCK.get(),
			itemProperties.DEFAULT.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> ANTIROCK = RegUtil.register(Registries.BLOCK, "antirock",
		(id) -> new LightningAttractorBlock
			.Builder<>(entities.ANTI_BOLT)
			.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
			.build(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> ANTIROCK_ITEM = RegUtil.register(Registries.ITEM, ANTIROCK.getId().getPath(),
		(id) -> new BlockItem(
			ANTIROCK.get(),
			itemProperties.DEFAULT.apply(id)
		)
	);

	public final DeferredHolder<Block, Block> ASTRALROCK = RegUtil.register(Registries.BLOCK, "astralrock",
		(id) -> new LightningAttractorBlock
			.Builder<>(entities.ANTI_BOLT)
			.positionModifier(pos -> pos.subtract(0, 0.01F, 0))
			.to(() -> oreBlocks.CRACKED_ASTRALROCK.get().defaultBlockState())
			.build(Block.Properties.of()
			.setId(ResourceKey.create(Registries.BLOCK, id))
				.randomTicks()
				.sound(SoundType.STONE)
				.mapColor(MapColor.COLOR_BLACK)
				.strength(-1.0F, 3600000.0F)
				.noLootTable()
				.isValidSpawn((_, _, _, _) -> false)
			)
	);
	public final Supplier<Item> ASTRALROCK_ITEM = RegUtil.register(Registries.ITEM, ASTRALROCK.getId().getPath(),
		(id) -> new BlockItem(
			ASTRALROCK.get(),
			itemProperties.DEFAULT.apply(id)
		)
	);

}
