package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.INoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NoteService {
    private final INoteMapper noteMapper;

    public void upsert(Note note) {
        if (Objects.isNull(note.getNoteId())) {
            noteMapper.insert(note);
        } else {
            noteMapper.update(note);
        }
    }

    public Note get(int id) {
        return noteMapper.findById(id);
    }

    public List<Note> getAllByUser(int userId) {
        return noteMapper.getAllByUserId(userId);
    }

    public boolean deleteById(int id) {
        return noteMapper.deleteById(id) == 1;
    }
}
