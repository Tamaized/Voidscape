package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class ThunderHyphaeRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				RecipeCategory.BUILDING_BLOCKS,
				blocks.thunderForestBiomeBlocks().THUNDER_HYPHAE.get(),
				3
			)
			.pattern("CC")
			.pattern("CC")
			.define('C', blocks.thunderForestBiomeBlocks().THUNDER_STEM.get())
			.unlockedBy("has_template", recipeProviderUtil.has(blocks.thunderForestBiomeBlocks().THUNDER_STEM.get()))
			.save(recipeOutput, "thunder_hyphae");
	}

}
