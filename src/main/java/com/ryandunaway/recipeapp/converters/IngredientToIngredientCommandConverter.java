package com.ryandunaway.recipeapp.converters;

import com.ryandunaway.recipeapp.formobjects.IngredientCommand;
import com.ryandunaway.recipeapp.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommandConverter implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommandConverter uomConverter;

    public IngredientToIngredientCommandConverter(UnitOfMeasureToUnitOfMeasureCommandConverter uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredient.getId());
        if (ingredient.getRecipe() != null) {
            ingredientCommand.setRecipeId(ingredient.getRecipe().getId());
        }
        ingredientCommand.setAmount(ingredient.getAmount());
        ingredientCommand.setDescription(ingredient.getDescription());
        ingredientCommand.setUom(uomConverter.convert(ingredient.getUom()));
        return ingredientCommand;
    }
}
