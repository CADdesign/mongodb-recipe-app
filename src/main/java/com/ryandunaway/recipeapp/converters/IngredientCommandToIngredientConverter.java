package com.ryandunaway.recipeapp.converters;

import com.ryandunaway.recipeapp.formobjects.IngredientCommand;
import com.ryandunaway.recipeapp.model.Ingredient;
import com.ryandunaway.recipeapp.model.Recipe;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredientConverter implements Converter<IngredientCommand, Ingredient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientCommandToIngredientConverter.class);
    private final UnitOfMeasureCommandToUnitOfMeasureConverter uomConverter;

    @Autowired
    public IngredientCommandToIngredientConverter(UnitOfMeasureCommandToUnitOfMeasureConverter uomConverter) {
        LOGGER.debug("You're in the this constructor.");
        this.uomConverter = uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if(source == null) {
            return null;
        }
        LOGGER.debug("The command contains: " + source.getId());
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        if(source.getRecipeId() != null) {
            Recipe recipe = new Recipe();
            recipe.setId(source.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUom(uomConverter.convert(source.getUom()));

        return ingredient;
    }
}
