package com.ryandunaway.recipeapp.repositories;

import com.ryandunaway.recipeapp.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepo extends CrudRepository<Recipe, Long> {
}
