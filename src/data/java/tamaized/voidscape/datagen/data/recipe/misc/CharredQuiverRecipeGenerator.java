package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class CharredQuiverRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				RecipeCategory.TOOLS,
				items.toolSetComponentDirectory().charredToolSet().CHARRED_QUIVER.get()
			)
			.pattern("LCL")
			.pattern("CBC")
			.pattern("LCT")
			.define('L', Items.LEATHER)
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.define('B', items.materialItems().CHARRED_BONE.get())
			.define('T', items.materialItems().TENDRIL.get())
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().CHARRED_BONE.get()))
			.save(recipeOutput, "charred_quiver");
	}

}
