package ru.rabiarill.dto.model.user;

import org.modelmapper.ModelMapper;
import ru.rabiarill.models.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {

   private int id;

   @NotEmpty(message = "Username should not be empty")
   @Size(min = 4, max = 100, message = "Username size should be between 4 and 100")
   private String username;

   @Size(min = 4, message = "Password size should be more than 4")
   private String password;

   @Email(message = "Email should be valid")
   private String email;

   @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Expected [ROLE_USER] or [ROLE_ADMIN]")
   private String role;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public User convertToUser(){
      return new ModelMapper().map(this, User.class);
   }
}
