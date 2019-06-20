package com.ryandunaway.recipeapp.repositories;

import com.ryandunaway.recipeapp.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepo extends CrudRepository<UnitOfMeasure, Long> {
    
    Optional<UnitOfMeasure> findByDescription(String description);
}
