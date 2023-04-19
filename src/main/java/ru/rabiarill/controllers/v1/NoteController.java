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
import ru.rabiarill.services.NoteService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/finance-api/v1/note")
public class NoteController {

   private final NoteService noteService;

   @Autowired
   public NoteController(NoteService noteService) {
      this.noteService = noteService;
   }

   @GetMapping()
   public ResponseEntity<List<NoteDTO>> findAll(){

      List<NoteDTO> response = Note.convertListToDTO(noteService.findByOwner());

      return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @PutMapping("/{id}")
   public ResponseEntity<HttpStatus> update(@RequestBody @Valid NoteDTO noteDTO,
                                            BindingResult bindingResult,
                                            @PathVariable("id") int id) throws NoAccessException, NoteNotFoundException {
      if (bindingResult.hasErrors())
         throw new NotValidNoteException();

      Note noteToUpdate = noteDTO.convertToNote();

      noteService.update(noteToUpdate, id);

      return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<HttpStatus> deleteById(@PathVariable(value = "id") int id)
           throws NoteNotFoundException, NoAccessException {

      noteService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }



}
