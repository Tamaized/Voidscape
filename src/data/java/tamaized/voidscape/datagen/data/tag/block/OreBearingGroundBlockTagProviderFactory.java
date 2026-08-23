package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;

@Component
public class OreBearingGroundBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Override
	public void make(ExposedKeyTagProvider<Block> accessor, HolderLookup.Provider provider) {
		tag(accessor, Blocks.BEDROCK);
		tag(accessor, Blocks.END_STONE);
	}

	private void tag(ExposedKeyTagProvider<Block> accessor, Block block) {
		ResourceKey<Block> key = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
		accessor.tag(TagKey.create(
			Registries.BLOCK,
			Identifier.fromNamespaceAndPath(
				"c",
				"ore_bearing_ground/" + key.identifier().getPath()
			)
		)).add(key);
	}
}
