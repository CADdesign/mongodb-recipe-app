package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.formobjects.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(long recipeId, long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(long recipeId, long ingredientId);
}
