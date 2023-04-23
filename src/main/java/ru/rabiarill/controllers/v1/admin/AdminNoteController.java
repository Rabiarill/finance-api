package ru.rabiarill.controllers.v1.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.model.note.NoteDTO;
import ru.rabiarill.exception.model.note.NotValidNoteException;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.services.NoteService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/finance-api/v1/admin/note")
public class AdminNoteController {

   private final NoteService noteService;

   @Autowired
   public AdminNoteController(NoteService noteService) {
      this.noteService = noteService;
   }

   @GetMapping()
   public ResponseEntity<List<NoteDTO>> findAll() {
      List<NoteDTO> noteDTO = noteService.findAll().stream()
              .map(Note::convertToDTO)
              .collect(Collectors.toList());
      return new ResponseEntity<>(noteDTO, HttpStatus.OK);
   }

   @GetMapping("/{id}")
   public ResponseEntity<NoteDTO> findOne(@PathVariable("id") int id) {
      return new ResponseEntity<>(noteService.findOne(id).convertToDTO(), HttpStatus.OK);
   }

   @PutMapping("/{id}")
   public ResponseEntity<HttpStatus> update(@RequestBody @Valid NoteDTO noteDTO,
                                            BindingResult bindingResult,
                                            @PathVariable("id") int id) {
      if (bindingResult.hasErrors())
         throw new NotValidNoteException(bindingResult.getFieldErrors());

      Note noteDB = noteService.findOne(id);
      Note noteToUpdate = noteDTO.convertToNote();

      noteToUpdate.setOwner(noteDB.getOwner());

      noteService.update(noteToUpdate, id);

      return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<HttpStatus> deleteNote(@PathVariable("id") int id) {
      noteService.delete(id);
      return new ResponseEntity<>(HttpStatus.OK);
   }

}
