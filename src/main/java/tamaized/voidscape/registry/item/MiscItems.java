package tamaized.voidscape.registry.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.item.BlockTransformerItem;
import tamaized.voidscape.registry.ModAdvancementTriggers;
import tamaized.voidscape.registry.ModItemProperties;
import tamaized.voidscape.registry.block.OreBlocks;
import tamaized.voidscape.registry.block.ThunderForestBiomeBlocks;

@Component
public class MiscItems {

	@Autowired
	private ModAdvancementTriggers advancementTriggers;

	@Autowired
	private OreBlocks oreBlocks;

	@Autowired
	private ThunderForestBiomeBlocks thunderForestBiomeBlocks;

	@Autowired
	private ModItemProperties itemProperties;

	public final DeferredHolder<Item, Item> ETHEREAL_ESSENCE = RegUtil.register(Registries.ITEM, "ethereal_essence", (id) -> new BlockTransformerItem.Builder(
		state -> state.is(Blocks.BEDROCK),
		() -> oreBlocks.VOIDIC_CRYSTAL_ORE.get().defaultBlockState())
		.advancement(advancementTriggers.ETHEREAL_ESSENCE_TRIGGER)
		.build(itemProperties.LAVA_IMMUNE.apply(id))
	);

	public final DeferredHolder<Item, Item> ETHEREAL_SPIDER_EGGS = RegUtil.register(Registries.ITEM, "ethereal_spider_eggs", (id) -> new BlockTransformerItem.Builder(
		state -> state.is(Blocks.BEDROCK) || state.is(thunderForestBiomeBlocks.THUNDER_NYLIUM),
		Blocks.AIR::defaultBlockState)
		.advancement(advancementTriggers.ETHEREAL_SPIDER_EGGS_TRIGGER)
		.particleCount(200)
		.particle(() -> ParticleTypes.ASH)
		.sound(() -> SoundEvents.CONDUIT_ATTACK_TARGET)
		.build(itemProperties.LAVA_IMMUNE.apply(id))
	);

}
