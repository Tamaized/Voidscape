package tamaized.voidscape.registry.tool;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class IncorrectBlocksForToolModTagKeys {

	public final TagKey<Block> VOIDIC_CRYSTAL = TagKey.create(Registries.BLOCK, name("voidic_crystal"));
	public final TagKey<Block> CORRUPT = TagKey.create(Registries.BLOCK, name("corrupt"));
	public final TagKey<Block> TITANITE = TagKey.create(Registries.BLOCK, name("titanite"));
	public final TagKey<Block> ICHOR = TagKey.create(Registries.BLOCK, name("ichor"));
	public final TagKey<Block> ASTRAL = TagKey.create(Registries.BLOCK, name("astral"));

	private Identifier name(String type) {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, "incorrect_for_" + type + "_tool");
	}

}
