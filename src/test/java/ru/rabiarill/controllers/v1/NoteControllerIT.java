package ru.rabiarill.controllers.v1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import ru.rabiarill.enumeration.RoleEnum;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.NoteService;
import ru.rabiarill.services.UserService;
import ru.rabiarill.util.security.UserUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, MockitoTestExecutionListener.class })
class NoteControllerIT {

   static final String URL = "/finance-api/v1/note";

   @Autowired
   MockMvc mockMvc;

   @Autowired
   NoteService noteService;

   @Autowired
   UserService userService;

   @MockBean
   UserUtil userUtil;

   @BeforeEach
   void setUp() {
      User testUser1 = getMockUser();
      User testUser2 = new User(2, "username2", "password2", "test_user@mail.com", RoleEnum.ROLE_ADMIN);

      Note testNote1 = getMockNotes().get(0);
      testNote1.setOwner(testUser1);

      Note testNote2 = getMockNotes().get(1);
      testNote2.setOwner(testUser1);

      Note testNote3 = getMockNotes().get(2);
      testNote3.setOwner(testUser2);

      userService.save(testUser1);
      userService.save(testUser2);

      noteService.save(testNote1);
      noteService.save(testNote2);
      noteService.save(testNote3);
   }


   @Test
   void findAll_ValidUser_ReturnsValidResponse() throws Exception {
      when(userUtil.getUserFromContextHolder()).thenReturn(getMockUser());

      mockMvc.perform(get(URL))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.[0].id").value(1))
              .andExpect(jsonPath("$.[0].amount").value(123))
              .andExpect(jsonPath("$.[0].description").value("for IT tests"))
              .andExpect(jsonPath("$.[0].category").value("test"))
              .andExpect(jsonPath("$.[0].transactionDate").value("2023-02-12T16:30"))

              .andExpect(jsonPath("$.[1].id").value(2))
              .andExpect(jsonPath("$.[1].amount").value(321))
              .andExpect(jsonPath("$.[1].description").value("for IT tests"))
              .andExpect(jsonPath("$.[1].category").value("test"))
              .andExpect(jsonPath("$.[1].transactionDate").value("2023-01-12T17:30"));
   }


   @AfterEach
   void tearDown() {
      noteService.deleteAll();
      userService.deleteAll();
   }

   private User getMockUser(){
      return new User(1, "Doe", "1", "doe@mail.com", RoleEnum.ROLE_USER);
   }

   private List<Note> getMockNotes(){
      return List.of(new Note(1, BigDecimal.valueOf(123), "test", "for IT tests", LocalDateTime.of(2023,2,12,16,30)),
              new Note(2, BigDecimal.valueOf(321), "test", "for IT tests", LocalDateTime.of(2023,1,12,17,30)),
              new Note(3, BigDecimal.valueOf(400), "testing", "for tests", LocalDateTime.of(2022,12,12,20,30)));
   }

}