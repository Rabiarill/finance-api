package ru.rabiarill.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.rabiarill.dto.statistic.StatisticByCategoryDTO;
import ru.rabiarill.dto.statistic.StatisticDTO;
import ru.rabiarill.exception.model.note.NoteNotFoundException;
import ru.rabiarill.models.note.Note;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

   private static final BigDecimal TOTAL = BigDecimal.valueOf(600.62);
   private static final BigDecimal AVG = BigDecimal.valueOf(300.31);
   private static final BigDecimal MAX = BigDecimal.valueOf(400.26);
   private static final BigDecimal MIN = BigDecimal.valueOf(200.36);

   @InjectMocks
   StatisticService statisticService;

   @Test
   void createGroupByCategory_ValidNotes_ReturnsValidStatisticDTO() {

      StatisticDTO statistic = this.statisticService.createGroupByCategory(getMockNoteList());

      assertEquals(1, statistic.getStatistic().size());
      StatisticByCategoryDTO expected = statistic.getStatistic().get(0);
      assertEquals(TOTAL, expected.getTotal());
      assertEquals(AVG, expected.getAverage());
      assertEquals(MAX, expected.getMax());
      assertEquals(MIN, expected.getMin());

   }

   @Test
   void createFromList_EmptyList_ThrowsNoteNotFoundException() {
      List<Note> emptyList = new ArrayList<>();
      String category = "Test";

      NoteNotFoundException thrown = assertThrows(NoteNotFoundException.class,
              () -> statisticService.createFromList(category, emptyList));
      assertEquals("List with category " + category + " is empty", thrown.getMessage());
   }

   private List<Note> getMockNoteList(){
      return List.of(new Note(1, MAX, "testCategory", "", LocalDateTime.of(2020,11,5,16,30)),
              new Note(2, MIN, "testCategory", "", LocalDateTime.of(2020,12,20,16,35)));
   }

}