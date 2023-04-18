package ru.rabiarill.dto.response;

import java.time.LocalDateTime;

public class ErrMessageDTO {
   private String message;
   private LocalDateTime timestamp;

   public ErrMessageDTO(String message, LocalDateTime timestamp) {
      this.message = message;
      this.timestamp = timestamp;
   }

   public String getMessage() {
      return message;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }
}
