package com.ryandunaway.recipeapp.converters;

import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.model.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommandConverter implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommandConverter categoryConverter;
    private final IngredientToIngredientCommandConverter ingredientConverter;
    private final NotesToNotesCommandConverter notesConverter;

    public RecipeToRecipeCommandConverter(CategoryToCategoryCommandConverter categoryConverter,
                                          IngredientToIngredientCommandConverter ingredientConverter,
                                          NotesToNotesCommandConverter notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if (source == null) {
            return null;
        }

        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(source.getId());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setSource(source.getSource());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setNotes(notesConverter.convert(source.getNotes()));
        recipeCommand.setImage(source.getImage());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setDifficulty(source.getDifficulty());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setCookTime(source.getCookTime());

        if(source.getIngredients() != null && source.getIngredients().size() > 0) {
            source.getIngredients()
                    .forEach(ingredient ->
                            recipeCommand
                                    .getIngredients()
                                    .add(ingredientConverter.convert(ingredient)));
        }

        if(source.getCategories() != null && source.getCategories().size() > 0) {
            source.getCategories()
                    .forEach(category ->
                            recipeCommand
                                    .getCategories()
                                    .add(categoryConverter.convert(category)));
        }

        return recipeCommand;
    }
}
