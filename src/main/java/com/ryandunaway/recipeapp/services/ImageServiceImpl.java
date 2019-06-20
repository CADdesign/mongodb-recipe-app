package com.ryandunaway.recipeapp.services;

import com.ryandunaway.recipeapp.model.Recipe;
import com.ryandunaway.recipeapp.repositories.RecipeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class ImageServiceImpl implements ImageService {

    private final RecipeRepo recipeRepo;

    public ImageServiceImpl(RecipeRepo recipeRepo) {

        this.recipeRepo = recipeRepo;
    }

    @Override
    @Transactional
    public void saveImageFile(long recipeId, MultipartFile file) {
        log.debug("RecipeID passed" + recipeId);
        try {
            Optional<Recipe> recipeOptional = recipeRepo.findById(recipeId);
            if (recipeOptional.isPresent()){
                Byte[] byteObjects = new Byte[file.getBytes().length];

                int i = 0;
                for (byte b : file.getBytes()) {
                    byteObjects[i++] = b;
                }

                recipeOptional.get().setImage(byteObjects);

                recipeRepo.save(recipeOptional.get());
            }

        } catch (IOException e){
            // TODO handle this better later on.
            log.error("Error occured: ", e);
        }
    }
}
