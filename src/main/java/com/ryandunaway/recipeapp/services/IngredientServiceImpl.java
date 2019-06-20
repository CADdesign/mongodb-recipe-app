package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.converters.IngredientCommandToIngredientConverter;
import com.ryandunaway.recipeapp.converters.IngredientToIngredientCommandConverter;
import com.ryandunaway.recipeapp.formobjects.IngredientCommand;
import com.ryandunaway.recipeapp.model.Ingredient;
import com.ryandunaway.recipeapp.model.Recipe;
import com.ryandunaway.recipeapp.repositories.RecipeRepo;
import com.ryandunaway.recipeapp.repositories.UnitOfMeasureRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter;
    private final IngredientCommandToIngredientConverter ingredientCommandToIngredientConverter;

    private final RecipeRepo recipeRepo;
    private final UnitOfMeasureRepo unitOfMeasureRepo;

    public IngredientServiceImpl(IngredientToIngredientCommandConverter ingredientToIngredientCommandConverter,
                                 IngredientCommandToIngredientConverter ingredientCommandToIngredientConverter,
                                 RecipeRepo recipeRepo,
                                 UnitOfMeasureRepo unitOfMeasureRepo) {
        this.ingredientToIngredientCommandConverter = ingredientToIngredientCommandConverter;
        this.recipeRepo = recipeRepo;
        this.ingredientCommandToIngredientConverter = ingredientCommandToIngredientConverter;
        this.unitOfMeasureRepo = unitOfMeasureRepo;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(long recipeId, long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepo.findById(recipeId);


        if (!recipeOptional.isPresent()) {
            LOGGER.error("Recipe id not found.  Id passed: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommandConverter::convert).findFirst();

        if(!ingredientCommandOptional.isPresent()) {
            LOGGER.error("Ingredient was not found. ID: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepo.findById(command.getRecipeId());
        //TODO: toss error if not found!
        if (!recipeOptional.isPresent()) {
            LOGGER.error("Recipe not found for ID: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepo
                        .findById(command.getUom().getId())
                        .orElseThrow(()-> new RuntimeException("UOM NOT FOUND")));
            } else {
                // Add new ingredient
                Ingredient ingredient = ingredientCommandToIngredientConverter.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepo.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            // Check by description

            if(!savedIngredientOptional.isPresent()) {
                // not safe really, but best guess?
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            // TODO: check for failure
            return ingredientToIngredientCommandConverter.convert(savedIngredientOptional.get());
        }




//        Ingredient convertedIngredient = ingredientCommandToIngredientConverter.convert(ingredientCommand);
//        if (convertedIngredient == null) {
//            return null;
//        }
//
//        Optional<Recipe> recipeOptional = recipeRepo.findById(convertedIngredient.getRecipe().getId());
//        if (!recipeOptional.isPresent()) {
//            LOGGER.error("Recipe isn't found:?");
//        }

//        recipeOptional.get().getIngredients().add(convertedIngredient);
    }

    @Override
    @Transactional
    public void deleteById(long recipeId, long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepo.findById(recipeId);
        if (recipeOptional.isPresent()) {
            LOGGER.debug("Recipe was found ");
            Optional<Ingredient> ingredientOptional = recipeOptional.get().getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                LOGGER.debug("Ingredient was found");
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setRecipe(null);
                recipeOptional.get().getIngredients().remove(ingredient);

            }
            recipeRepo.save(recipeOptional.get());
        } else {
            throw new RuntimeException("Recipe ID is invalid " + ingredientId);
        }


    }
}
