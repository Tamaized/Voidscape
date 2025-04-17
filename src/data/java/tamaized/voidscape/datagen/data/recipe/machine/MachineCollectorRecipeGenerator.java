package tamaized.voidscape.datagen.data.recipe.machine;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class MachineCollectorRecipeGenerator implements IRecipeGenerator {

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
				blocks.machineBlocks().MACHINE_COLLECTOR.get()
			)
			.pattern("ABA")
			.pattern("BCB")
			.pattern("ABA")
			.define('A', items.materialItems().ASTRAL_CRYSTAL.get())
			.define('B', items.materialItems().CHARRED_BONE.get())
			.define('C', blocks.machineBlocks().MACHINE_CORE.get())
			.unlockedBy("has_template", recipeProviderUtil.has(blocks.machineBlocks().MACHINE_CORE.get()))
			.save(recipeOutput, "machine_collector");
	}

}
