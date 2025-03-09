package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class DebugOreBlockTagProviderFactory implements IBlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Override
	public void make(BlockTagProviderFactory.BlockTagsProviderAccessor accessor, HolderLookup.Provider provider) {
		accessor.tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "debug_ore"))).add(
			Blocks.IRON_ORE,
			Blocks.GOLD_ORE,
			Blocks.COPPER_ORE,
			Blocks.DIAMOND_ORE,
			Blocks.EMERALD_ORE,
			Blocks.REDSTONE_ORE,
			Blocks.LAPIS_ORE,
			Blocks.COAL_ORE,
			Blocks.NETHER_GOLD_ORE,
			Blocks.STONE,
			Blocks.NETHERRACK
		);
	}

}
