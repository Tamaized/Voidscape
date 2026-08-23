package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.fluid.ModFluidBuckets;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class VeryDrippyDripstoneRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModFluidBuckets buckets;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(
				itemProvider,
				RecipeCategory.BUILDING_BLOCKS,
				blocks.functionalBlocks().VERY_DRIPPY_DRIPSTONE.get()
			)
			.requires(Items.POINTED_DRIPSTONE)
			.requires(buckets.VOIDIC.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, buckets.VOIDIC.get()))
			.save(recipeOutput, "very_drippy_dripstone");
	}

}
