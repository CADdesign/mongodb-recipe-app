package com.ryandunaway.recipeapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;

    @ManyToOne
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
