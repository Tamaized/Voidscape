package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class VeryDrippyDripstoneRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModFluidBuckets buckets;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(
				RecipeCategory.BUILDING_BLOCKS,
				blocks.functionalBlocks().VERY_DRIPPY_DRIPSTONE.get()
			)
			.requires(Items.POINTED_DRIPSTONE)
			.requires(buckets.VOIDIC.get())
			.unlockedBy("has_template", recipeProviderUtil.has(buckets.VOIDIC.get()))
			.save(recipeOutput, "very_drippy_dripstone");
	}

}
