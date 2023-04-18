package ru.rabiarill.models.note;

import org.modelmapper.ModelMapper;
import ru.rabiarill.dto.model.note.NoteDTO;
import ru.rabiarill.models.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Note")
public class Note {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   @ManyToOne
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private User owner;

   @Column(name = "amount")
   private BigDecimal amount;

   @Column(name = "category")
   private String category;

   @Column(name = "description")
   private String description;

   @Column(name = "transaction_date")
   private LocalDateTime transactionDate;


   public Note() { }

   public Note(BigDecimal amount, String category, String description, LocalDateTime transactionDate) {
      this.amount = amount;
      this.category = category;
      this.description = description;
      this.transactionDate = transactionDate;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public User getOwner() {
      return owner;
   }

   public void setOwner(User owner) {
      this.owner = owner;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public LocalDateTime getTransactionDate() {
      return transactionDate;
   }

   public void setTransactionDate(LocalDateTime transactionDate) {
      this.transactionDate = transactionDate;
   }

   public NoteDTO convertToDTO(){
      return new ModelMapper().map(this, NoteDTO.class);
   }

   public static List<NoteDTO> convertListToDTO(List<Note> notes) {
      return notes.stream()
              .map(Note::convertToDTO)
              .collect(Collectors.toList());
   }

   public void updateFields(Note noteWithNewFields) {
      this.amount = noteWithNewFields.getAmount();
      this.category = noteWithNewFields.getCategory();
      this.description = noteWithNewFields.getDescription();
      this.transactionDate = noteWithNewFields.getTransactionDate();
   }
}
