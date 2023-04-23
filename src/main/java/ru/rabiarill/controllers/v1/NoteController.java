package ru.rabiarill.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.model.note.NoteDTO;
import ru.rabiarill.exception.model.NoAccessException;
import ru.rabiarill.exception.model.note.NotValidNoteException;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.NoteService;
import ru.rabiarill.util.security.UserUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/finance-api/v1/note")
public class NoteController {

   private final NoteService noteService;
   private final UserUtil userUtil;

   @Autowired
   public NoteController(NoteService noteService, UserUtil userUtil) {
      this.noteService = noteService;
      this.userUtil = userUtil;
   }

   @GetMapping()
   public ResponseEntity<List<NoteDTO>> findAll() {
      User sender = userUtil.getUserFromContextHolder();

      List<NoteDTO> response = Note.convertListToDTO(noteService.findByOwnerId(sender.getId()));

      return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @PostMapping()
   public ResponseEntity<HttpStatus> create(@RequestBody @Valid NoteDTO noteDTO,
                                            BindingResult bindingResult) {
      if (bindingResult.hasErrors())
         throw new NotValidNoteException(bindingResult.getFieldErrors());

      Note noteToSave = noteDTO.convertToNote();

      noteService.save(noteToSave);

      return new ResponseEntity<>(HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   public ResponseEntity<HttpStatus> update(@RequestBody @Valid NoteDTO noteDTO,
                                            BindingResult bindingResult,
                                            @PathVariable("id") int id) {
      if (bindingResult.hasErrors())
         throw new NotValidNoteException(bindingResult.getFieldErrors());

      Note noteDB = noteService.findOne(id);
      User sender = userUtil.getUserFromContextHolder();
      if (!userUtil.hasAccess(sender, noteDB))
         throw new NoAccessException("You should be owner of note or has role \"ADMIN\"");

      Note noteToUpdate = noteDTO.convertToNote();
      noteToUpdate.setOwner(noteDB.getOwner());

      noteService.update(noteToUpdate, id);

      return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<HttpStatus> deleteById(@PathVariable(value = "id") int id) throws NoteNotFoundException {

      User sender = userUtil.getUserFromContextHolder();
      Note note = noteService.findOne(id);

      if (!userUtil.hasAccess(sender, note))
         throw new NoAccessException("You should be owner of note or has role \"ADMIN\"");

      noteService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

}
