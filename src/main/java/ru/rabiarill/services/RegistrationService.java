package ru.rabiarill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rabiarill.enumeration.RoleEnum;
import ru.rabiarill.models.user.User;

@Service
public class RegistrationService {

   private final UserService userService;

   private final PasswordEncoder passwordEncoder;

   @Autowired
   public RegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
      this.userService = userService;
      this.passwordEncoder = passwordEncoder;
   }

   @Transactional
   public void register(User user){
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(RoleEnum.ROLE_USER);
      userService.save(user);
   }

}
