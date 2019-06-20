package com.ryandunaway.recipeapp.controllers;

import com.ryandunaway.recipeapp.exceptions.NotFoundException;
import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.services.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * We are using formObjects to bind the data passed from our exposed (web form) then converting that
 * data from the exposed area to the Model we are trying to bind with.  The formobjects package is to show this.
 * Then we use converters to convert from Exposed Objects to the Model Objects.
 * 6/14/2019.  Remember this when you are doing other things.
 */
@Controller
@RequestMapping
public class RecipeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);
    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
    private static final String RECIPE_SHOW_URL = "recipe/show";
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model) {

        long value = Long.valueOf(id);
        model.addAttribute("recipe", recipeService.findById(value));

        return RECIPE_SHOW_URL;
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;
    }

    // Binds for the form post parameters to the RecipeCommand object automatically by the naming convention.
    @PostMapping("/recipe/")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.debug(objectError.toString()));

            return RECIPE_RECIPEFORM_URL;
        }
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return RECIPE_RECIPEFORM_URL;
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {

        LOGGER.debug("The id to delete: " + id);
        recipeService.deleteById(Long.valueOf(id));

        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception ex) {
        LOGGER.error("Handling not found exception.  Message is: " + ex.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", ex);

        mv.setViewName("404error");

        return mv;
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NumberFormatException.class)
//    public ModelAndView handleNumberFormatException(Exception ex) {
//        LOGGER.error("Number format is incorrect " + ex.getMessage());
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("exception", ex);
//        mv.setViewName("400error");
//        return mv;
//    }
}
