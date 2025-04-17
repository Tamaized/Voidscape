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
public class CharredWarhammerHeadRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				RecipeCategory.TOOLS,
				items.partItems().CHARRED_WARHAMMER_HEAD.get()
			)
			.pattern("BBB")
			.pattern("BCB")
			.define('B', items.materialItems().CHARRED_BONE.get())
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().CHARRED_BONE.get()))
			.save(recipeOutput, "charred_warhammer_head");
	}

}
