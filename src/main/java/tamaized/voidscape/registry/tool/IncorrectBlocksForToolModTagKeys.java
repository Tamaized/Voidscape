package tamaized.voidscape.registry.tool;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class IncorrectBlocksForToolModTagKeys {

	public final TagKey<Block> INCORRECT_FOR_VOIDIC_CRYSTAL = TagKey.create(Registries.BLOCK, name("voidic_crystal"));
	public final TagKey<Block> INCORRECT_FOR_CORRUPT = TagKey.create(Registries.BLOCK, name("corrupt"));
	public final TagKey<Block> INCORRECT_FOR_TITANITE = TagKey.create(Registries.BLOCK, name("titanite"));
	public final TagKey<Block> INCORRECT_FOR_ICHOR = TagKey.create(Registries.BLOCK, name("ichor"));
	public final TagKey<Block> INCORRECT_FOR_ASTRAL = TagKey.create(Registries.BLOCK, name("astral"));

	public final TagKey<Block> NEEDS_VOIDIC_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "needs_voidic_tool"));
	public final TagKey<Block> NEEDS_CORRUPT_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "needs_corrupt_tool"));
	public final TagKey<Block> NEEDS_TITANITE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "needs_titanite_tool"));
	public final TagKey<Block> NEEDS_ICHOR_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "needs_ichor_tool"));
	public final TagKey<Block> NEEDS_ASTRAL_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Voidscape.MODID, "needs_astral_tool"));

	private Identifier name(String type) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, "incorrect_for_" + type + "_tool");
	}

}
