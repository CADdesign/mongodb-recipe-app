package com.ryandunaway.recipeapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode(exclude = {"recipe"})
public class Ingredient {

    private String id;
    private String description;
    private BigDecimal amount;

    private UnitOfMeasure uom;

    private Recipe recipe;

    public Ingredient(){}

    private Ingredient(String description, BigDecimal bigDecimal, UnitOfMeasure eachUom) {
        this.description = description;
        this.amount = bigDecimal;
        this.uom = eachUom;
    }

    public static Ingredient createIngredient(String description, BigDecimal bigDecimal, UnitOfMeasure eachUom) {
        return new Ingredient(description, bigDecimal, eachUom);
    }

}
