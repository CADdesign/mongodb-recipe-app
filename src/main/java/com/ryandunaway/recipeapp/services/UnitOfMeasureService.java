package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.formobjects.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
