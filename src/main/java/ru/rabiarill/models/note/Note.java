package ru.rabiarill.models.note;

import org.modelmapper.ModelMapper;
import ru.rabiarill.dto.model.note.NoteDTO;
import ru.rabiarill.dto.statistic.StatisticByCategoryDTO;
import ru.rabiarill.models.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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


   public Note() {
   }

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

   public NoteDTO convertToDTO() {
      return new ModelMapper().map(this, NoteDTO.class);
   }

   public static List<NoteDTO> convertListToDTO(List<Note> notes) {
      return notes.stream()
              .map(Note::convertToDTO)
              .collect(Collectors.toList());
   }

   public static StatisticByCategoryDTO createStatistic(String category, List<Note> notes) {
      BigDecimal total = BigDecimal.valueOf(notes.stream()
              .mapToDouble(note -> note.getAmount().doubleValue()).sum())
              .setScale(2, RoundingMode.HALF_UP);

      BigDecimal avg = BigDecimal.valueOf(notes.stream()
              .mapToDouble(note -> note.getAmount().doubleValue()).average().orElse(0.0))
              .setScale(2, RoundingMode.HALF_UP);

      BigDecimal max = BigDecimal.valueOf(notes.stream()
              .mapToDouble(note -> note.getAmount().doubleValue()).max().orElse(0.0))
              .setScale(2, RoundingMode.HALF_UP);

      BigDecimal min = BigDecimal.valueOf(notes.stream()
              .mapToDouble(note -> note.getAmount().doubleValue()).min().orElse(0.0))
              .setScale(2, RoundingMode.HALF_UP);

      return new StatisticByCategoryDTO(category, total, avg, max, min);
   }

   public void updateFields(Note noteWithNewFields) {
      this.amount = noteWithNewFields.getAmount();
      this.category = noteWithNewFields.getCategory();
      this.description = noteWithNewFields.getDescription();
      this.transactionDate = noteWithNewFields.getTransactionDate();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Note note = (Note) o;
      return Objects.equals(owner, note.owner) &&
              Objects.equals(amount, note.amount) &&
              Objects.equals(category, note.category) &&
              Objects.equals(description, note.description) &&
              Objects.equals(transactionDate, note.transactionDate);
   }

   @Override
   public int hashCode() {
      return Objects.hash(owner, amount, category, description, transactionDate);
   }
}
