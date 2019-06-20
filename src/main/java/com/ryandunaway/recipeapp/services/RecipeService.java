package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.model.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(long l);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand findCommandById(long anyLong);

    void deleteById(long anyLong);
}
