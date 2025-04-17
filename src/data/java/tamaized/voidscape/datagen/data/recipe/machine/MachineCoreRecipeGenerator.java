package tamaized.voidscape.datagen.data.recipe.machine;

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
public class MachineCoreRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				RecipeCategory.BUILDING_BLOCKS,
				blocks.machineBlocks().MACHINE_CORE.get()
			)
			.pattern("TTT")
			.pattern("TRT")
			.pattern("TTT")
			.define('T', items.materialItems().TENDRIL.get())
			.define('R', Items.REDSTONE)
			.unlockedBy("has_template", recipeProviderUtil.has(items.materialItems().TENDRIL.get()))
			.save(recipeOutput, "machine_core");
	}

}
