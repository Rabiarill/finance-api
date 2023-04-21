package ru.rabiarill.exception.model.note;

public class NoteNotFoundException extends RuntimeException {
   public NoteNotFoundException() {
   }

   public NoteNotFoundException(String message) {
      super(message);
   }
}
