package tamaized.voidscape.registry.tool;

import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.ToolTier;
import tamaized.voidscape.registry.item.MaterialItems;

@Component
public class ModToolTiers {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolTags;

	@Autowired
	private MaterialItems materialItems;

	public final ToolTier VOIDIC_CRYSTAL = new ToolTier(
		() -> incorrectBlocksForToolTags.VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		() -> Ingredient.of(materialItems.VOIDIC_CRYSTAL.get())
	);

	public final ToolTier CHARRED = new ToolTier(
		() -> incorrectBlocksForToolTags.VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		() -> Ingredient.of(materialItems.CHARRED_BONE.get()
		));

	public final ToolTier CORRUPT = new ToolTier(
		() -> incorrectBlocksForToolTags.CORRUPT,
		3041, 10.0F, 6F, 19,
		() -> Ingredient.of(materialItems.TENDRIL.get()
		));

	public final ToolTier TITANITE = new ToolTier(
		() -> incorrectBlocksForToolTags.TITANITE,
		3544, 10.5F, 7F, 21,
		() -> Ingredient.of(materialItems.TITANITE_SHARD.get()
		));

	public final ToolTier ICHOR = new ToolTier(
		() -> incorrectBlocksForToolTags.ICHOR,
		4047, 11.0F, 8F, 23,
		() -> Ingredient.of(materialItems.ICHOR_CRYSTAL.get()
		));

	public final ToolTier ASTRAL = new ToolTier(
		() -> incorrectBlocksForToolTags.ASTRAL,
		4550, 11.5F, 9F, 25,
		() -> Ingredient.of(materialItems.ASTRAL_CRYSTAL.get()
		));

}
