package com.ryandunaway.recipeapp.repositories;

import com.ryandunaway.recipeapp.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepo extends CrudRepository<Category, String> {

    Optional<Category> findByDescription(String description);
}
