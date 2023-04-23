package ru.rabiarill.dto.response;

import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrResponse {

   private List<ErrMessageDTO> errors = new ArrayList<>();

   public ErrResponse() {
   }

   public ErrResponse(List<ErrMessageDTO> errors) {
      this.errors = errors;
   }

   public ErrResponse(Exception e) {
      this.errors.add(new ErrMessageDTO(e.getMessage(), LocalDateTime.now()));
   }

   public List<ErrMessageDTO> getErrors() {
      return errors;
   }

   public void setErrors(List<ErrMessageDTO> errors) {
      this.errors = errors;
   }

   public void addError(Exception e) {
      this.errors
              .add(new ErrMessageDTO(e.getMessage(), LocalDateTime.now()));
   }

   public void addErrors(List<FieldError> fieldErrors) {
      this.errors.addAll(
              fieldErrors.stream()
                      .map(e -> new ErrMessageDTO(e.getField() + " - " + e.getDefaultMessage(), LocalDateTime.now()))
                      .collect(Collectors.toList()));
   }
}
