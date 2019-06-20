package com.ryandunaway.recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(long recipeId, MultipartFile object);
}
