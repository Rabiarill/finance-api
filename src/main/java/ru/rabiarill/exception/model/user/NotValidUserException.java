package ru.rabiarill.exception.model.user;

import org.springframework.validation.FieldError;
import ru.rabiarill.exception.model.ModelFieldException;

import java.util.List;

public class NotValidUserException extends ModelFieldException {

   public NotValidUserException(List<FieldError> fieldErrors) {
      super(fieldErrors);
   }

}
