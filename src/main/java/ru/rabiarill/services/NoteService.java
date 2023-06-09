package ru.rabiarill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.repositories.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NoteService {

   private final NoteRepository noteRepository;

   @Autowired
   public NoteService(NoteRepository noteRepository) {
      this.noteRepository = noteRepository;
   }

   public List<Note> findAll() {
      return noteRepository.findAll();
   }

   public Note findOne(int id) throws NoteNotFoundException {
      return noteRepository
              .findById(id)
              .orElseThrow(() -> new NoteNotFoundException("Note with id = " + id + " not found"));
   }

   public List<Note> findByOwnerId(int ownerId) {
      return noteRepository.findByOwnerId(ownerId);
   }

   public List<Note> findByOwnerAndCategory(int ownerId, String category) {
      return noteRepository.findByOwnerIdAndCategory(ownerId, category);
   }

   public List<Note> findByOwnerAndDate(int userId, LocalDateTime startDate, LocalDateTime endDate) {
      return noteRepository.findByOwnerIdAndTransactionDateBetween(userId, startDate, endDate);
   }

   public List<Note> findByOwnerAndCategoryAndDate(int userId, String category,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate) {
      return noteRepository.findByOwnerIdAndCategoryAndTransactionDateBetween(userId, category, startDate, endDate);
   }

   @Transactional
   public void save(Note note) {
      noteRepository.save(note);
   }

   @Transactional
   public void update(Note noteToUpdate, int id) throws NoteNotFoundException {
      noteToUpdate.setId(id);
      noteRepository.save(noteToUpdate);
   }

   @Transactional
   public void delete(int id) {
      noteRepository.deleteById(id);
   }

   @Transactional
   public void deleteAll(){
      noteRepository.deleteAll();
   }

}
