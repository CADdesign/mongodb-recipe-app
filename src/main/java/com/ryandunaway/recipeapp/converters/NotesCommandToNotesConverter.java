package com.ryandunaway.recipeapp.converters;

import com.ryandunaway.recipeapp.formobjects.NotesCommand;
import com.ryandunaway.recipeapp.model.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotesConverter implements Converter<NotesCommand, Notes> {


    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesCommand notesCommand) {
        if(notesCommand == null) {
            return null;
        }

        final Notes notes = new Notes();
        notes.setId(notesCommand.getId());
        notes.setRecipeNotes(notesCommand.getRecipeNotes());
        return notes;
    }
}
