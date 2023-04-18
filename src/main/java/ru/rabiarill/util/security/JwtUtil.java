package ru.rabiarill.util.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

   @Value("${jwt.secret}")
   private String secret;

   public String generateToken(String username){
      return JWT
              .create()
              .withSubject("User details")
              .withClaim("username", username)
              .withIssuedAt(new Date())
              .withExpiresAt(generateExpirationDate())
              .withIssuer("finance-java-api")
              .sign(Algorithm.HMAC256(secret));

   }

   public String getUsername(String token){
      return getVerifier().verify(token)
              .getClaim("username").asString();
   }

   private JWTVerifier getVerifier(){
      return JWT
              .require(Algorithm.HMAC256(secret))
              .withSubject("User details")
              .withIssuer("finance-java-api")
              .build();

   }

   public boolean isValid(String token){
      try {
         getVerifier().verify(token);
         return true;
      }catch (Exception e){
         return false;
      }
   }

   private Date generateExpirationDate(){
      return Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
   }
}
