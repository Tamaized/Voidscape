package tamaized.voidscape.datagen.data.recipe.machine;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class MachineHatcheryRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		SmithingTransformRecipeBuilder.smithing(
			Ingredient.of(Items.DRAGON_HEAD),
			Ingredient.of(blocks.machineBlocks().MACHINE_COOP.get()),
			Ingredient.of(items.materialItems().ASTRAL_CRYSTAL.get()),
			RecipeCategory.BUILDING_BLOCKS,
			blocks.machineBlocks().MACHINE_HATCHERY_ITEM.get()
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, Items.DRAGON_HEAD))
			.save(recipeOutput, "machine_hatchery");
	}

}
