package ru.rabiarill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rabiarill.exception.model.NoAccessException;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.models.user.User;
import ru.rabiarill.repositories.NoteRepository;
import ru.rabiarill.util.security.UserDetailsImpl;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NoteService {

   private final NoteRepository noteRepository;

   @Autowired
   public NoteService(NoteRepository noteRepository) {
      this.noteRepository = noteRepository;
   }

   public List<Note> findAll(){
      return noteRepository.findAll();
   }

   public Note findOne(int id) throws NoteNotFoundException {
      return noteRepository
              .findById(id)
              .orElseThrow(() -> new NoteNotFoundException("Note with id = " + id + " not found"));
   }

   public List<Note> findByOwner() {
      int ownerId = getUserFromContextHolder().getId();
      return noteRepository.findByOwnerId(ownerId);
   }

   @Transactional
   public void save(Note note) {
      User sender = getUserFromContextHolder();

      note.setOwner(sender);

      noteRepository.save(note);
   }

   @Transactional
   public void update(Note noteToUpdate, int id) throws NoAccessException, NoteNotFoundException {
      Note noteDB = this.findOne(id);
      User sender = getUserFromContextHolder();

      if (!hasAccess(sender, noteDB))
         throw new NoAccessException("You should be owner of note or has role \"ADMIN\"");

      noteDB.updateFields(noteToUpdate);
      noteRepository.save(noteDB);
   }

   @Transactional
   public void delete(int id) throws NoteNotFoundException, NoAccessException {
      User sender = getUserFromContextHolder();
      Note note  = this.findOne(id);

      if (!hasAccess(sender, note))
         throw new NoAccessException("You should be owner of note or has role \"ADMIN\"");

      noteRepository.deleteById(id);
   }

   private boolean hasAccess(User sender, Note note) {
      return sender.getId() == note.getOwner().getId() || sender.idAdmin();
   }

   private User getUserFromContextHolder(){
      UserDetailsImpl authentication = (UserDetailsImpl) SecurityContextHolder
              .getContext().getAuthentication().getPrincipal();

      return authentication.getUser();
   }


}
