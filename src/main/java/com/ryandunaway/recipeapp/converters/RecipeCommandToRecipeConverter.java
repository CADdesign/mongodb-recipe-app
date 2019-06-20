package com.ryandunaway.recipeapp.converters;

import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipeConverter implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategoryConverter categoryConverter;
    private final IngredientCommandToIngredientConverter ingredientConverter;
    private final NotesCommandToNotesConverter notesConverter;

    public RecipeCommandToRecipeConverter(CategoryCommandToCategoryConverter categoryConverter,
                                          IngredientCommandToIngredientConverter ingredientConverter,
                                          NotesCommandToNotesConverter notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setSource(recipeCommand.getSource());
        recipe.setServings(recipeCommand.getServings());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setNotes(notesConverter.convert(recipeCommand.getNotes()));
        if (recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0) {
            recipeCommand.getIngredients()
                    .forEach(ingredientCommand -> recipe.getIngredients().add(ingredientConverter.convert(ingredientCommand)));
        }
        recipe.setImage(recipeCommand.getImage());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setCookTime(recipeCommand.getCookTime());
        if (recipeCommand.getCategories() != null && recipeCommand.getCategories().size() > 0) {
            recipeCommand.getCategories()
                    .forEach(categoryCommand -> recipe.getCategories().add(categoryConverter.convert(categoryCommand)));
        }
        return recipe;
    }
}
