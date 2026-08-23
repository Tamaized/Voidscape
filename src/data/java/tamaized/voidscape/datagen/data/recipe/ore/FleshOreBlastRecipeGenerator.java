package tamaized.voidscape.datagen.data.recipe.ore;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class FleshOreBlastRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		SimpleCookingRecipeBuilder.blasting(
				Ingredient.of(blocks.oreBlocks().FLESH_ORE.get()),
				RecipeCategory.MISC,
				CookingBookCategory.MISC,
				items.materialItems().FLESH_CHUNK.get(),
				1.5F,
				100
			)
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, blocks.oreBlocks().FLESH_ORE.get()))
			.save(recipeOutput, "blast_flesh_ore");
	}

}
