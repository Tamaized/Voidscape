package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemTags;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class ThunderPlanksRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModItemTags itemTags;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(
				itemProvider,
				RecipeCategory.BUILDING_BLOCKS,
				blocks.thunderForestBiomeBlocks().THUNDER_PLANKS.get(),
				4
			)
			.requires(itemTags.THUNDER_STEMS)
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, itemTags.THUNDER_STEMS))
			.save(recipeOutput, "thunder_planks");
	}

}
