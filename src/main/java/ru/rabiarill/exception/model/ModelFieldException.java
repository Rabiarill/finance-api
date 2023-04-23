package ru.rabiarill.exception.model;

import org.springframework.validation.FieldError;

import java.util.List;

public class ModelFieldException extends RuntimeException {

   private List<FieldError> fieldErrors;

   public ModelFieldException(List<FieldError> fieldErrors) {
      this.fieldErrors = fieldErrors;
   }

   public List<FieldError> getFieldErrors() {
      return fieldErrors;
   }

   public void setFieldErrors(List<FieldError> fieldErrors) {
      this.fieldErrors = fieldErrors;
   }
}
