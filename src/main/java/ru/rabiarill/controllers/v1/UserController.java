package ru.rabiarill.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.exception.model.NoAccessException;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.UserService;
import ru.rabiarill.util.security.UserUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/finance-api/v1/user")
public class UserController {

   private final UserService userService;
   private final AuthenticationManager authenticationManager;
   private final UserUtil userUtil;

   @Autowired
   public UserController(UserService userService, AuthenticationManager authenticationManager, UserUtil userUtil) {
      this.userService = userService;
      this.authenticationManager = authenticationManager;
      this.userUtil = userUtil;
   }

   @GetMapping()
   public ResponseEntity<UserDTO> userInfo(){
      UserDTO userDTO = userUtil.getUserFromContextHolder().convertToUserDTO();

      return new ResponseEntity<>(userDTO, HttpStatus.OK);
   }

   @PutMapping()
   public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO userDTO,
                                            BindingResult bindingResult) throws NoAccessException {
      if (bindingResult.hasErrors())
         throw new NotValidUserException();

      User sender = userUtil.getUserFromContextHolder();
      if (sender.getId() != userDTO.getId())
         throw new NoAccessException("You have no access to update this user");

      User userToUpdate = userDTO.convertToUser();
      userService.save(userToUpdate);

      return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping()
   public ResponseEntity<HttpStatus> deleteUser(@RequestBody @Valid UserDTO userDTO,
                                                BindingResult bindingResult){
      if (bindingResult.hasErrors())
         throw new NotValidUserException();

      UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
      try {
         authenticationManager.authenticate(authToken);
      }catch (BadCredentialsException e){
         throw new BadCredentialsException("Invalid Credentials");
      }
      userService.delete(userDTO.getId());
      return new ResponseEntity<>(HttpStatus.OK);
   }

}
