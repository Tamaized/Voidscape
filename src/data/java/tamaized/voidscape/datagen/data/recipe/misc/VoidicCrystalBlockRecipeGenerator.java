package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class VoidicCrystalBlockRecipeGenerator extends RecipeHolder {

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
				blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get()
			)
			.pattern("CCC")
			.pattern("CCC")
			.pattern("CCC")
			.define('C', items.materialItems().VOIDIC_CRYSTAL.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, items.materialItems().VOIDIC_CRYSTAL.get()))
			.save(recipeOutput, "voidic_crystal_block");

		ShapelessRecipeBuilder.shapeless(
				itemProvider,
				RecipeCategory.MISC,
				items.materialItems().VOIDIC_CRYSTAL.get(),
				9
			)
			.requires(blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, blocks.materialBlocks().VOIDIC_CRYSTAL_BLOCK.get()))
			.save(recipeOutput, "voidic_crystal_block_reverse");
	}

}
