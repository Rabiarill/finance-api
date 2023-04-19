package ru.rabiarill.util.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.rabiarill.models.user.User;

@Component
public class UserUtil {
   public User getUserFromContextHolder(){
      UserDetailsImpl authentication = (UserDetailsImpl) SecurityContextHolder
              .getContext().getAuthentication().getPrincipal();

      return authentication.getUser();
   }
}
