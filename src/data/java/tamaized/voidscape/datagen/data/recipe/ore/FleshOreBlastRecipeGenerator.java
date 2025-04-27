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
public class FleshOreBlastRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		SimpleCookingRecipeBuilder.blasting(
				Ingredient.of(blocks.oreBlocks().FLESH_ORE.get()),
				RecipeCategory.MISC,
				items.materialItems().FLESH_CHUNK.get(),
				1.5F,
				100
			)
			.unlockedBy("has_template", recipeProviderUtil.has(blocks.oreBlocks().FLESH_ORE.get()))
			.save(recipeOutput, "blast_flesh_ore");
	}

}
