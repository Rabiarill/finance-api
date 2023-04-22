package ru.rabiarill.util.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rabiarill.models.user.User;
import ru.rabiarill.services.UserService;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserService userService;

   @Autowired
   public UserDetailsServiceImpl(UserService userService) {
      this.userService = userService;
   }

   @Override
   public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
      Optional<User> user = userService.findByUsername(s);

      if (user.isEmpty())
         throw new UsernameNotFoundException("User not found");

      return new UserDetailsImpl(user.get());
   }

}
