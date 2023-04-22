package ru.rabiarill.dto.security;

public class JwtTokenDTO {

   private String token;

   public JwtTokenDTO() {
   }

   public JwtTokenDTO(String token) {
      this.token = token;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }
}
