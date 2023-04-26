package ru.rabiarill.controllers.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import ru.rabiarill.dto.model.note.NoteDTO;
import ru.rabiarill.enumeration.RoleEnum;
import ru.rabiarill.exception.model.NoAccessException;
import ru.rabiarill.exception.model.note.NotValidNoteException;
import ru.rabiarill.models.note.Note;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.NoteService;
import ru.rabiarill.util.security.UserUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

   @Mock
   NoteService noteService;
   @Mock
   UserUtil userUtil;
   @Mock
   BindingResult mockBindingResult;
   
   @InjectMocks
   NoteController noteController;

   @BeforeEach
   void setUp() {
      lenient().when(userUtil.getUserFromContextHolder()).thenReturn(getMockUser());
   }

   @Test
   void findAll_ReturnValidResponseEntity() {
      // given
      when(noteService.findByOwnerId(any(Integer.class))).thenReturn(getMockNoteList());

      //when
      var responseEntity = this.noteController.findAll();

      // then
      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      List<Note> expectedNotes = Objects.requireNonNull(responseEntity.getBody())
              .stream().map(NoteDTO::convertToNote).collect(Collectors.toList());
      assertEquals(getMockNoteList(), expectedNotes);

   }

   @Test
   void create_PayloadIsValid_ReturnValidResponseEntity() {
      // given
      NoteDTO note = getMockNoteList().get(0).convertToDTO();

      // when
      var responseEntity = this.noteController.create(note, mockBindingResult);

      // then
      assertNotNull(responseEntity);
      assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
      assertNull(responseEntity.getBody());
      verify(noteService, only()).save(any(Note.class));
   }

   @Test
   void create_PayloadIsNotValid_ThrowNotValidNoteException() {

      when(mockBindingResult.hasErrors()).thenReturn(true);

      assertThrows(NotValidNoteException.class, () -> this.noteController.create(new NoteDTO(), mockBindingResult));
      verifyNoMoreInteractions(noteService);
   }

   @Test
   void update_PayloadIsValid_ReturnValidResponseEntity(){
      // given
      int id = 0;
      Note note = getMockNoteList().get(0);
      when(noteService.findOne(id)).thenReturn(note);
      when(userUtil.hasAccess(any(User.class), any(Note.class))).thenReturn(true);

      // when
      var responseEntity = this.noteController.update(note.convertToDTO(), mockBindingResult, id);

      // then
      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNull(responseEntity.getBody());
      verify(noteService).update(any(Note.class), eq(id));
   }

   @Test
   void update_PayloadIsNotValid_ThrowNotValidNoteException(){
      when(mockBindingResult.hasErrors()).thenReturn(true);

      assertThrows(NotValidNoteException.class,
              () -> this.noteController.update(mock(NoteDTO.class), mockBindingResult, 0));
      verifyNoMoreInteractions(noteService);
   }

   @Test
   void update_SenderHasNoAccessToNote_ThrowNoAccessException(){
      int id = 0;
      Note note = getMockNoteList().get(0);
      when(noteService.findOne(id)).thenReturn(mock(Note.class));
      when(userUtil.hasAccess(any(User.class), any(Note.class))).thenReturn(false);

      assertThrows(NoAccessException.class,
              () -> this.noteController.update(note.convertToDTO(), mockBindingResult, id));
      verifyNoMoreInteractions(noteService);
   }

   @Test
   void deleteById_SenderHasAccessToNote_ReturnValidResponse() {
      // given
      when(noteService.findOne(any(Integer.class))).thenReturn(mock(Note.class));
      when(userUtil.hasAccess(any(User.class), any(Note.class))).thenReturn(true);

      // when
      var responseEntity = this.noteController.deleteById(0);

      // then
      assertNotNull(responseEntity);
      assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
      assertNull(responseEntity.getBody());
   }

   @Test
   void deleteById_SenderHasNoAccessToNote_ThrowNoAccessException(){

      assertThrows(NoAccessException.class,
              () -> this.noteController.deleteById(0));
      verify(noteService, never()).delete(any(Integer.class));

   }

   private User getMockUser(){
      return new User("Doe", "1", "doe@mail.com", RoleEnum.ROLE_USER );
   }

   private List<Note> getMockNoteList(){
      return List.of(new Note(BigDecimal.valueOf(423), "testCategory", "", LocalDateTime.of(2020,11,5,16,30)),
              new Note(BigDecimal.valueOf(423), "testCategory", "", LocalDateTime.of(2020,12,20,16,35)));
   }
}