package ru.rabiarill.services;

import org.springframework.stereotype.Service;
import ru.rabiarill.dto.statistic.StatisticByCategoryDTO;
import ru.rabiarill.dto.statistic.StatisticDTO;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.models.note.Note;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {

   public StatisticDTO createGroupByCategory(List<Note> notes) {
      List<StatisticByCategoryDTO> statistics =
              groupByCategory(notes)
                      .entrySet()
                      .stream()
                      .map(s -> createFromList(s.getKey(), s.getValue()))
                      .collect(Collectors.toList());

      return new StatisticDTO(statistics);
   }

   public StatisticByCategoryDTO createFromList(String category, List<Note> notes) {

      if (notes.isEmpty())
         throw new NoteNotFoundException("List with category " + category + " is empty");

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

   private Map<String, List<Note>> groupByCategory(List<Note> notes) {
      return notes
              .stream()
              .collect(Collectors.groupingBy(Note::getCategory));
   }

}
