package ru.rabiarill.controllers.v1.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.exception.model.user.NotValidUserException;
import ru.rabiarill.exception.model.user.UserNotFoundException;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.UserService;
import ru.rabiarill.util.validators.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/finance-api/v1/admin/user")
public class AdminUserController {

   private final UserService userService;
   private final UserValidator userValidator;

   @Autowired
   public AdminUserController(UserService userService, UserValidator userValidator) {
      this.userService = userService;
      this.userValidator = userValidator;
   }

   @GetMapping()
   public ResponseEntity<List<UserDTO>> findAllUsers() {
      List<UserDTO> userDTO = userService.findAll().stream()
              .map(User::convertToUserDTO)
              .collect(Collectors.toList());
      return new ResponseEntity<>(userDTO, HttpStatus.OK);
   }

   @GetMapping("/{id}")
   public ResponseEntity<UserDTO> findOneUser(@PathVariable("id") int id) {
      return new ResponseEntity<>(userService.findOne(id).convertToUserDTO(), HttpStatus.OK);
   }


   @PutMapping()
   public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDTO userDTO,
                                                BindingResult bindingResult) {

      User userToUpdate = userDTO.convertToUser();
      if (userToUpdate.getId() == 0)
         throw new UserNotFoundException("The user id to update must not be null");

      userValidator.validate(userToUpdate, bindingResult);

      if (bindingResult.hasErrors())
         throw new NotValidUserException(bindingResult.getFieldErrors());

      userService.save(userToUpdate);

      return new ResponseEntity<>(HttpStatus.OK);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
      userService.delete(id);
      return new ResponseEntity<>(HttpStatus.OK);
   }

}
