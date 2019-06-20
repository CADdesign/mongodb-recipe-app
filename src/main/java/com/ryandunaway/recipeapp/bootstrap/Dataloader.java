package com.ryandunaway.recipeapp.bootstrap;

import com.ryandunaway.recipeapp.model.*;
import com.ryandunaway.recipeapp.repositories.CategoryRepo;
import com.ryandunaway.recipeapp.repositories.RecipeRepo;
import com.ryandunaway.recipeapp.repositories.UnitOfMeasureRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class Dataloader implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepo recipeRepo;
    private final CategoryRepo categoryRepo;
    private final UnitOfMeasureRepo unitOfMeasureRepo;

    @Autowired
    public Dataloader(RecipeRepo recipeRepo, CategoryRepo categoryRepo, UnitOfMeasureRepo unitOfMeasureRepo) {
        this.recipeRepo = recipeRepo;
        this.categoryRepo = categoryRepo;
        this.unitOfMeasureRepo = unitOfMeasureRepo;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepo.saveAll(getRecipes());
        log.debug("Loading bootstrap data");
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>(2);

        // Get all the Unit Of Measurements from the sql
        UnitOfMeasure eachUom = findUOM("Each");
        UnitOfMeasure tablespoonUom = findUOM("Tablespoon");
        UnitOfMeasure teaspoonUom = findUOM("Teaspoon");
        UnitOfMeasure dashUom = findUOM("Dash");
        UnitOfMeasure pintUom = findUOM("Pint");
        UnitOfMeasure cupsUom = findUOM("Cup");
        UnitOfMeasure gloveUom = findUOM("Glove");

        // Get the Categories
        Category american = findCategory("American");
        Category mexican = findCategory("Mexican");

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");
        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("For a very quick guacamole just take...");
        guacRecipe.setNotes(guacNotes);

        guacRecipe.addIngredient(Ingredient.createIngredient("ripe avocados", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("Kosher salt", new BigDecimal(".5"), teaspoonUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("fresh lime juice or lemon juice", new BigDecimal(2), tablespoonUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tablespoonUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("cilantro, finely chopped", new BigDecimal(2), tablespoonUom));
        guacRecipe.addIngredient(Ingredient.createIngredient("freshly grated black pepper", new BigDecimal(2), dashUom));

        guacRecipe.getCategories().add(american);
        guacRecipe.getCategories().add(mexican);

        guacRecipe.setUrl("www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");
        recipes.add(guacRecipe);


        // Add the Tacos Recipe next
        Recipe chickTacosRecipe = new Recipe();
        chickTacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        chickTacosRecipe.setCookTime(9);
        chickTacosRecipe.setPrepTime(20);
        chickTacosRecipe.setDifficulty(Difficulty.MODERATE);

        chickTacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n");

        Notes chkNotes = new Notes();
        chkNotes.setRecipeNotes("Look for ancho chile powder with the Mexican ingredients at your grocery store, on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)");
        chickTacosRecipe.setNotes(chkNotes);
        chickTacosRecipe.addIngredient(createIngredient("ancho chili powder", new BigDecimal(1), tablespoonUom));
        chickTacosRecipe.addIngredient(createIngredient("dried oregano", new BigDecimal(1), teaspoonUom));
        chickTacosRecipe.addIngredient(createIngredient("sugar", new BigDecimal(1), teaspoonUom));
        chickTacosRecipe.addIngredient(createIngredient("salt", new BigDecimal(.5), teaspoonUom));
        chickTacosRecipe.addIngredient(createIngredient("garlic clove", new BigDecimal(1), gloveUom));
        chickTacosRecipe.addIngredient(createIngredient("orange zest", new BigDecimal(1), tablespoonUom));
        chickTacosRecipe.addIngredient(createIngredient("orange juice", new BigDecimal(3), tablespoonUom));
        chickTacosRecipe.addIngredient(createIngredient("olive oil", new BigDecimal(2), tablespoonUom));
        chickTacosRecipe.addIngredient(createIngredient("skinless, boneless chicken thighs", new BigDecimal(6), eachUom));

        chickTacosRecipe.getCategories().add(mexican);
        recipes.add(chickTacosRecipe);


        return recipes;
    }

    private Ingredient createIngredient(String description, BigDecimal amt, UnitOfMeasure unitOfMeasure) {
        return Ingredient.createIngredient(description, amt, unitOfMeasure);

    }

    private UnitOfMeasure findUOM(String desc) {
        log.info("The description passed is: " + desc);
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepo.findByDescription(desc);
        return unitOfMeasureOptional.orElse(null);
    }

    private Category findCategory(String desc) {
        Optional<Category> categoryOptional = categoryRepo.findByDescription(desc);
        return categoryOptional.orElse(null);
    }



//    private Ingredient createIngredient(String desc, BigDecimal amt, UnitOfMeasure uom) {
//        return Ingredient.createIngredient(desc, amt, uom);
//    }

}
