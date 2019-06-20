package com.ryandunaway.recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormat(Exception ex) {
        log.error("Incorrect number format passed: ");
        log.error(ex.getMessage());

        ModelAndView mv = new ModelAndView();
        mv.setViewName("400error");
        mv.addObject("exception", ex);

        return mv;
    }

}
