package tamaized.voidscape.registry.tool;

import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.common.util.Lazy;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.registry.ModItemTags;

@Component
public class ModToolMaterials {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolTags;

	@Autowired
	private ModItemTags itemTags;

	public final Lazy<ToolMaterial> VOIDIC_CRYSTAL = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		itemTags.REPAIR_MATERIAL_VOIDIC_CRYSTAL
	));

	public final Lazy<ToolMaterial> CHARRED = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		itemTags.REPAIR_MATERIAL_CHARRED
	));

	public final Lazy<ToolMaterial> CORRUPT = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_CORRUPT,
		3041, 10.0F, 6F, 19,
		itemTags.REPAIR_MATERIAL_CORRUPT
	));

	public final Lazy<ToolMaterial> TITANITE = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_TITANITE,
		3544, 10.5F, 7F, 21,
		itemTags.REPAIR_MATERIAL_TITANITE
	));

	public final Lazy<ToolMaterial> ICHOR = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_ICHOR,
		4047, 11.0F, 8F, 23,
		itemTags.REPAIR_MATERIAL_ICHOR
	));

	public final Lazy<ToolMaterial> ASTRAL = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.INCORRECT_FOR_ASTRAL,
		4550, 11.5F, 9F, 25,
		itemTags.REPAIR_MATERIAL_ASTRAL
	));

}
