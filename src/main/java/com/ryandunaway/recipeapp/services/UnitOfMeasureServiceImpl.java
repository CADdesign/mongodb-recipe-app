package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommandConverter;
import com.ryandunaway.recipeapp.formobjects.UnitOfMeasureCommand;
import com.ryandunaway.recipeapp.repositories.UnitOfMeasureRepo;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepo unitOfMeasureRepo;
    private final UnitOfMeasureToUnitOfMeasureCommandConverter converter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepo unitOfMeasureRepo,
                                    UnitOfMeasureToUnitOfMeasureCommandConverter converter) {
        this.unitOfMeasureRepo = unitOfMeasureRepo;
        this.converter = converter;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(unitOfMeasureRepo.findAll()
                .spliterator(), false)
                .map(converter::convert)
                .collect(Collectors.toSet());
    }
}
