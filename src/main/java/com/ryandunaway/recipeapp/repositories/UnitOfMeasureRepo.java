package com.ryandunaway.recipeapp.repositories;

import com.ryandunaway.recipeapp.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepo extends CrudRepository<UnitOfMeasure, String> {
    
    Optional<UnitOfMeasure> findByDescription(String description);
}
