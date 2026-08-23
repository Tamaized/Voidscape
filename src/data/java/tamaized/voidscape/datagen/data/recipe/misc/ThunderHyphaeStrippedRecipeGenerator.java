package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class ThunderHyphaeStrippedRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				itemProvider,
				RecipeCategory.BUILDING_BLOCKS,
				blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE_STRIPPED.get(),
				3
			)
			.pattern("CC")
			.pattern("CC")
			.define('C', blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, blocks.thunderForestBiomeBlocks().THUNDER_STEM_STRIPPED.get()))
			.save(recipeOutput, "thunder_hyphae_stripped");
	}

}
