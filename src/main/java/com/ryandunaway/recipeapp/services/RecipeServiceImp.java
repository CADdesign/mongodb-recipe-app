package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.converters.RecipeCommandToRecipeConverter;
import com.ryandunaway.recipeapp.converters.RecipeToRecipeCommandConverter;
import com.ryandunaway.recipeapp.exceptions.NotFoundException;
import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.model.Recipe;
import com.ryandunaway.recipeapp.repositories.RecipeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImp implements RecipeService {

    private final RecipeRepo recipeRepo;
    private final RecipeCommandToRecipeConverter recipeCommandToRecipeConverter;
    private final RecipeToRecipeCommandConverter recipeToRecipeCommandConverter;

    public RecipeServiceImp(RecipeRepo recipeRepo, RecipeCommandToRecipeConverter recipeCommandToRecipeConverter,
                            RecipeToRecipeCommandConverter recipeToRecipeCommandConverter) {
        this.recipeRepo = recipeRepo;
        this.recipeCommandToRecipeConverter = recipeCommandToRecipeConverter;
        this.recipeToRecipeCommandConverter = recipeToRecipeCommandConverter;
    }

    @Override
    public Set<Recipe> getRecipes() {
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepo.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(long passedId) {

        Optional<Recipe> recipeOptional = recipeRepo.findById(passedId);
        if (!recipeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found for the ID of: " + passedId);
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        // Still detached from the Hibernate context.
        Recipe detachedRecipe = recipeCommandToRecipeConverter.convert(command);

        // Persist this to the RecipeRepo.
        Recipe savedRecipe = recipeRepo.save(detachedRecipe);
        log.debug("Saved recipeId: " + savedRecipe.getId());
        return recipeToRecipeCommandConverter.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(long anyLong) {
        // First we need to see if we can find the value in the repo
        Recipe foundRecipe = findById(anyLong);

        RecipeCommand convertedRecipe = recipeToRecipeCommandConverter.convert(foundRecipe);
        if (convertedRecipe != null) {
            log.debug("The convertedRecipe id = " + convertedRecipe.getId());
        } else {
            log.error("We apparently can't convert this recipe??? I'm in RecipeServiceImpl line 73");
        }

        return convertedRecipe;

    }

    @Override
    @Transactional
    public void deleteById(long anyLong) {
        recipeRepo.deleteById(anyLong);
    }
}
