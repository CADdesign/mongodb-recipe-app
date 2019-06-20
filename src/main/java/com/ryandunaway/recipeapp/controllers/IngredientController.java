package com.ryandunaway.recipeapp.controllers;

import com.ryandunaway.recipeapp.formobjects.IngredientCommand;
import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.formobjects.UnitOfMeasureCommand;
import com.ryandunaway.recipeapp.services.IngredientService;
import com.ryandunaway.recipeapp.services.RecipeService;
import com.ryandunaway.recipeapp.services.UnitOfMeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IngredientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        LOGGER.debug("Getting ingredient list for recipe id: " + recipeId);

        // use command object to avoid lazy load errors in thymeLeaf
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String ingredientId,
                                       Model model) {
        final StringBuilder sb = new StringBuilder();
        sb.append("We are getting the single ingredient of: ");
        sb.append(recipeId);
        sb.append(" with the ingredient id of: ");
        sb.append(ingredientId);
        LOGGER.debug(sb.toString());

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String ingredientId,
                                         Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        LOGGER.debug("Saved recipe id: " + savedCommand.getRecipeId());
        LOGGER.debug("with ingredient id of : " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredientForm(@PathVariable String recipeId,
                                    Model model) {

        // make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        if (recipeCommand == null) {
            throw new RuntimeException("Invalid Recipe ID");
        }

        // need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        model.addAttribute("ingredient", ingredientCommand);
        // init uom
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredientFromRecipe(@PathVariable String recipeId,
                                           @PathVariable String ingredientId) {

        LOGGER.debug("the ID being deleted is: " + ingredientId);
        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(ingredientId));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
