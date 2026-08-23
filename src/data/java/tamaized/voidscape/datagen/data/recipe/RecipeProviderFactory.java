package tamaized.voidscape.datagen.data.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class RecipeProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(RecipeHolder.class)
	private List<RecipeHolder> recipeGenerators;

	public RecipeProvider.Runner make(GatherDataEvent event) {
		return new RecipeProvider.Runner(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event)
		) {
			@Override
			public String getName() {
				return "Voidscape recipes";
			}

			@Override
			protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
				return new RecipeProvider(registries, output) {
					@Override
					protected void buildRecipes() {
						recipeGenerators.forEach(generator -> generator.make(registries, items, output));
					}
				};
			}
		};
	}

}
