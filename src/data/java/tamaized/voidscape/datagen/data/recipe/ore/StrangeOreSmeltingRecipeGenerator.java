package tamaized.voidscape.datagen.data.recipe.ore;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class StrangeOreSmeltingRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		SimpleCookingRecipeBuilder.smelting(
				Ingredient.of(blocks.oreBlocks().STRANGE_ORE.get()),
				RecipeCategory.MISC,
				items.materialItems().STRANGE_PEARL.get(),
				1.5F,
				200
			)
			.unlockedBy("has_template", recipeProviderUtil.has(blocks.oreBlocks().STRANGE_ORE.get()))
			.save(recipeOutput, "smelt_strange_ore");
	}

}
