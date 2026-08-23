package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class VoidicTemplateRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				itemProvider,
				RecipeCategory.MISC,
				items.augmentItems().VOIDIC_TEMPLATE.get()
			)
			.pattern("CCC")
			.pattern("CDC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.define('D', Items.DIAMOND)
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_template");

		ShapedRecipeBuilder.shaped(
				itemProvider,
				RecipeCategory.MISC,
				items.augmentItems().VOIDIC_TEMPLATE.get(),
				2
			)
			.pattern("CCC")
			.pattern("CTC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.define('T', items.augmentItems().VOIDIC_TEMPLATE.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_template_copy");
	}

}
