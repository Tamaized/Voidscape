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
public class CharredWarhammerRecipeGenerator implements IRecipeGenerator {

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
				items.toolSetComponentDirectory().charredToolSet().CHARRED_WARHAMMER.get()
			)
			.pattern("H")
			.pattern("N")
			.pattern("C")
			.define('H', items.partItems().CHARRED_WARHAMMER_HEAD.get())
			.define('N', Items.NETHERITE_INGOT)
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.unlockedBy("has_template", recipeProviderUtil.has(items.partItems().CHARRED_WARHAMMER_HEAD.get()))
			.save(recipeOutput, "charred_warhammer");
	}

}
