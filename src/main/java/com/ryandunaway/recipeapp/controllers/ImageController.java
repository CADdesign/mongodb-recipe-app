package com.ryandunaway.recipeapp.controllers;

import com.ryandunaway.recipeapp.formobjects.RecipeCommand;
import com.ryandunaway.recipeapp.services.ImageService;
import com.ryandunaway.recipeapp.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable String recipeId,
                                 Model model) {

        RecipeCommand recipe = recipeService.findCommandById(Long.valueOf(recipeId));

        model.addAttribute("recipe", recipe);
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable String recipeId,
                                @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(recipeId), file);

        return "redirect:/recipe/" + recipeId + "/show";

    }

    @GetMapping("/recipe/{recipeId}/recipeimage")
    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        if (recipeCommand.getImage() != null) {
            byte[] byteArray = new byte[recipeCommand.getImage().length];
            int i = 0;
            for (Byte wrappedByte : recipeCommand.getImage()) {
                byteArray[i++] = wrappedByte; //auto boxing
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
