package ru.rabiarill.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.statistic.StatisticByCategoryDTO;
import ru.rabiarill.dto.statistic.StatisticDTO;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.services.NoteService;
import ru.rabiarill.services.StatisticService;
import ru.rabiarill.util.security.UserUtil;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/finance-api/v1/statistic")
public class StatisticController {

   private final StatisticService statisticService;
   private final NoteService noteService;
   private final UserUtil userUtil;

   @Autowired
   public StatisticController(StatisticService statisticService, NoteService noteService, UserUtil userUtil) {
      this.statisticService = statisticService;
      this.noteService = noteService;
      this.userUtil = userUtil;
   }

   @GetMapping()
   public ResponseEntity<StatisticDTO> getByUser(
           @RequestParam(name = "startDate", required = false)
           @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS") LocalDateTime startDate,

           @RequestParam(name = "endDate", required = false)
           @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS") LocalDateTime endDate) {

      List<Note> notes;
      int senderId = userUtil.getUserFromContextHolder().getId();

      if (startDate != null && endDate != null)
         notes = noteService.findByOwnerAndDate(senderId, startDate, endDate);
      else
         notes = noteService.findByOwnerId(senderId);

      StatisticDTO statistics = statisticService.createGroupByCategory(notes);

      return new ResponseEntity<>(statistics, HttpStatus.OK);
   }

   @GetMapping("/{category}")
   public ResponseEntity<StatisticDTO> getByUserAndCategory(
           @PathVariable("category") String category,
           @RequestParam(name = "startDate", required = false)
           @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS") LocalDateTime startDate,

           @RequestParam(name = "endDate", required = false)
           @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS") LocalDateTime endDate) {

      List<Note> notes;
      int senderId = userUtil.getUserFromContextHolder().getId();

      if (startDate != null && endDate != null)
         notes = noteService.findByOwnerAndCategoryAndDate(senderId, category, startDate, endDate);
      else
         notes = noteService.findByOwnerAndCategory(senderId, category);

      StatisticByCategoryDTO statistic = statisticService.createFromList(category, notes);

      return new ResponseEntity<>(new StatisticDTO(List.of(statistic)), HttpStatus.OK);
   }

}
