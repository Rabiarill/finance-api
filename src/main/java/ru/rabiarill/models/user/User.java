package ru.rabiarill.models.user;

import org.modelmapper.ModelMapper;
import ru.rabiarill.dto.model.user.UserDTO;
import ru.rabiarill.enumeration.RoleEnum;
import ru.rabiarill.models.note.Note;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private int id;

   @Column(name = "username")
   private String username;

   @Column(name = "password")
   private String password;

   @Column(name = "email")
   private String email;

   @Column(name = "role")
   @Enumerated(EnumType.STRING)
   private RoleEnum role;

   @OneToMany(mappedBy = "owner")
   private List<Note> notes;


   public User() {
   }

   public User(String username, String password, String email, RoleEnum role) {
      this.username = username;
      this.password = password;
      this.email = email;
      this.role = role;
   }

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

   public RoleEnum getRole() {
      return role;
   }

   public void setRole(RoleEnum role) {
      this.role = role;
   }

   public List<Note> getNotes() {
      return notes;
   }

   public void setNotes(List<Note> notes) {
      this.notes = notes;
   }

   public UserDTO convertToUserDTO() {
      return new ModelMapper().map(this, UserDTO.class);
   }

   public boolean idAdmin() {
      return this.role.equals(RoleEnum.ROLE_ADMIN);
   }
}
