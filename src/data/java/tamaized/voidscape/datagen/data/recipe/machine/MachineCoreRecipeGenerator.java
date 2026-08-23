package tamaized.voidscape.datagen.data.recipe.machine;

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
public class MachineCoreRecipeGenerator extends RecipeHolder {

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
				RecipeCategory.BUILDING_BLOCKS,
				blocks.machineBlocks().MACHINE_CORE.get()
			)
			.pattern("TTT")
			.pattern("TRT")
			.pattern("TTT")
			.define('T', items.materialItems().TENDRIL.get())
			.define('R', Items.REDSTONE)
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, items.materialItems().TENDRIL.get()))
			.save(recipeOutput, "machine_core");
	}

}
