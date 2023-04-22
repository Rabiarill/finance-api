package ru.rabiarill.services;

import org.springframework.stereotype.Service;
import ru.rabiarill.dto.statistic.StatisticByCategoryDTO;
import ru.rabiarill.dto.statistic.StatisticDTO;
import ru.rabiarill.models.note.Note;

import java.util.Collections;
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
                      .map(s -> Note.createStatistic(s.getKey(), s.getValue()))
                      .collect(Collectors.toList());

      return new StatisticDTO(statistics);
   }

   public StatisticDTO createByCategory(String category, List<Note> notes) {
      StatisticByCategoryDTO statistic = Note.createStatistic(category, notes);

      return new StatisticDTO(Collections.singletonList(statistic));
   }

   private Map<String, List<Note>> groupByCategory(List<Note> notes) {
      return notes
              .stream()
              .collect(Collectors.groupingBy(Note::getCategory));
   }

}
