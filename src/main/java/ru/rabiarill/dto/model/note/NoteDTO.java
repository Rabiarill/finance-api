package ru.rabiarill.dto.model.note;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.modelmapper.ModelMapper;
import ru.rabiarill.models.note.Note;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NoteDTO {

   private int id;

   @NotNull(message = "Amount should not be null")
   private BigDecimal amount;

   @Size(max = 255, message = "Description length should be less than 255")
   private String description;

   @Size(max = 100, message = "Category size should be less than 100")
   @NotBlank(message = "Category should not be blank")
   private String category;

   @NotNull(message = "Transaction date should not be null")
   @JsonSerialize(using = ToStringSerializer.class)
   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
   private LocalDateTime transactionDate;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public LocalDateTime getTransactionDate() {
      return transactionDate;
   }

   public void setTransactionDate(LocalDateTime transactionDate) {
      this.transactionDate = transactionDate;
   }

   public Note convertToNote() {
      return new ModelMapper().map(this, Note.class);
   }

}
