package ru.rabiarill.exception.model.note;

public class NoteNotFoundException extends Exception {
   public NoteNotFoundException() {
   }

   public NoteNotFoundException(String message) {
      super(message);
   }
}
