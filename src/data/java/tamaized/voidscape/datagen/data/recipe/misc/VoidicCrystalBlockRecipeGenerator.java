package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class VoidicCrystalBlockRecipeGenerator implements IRecipeGenerator {

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
				blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get()
			)
			.pattern("CCC")
			.pattern("CCC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_crystal_block");

		ShapelessRecipeBuilder.shapeless(
				RecipeCategory.MISC,
				items.materialItems().VOIDIC_CRYSTAL.get(),
				9
			)
			.requires(blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get())
			.unlockedBy("has_template", recipeProviderUtil.has(blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get()))
			.save(recipeOutput, "voidic_crystal_block_reverse");
	}

}
