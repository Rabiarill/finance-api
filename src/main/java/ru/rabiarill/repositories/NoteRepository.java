package ru.rabiarill.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rabiarill.models.note.Note;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
   List<Note> findByOwnerId(int id);

   List<Note> findByOwnerIdAndTransactionDateBetween(int ownerId, LocalDateTime startDate, LocalDateTime endDate);

   List<Note> findByOwnerIdAndCategory(int ownerId, String category);

   List<Note> findByOwnerIdAndCategoryAndTransactionDateBetween(int owner_id,
                                                                String category,
                                                                LocalDateTime transactionDate,
                                                                LocalDateTime transactionDate2);
}
