package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class VoidicTemplateRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				RecipeCategory.MISC,
				items.augmentItems().VOIDIC_TEMPLATE.get()
			)
			.pattern("CCC")
			.pattern("CDC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.define('D', Items.DIAMOND)
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_template");

		ShapedRecipeBuilder.shaped(
				RecipeCategory.MISC,
				items.augmentItems().VOIDIC_TEMPLATE.get(),
				2
			)
			.pattern("CCC")
			.pattern("CTC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.define('T', items.augmentItems().VOIDIC_TEMPLATE.get())
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_template_copy");
	}

}
