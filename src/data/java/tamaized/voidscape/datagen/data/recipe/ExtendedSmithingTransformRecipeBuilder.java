package tamaized.voidscape.datagen.data.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtendedSmithingTransformRecipeBuilder extends SmithingTransformRecipeBuilder {

	private final Ingredient template;
	private final Ingredient base;
	private final Ingredient addition;
	private final RecipeCategory category;
	private final ItemStack result;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	public ExtendedSmithingTransformRecipeBuilder(
		Ingredient template,
		Ingredient base,
		Ingredient addition,
		RecipeCategory category,
		ItemStack result
	) {
		super(template, base, addition, category, result.getItem());
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.category = category;
		this.result = result;
	}

	public static ExtendedSmithingTransformRecipeBuilder extendedSmithing(
		Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, ItemStack result
	) {
		return new ExtendedSmithingTransformRecipeBuilder(template, base, addition, category, result);
	}

	@Override
	public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
		this.ensureValid(recipeId);
		Advancement.Builder advancement$builder = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		SmithingTransformRecipe smithingtransformrecipe = new SmithingTransformRecipe(this.template, this.base, this.addition, this.result);
		recipeOutput.accept(recipeId, smithingtransformrecipe, advancement$builder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	public SmithingTransformRecipeBuilder unlocks(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return super.unlocks(key, criterion);
	}

	private void ensureValid(ResourceLocation location) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}
