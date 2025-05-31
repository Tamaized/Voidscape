package tamaized.voidscape.datagen.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class RecipeProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IRecipeGenerator.class)
	private List<IRecipeGenerator> recipeGenerators;

	public RecipeProvider make(GatherDataEvent event) {
		return new RecipeProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event)
		) {
			@Override
			protected void buildRecipes(RecipeOutput recipeOutput) {
				recipeGenerators.forEach(generator -> generator.generate(recipeOutput));
			}
		};
	}

}
