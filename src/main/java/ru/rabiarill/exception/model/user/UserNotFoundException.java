package ru.rabiarill.exception.model.user;

public class UserNotFoundException extends RuntimeException {

   public UserNotFoundException() {
   }

   public UserNotFoundException(String message) {
      super(message);
   }
}
