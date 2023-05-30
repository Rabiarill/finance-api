package ru.rabiarill.controllers.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.enumeration.RoleEnum;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.UserService;
import ru.rabiarill.util.security.JwtUtil;
import ru.rabiarill.util.security.UserUtil;
import ru.rabiarill.util.validators.UserValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

   @Mock
   UserService userService;
   @Mock
   AuthenticationManager authenticationManager;
   @Mock
   UserUtil userUtil;
   @Mock
   UserValidator userValidator;
   @Mock
   JwtUtil jwtUtil;
   @Mock
   BindingResult mockBindingResult;

   @InjectMocks
   UserController userController;

   @BeforeEach
   void setUp() {
      lenient().when(userUtil.getUserFromContextHolder()).thenReturn(getMockUser());
   }

   @Test
   void userInfo_ValidUser_ReturnValidResponse(){
      // given
      User user = getMockUser();

      // when
      var response = this.userController.userInfo(user);

      // then
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      assertEquals(user, response.getBody().convertToUser());

   }

   @Test
   void update_ValidUser_ReturnValidResponseAndSaveUser() {
      // given
      User user = getMockUser();

      // when
      var response = this.userController.update(user.convertToUserDTO(), mockBindingResult, user);

      // then
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody());
      verify(userService, only()).save(user);

   }

   @Test
   void update_NotValidUser_ThrowNotValidUserException() {
      when(mockBindingResult.hasErrors()).thenReturn(true);

      assertThrows(NotValidUserException.class,
              () -> this.userController.update(new UserDTO(), mockBindingResult, getMockUser()));
      verifyNoInteractions(userService);

   }

   @Test
   void deleteUser_ValidUser_ReturnValidResponseAndDeleteUser() {
      // given
      UserDTO userDTO = getMockUser().convertToUserDTO();

      // when
      var response = this.userController.deleteUser(userDTO, mockBindingResult);

      // then
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNull(response.getBody());
      verify(userService, only()).delete(userDTO.getId());

   }

   @Test
   void deleteUser_NotValidUser_ThrowNotValidUserException() {
      when(mockBindingResult.hasErrors()).thenReturn(true);

      assertThrows(NotValidUserException.class,
              () -> this.userController.deleteUser(new UserDTO(), mockBindingResult));
      verifyNoInteractions(userService);
      verifyNoInteractions(authenticationManager);

   }

   @Test
   void deleteUser_WithInvalidCredentials_ThrowBadCredentialsException() {
      UserDTO userDTO = getMockUser().convertToUserDTO();
      when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

      assertThrows(BadCredentialsException.class,
              () -> this.userController.deleteUser(userDTO, mockBindingResult));
      verifyNoInteractions(userService);

   }

   private User getMockUser(){
      return new User(1, "Doe", "1", "doe@mail.com", RoleEnum.ROLE_USER );
   }


}