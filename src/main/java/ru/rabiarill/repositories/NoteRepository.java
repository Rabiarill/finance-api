package ru.rabiarill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rabiarill.models.note.Note;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
   List<Note> findByOwnerId(int id);
}
