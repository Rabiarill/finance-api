package ru.rabiarill.exception.model;

public class NoAccessException extends Exception {
   public NoAccessException() {
   }

   public NoAccessException(String message) {
      super(message);
   }
}
