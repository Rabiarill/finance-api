package ru.rabiarill.exception.model.note;

import org.springframework.validation.FieldError;
import ru.rabiarill.exception.model.ModelFieldException;

import java.util.List;

public class NotValidNoteException extends ModelFieldException {

   public NotValidNoteException(List<FieldError> fieldErrors) {
      super(fieldErrors);
   }
}
