package ru.rabiarill.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.dto.security.JwtTokenDTO;
import ru.rabiarill.dto.security.JwtUserDTO;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.RegistrationService;
import ru.rabiarill.util.security.JwtUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/finance-api/v1/auth")
public class AuthController {

   private final RegistrationService registrationService;
   private final JwtUtil jwtUtil;
   private final AuthenticationManager authenticationManager;

   @Autowired
   public AuthController(RegistrationService registrationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
      this.registrationService = registrationService;
      this.jwtUtil = jwtUtil;
      this.authenticationManager = authenticationManager;
   }

   @PostMapping("/login")
   public ResponseEntity<JwtTokenDTO> login(@RequestBody @Valid JwtUserDTO userDTO,
                                            BindingResult bindingResult) throws NotValidUserException {
      if (bindingResult.hasErrors())
         throw new NotValidUserException();

      UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
      try {
         authenticationManager.authenticate(authToken);
      } catch (BadCredentialsException e) {
         throw new BadCredentialsException("Invalid Credentials");
      }

      JwtTokenDTO response = new JwtTokenDTO(jwtUtil.generateToken(userDTO.getUsername()));

      return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @PostMapping
   public ResponseEntity<JwtTokenDTO> generateJwtToken(@RequestBody @Valid UserDTO userDTO,
                                                       BindingResult bindingResult) {
      if (bindingResult.hasErrors())
         throw new NotValidUserException();

      User user = userDTO.convertToUser();
      registrationService.register(user);

      JwtTokenDTO response = new JwtTokenDTO(jwtUtil.generateToken(user.getUsername()));

      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

}
