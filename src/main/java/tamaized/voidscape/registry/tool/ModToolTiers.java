package tamaized.voidscape.registry.tool;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.common.util.Lazy;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.item.MaterialItems;

@Component
public class ModToolTiers {

	@Autowired
	private IncorrectBlocksForToolModTagKeys incorrectBlocksForToolTags;

	@Autowired
	private MaterialItems materialItems;

	public final TagKey<Item> REPAIR_TAG_VOIDIC_CRYSTAL = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "voidic_crystal"));
	public final Lazy<ToolMaterial> VOIDIC_CRYSTAL = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		REPAIR_TAG_VOIDIC_CRYSTAL
	));

	public final TagKey<Item> REPAIR_TAG_CHARRED = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "charred"));
	public final Lazy<ToolMaterial> CHARRED = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.VOIDIC_CRYSTAL,
		2538, 9.5F, 5F, 17,
		REPAIR_TAG_CHARRED
	));

	public final TagKey<Item> REPAIR_TAG_CORRUPT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "corrupt"));
	public final Lazy<ToolMaterial> CORRUPT = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.CORRUPT,
		3041, 10.0F, 6F, 19,
		REPAIR_TAG_CORRUPT
	));

	public final TagKey<Item> REPAIR_TAG_TITANITE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "titanite"));
	public final Lazy<ToolMaterial> TITANITE = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.TITANITE,
		3544, 10.5F, 7F, 21,
		REPAIR_TAG_TITANITE
	));

	public final TagKey<Item> REPAIR_TAG_ICHOR = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "ichor"));
	public final Lazy<ToolMaterial> ICHOR = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.ICHOR,
		4047, 11.0F, 8F, 23,
		REPAIR_TAG_ICHOR
	));

	public final TagKey<Item> REPAIR_TAG_ASTRAL = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Voidscape.MODID, "astral"));
	public final Lazy<ToolMaterial> ASTRAL = Lazy.of(() -> new ToolMaterial(
		incorrectBlocksForToolTags.ASTRAL,
		4550, 11.5F, 9F, 25,
		REPAIR_TAG_ASTRAL
	));

}
