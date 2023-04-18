package ru.rabiarill.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.util.security.UserDetailsImpl;
import ru.rabiarill.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/finance-api/v1/user")
public class UserController {

   private final UserService userService;
   private final AuthenticationManager authenticationManager;

   @Autowired
   public UserController(UserService userService, AuthenticationManager authenticationManager) {
      this.userService = userService;
      this.authenticationManager = authenticationManager;
   }

   @GetMapping()
   public ResponseEntity<UserDTO> userInfo(){
      UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      UserDTO userDTO = principal.getUser().convertToUserDTO();

      return new ResponseEntity<>(userDTO, HttpStatus.OK);
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
