package ru.rabiarill.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rabiarill.dto.response.ErrResponse;
import ru.rabiarill.exception.model.ModelFieldException;
import ru.rabiarill.exception.model.NoAccessException;
import ru.rabiarill.exception.model.note.NotValidNoteException;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.exception.model.user.UserNotFoundException;

@ControllerAdvice
public class HandleApiException {

   @ExceptionHandler(value = {BadCredentialsException.class})
   public ResponseEntity<ErrResponse> handle(BadCredentialsException e) {

      ErrResponse errDTO = new ErrResponse(e);

      return new ResponseEntity<>(errDTO, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {NotValidUserException.class, NotValidNoteException.class})
   public ResponseEntity<ErrResponse> handleNotValidModel(ModelFieldException e) {
      ErrResponse errDTO = new ErrResponse();

      errDTO.addErrors(e.getFieldErrors());

      return new ResponseEntity<>(errDTO, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {UserNotFoundException.class, NoteNotFoundException.class})
   public ResponseEntity<ErrResponse> handleNotFound(Exception e) {
      ErrResponse errDTO = new ErrResponse(e);

      return new ResponseEntity<>(errDTO, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {NoAccessException.class})
   public ResponseEntity<ErrResponse> handleNoAccess(NoAccessException e) {
      ErrResponse errDTO = new ErrResponse(e);

      return new ResponseEntity<>(errDTO, HttpStatus.FORBIDDEN);
   }

}
