package ru.rabiarill.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rabiarill.util.security.UserDetailsServiceImpl;
import ru.rabiarill.util.security.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

   private static final String AUTH_HEADER = "Authorization";
   private static final String TOKEN_PREFIX = "Bearer ";

   private final JwtUtil jwtUtil;
   private final UserDetailsServiceImpl userDetailsService;

   @Autowired
   public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
      this.jwtUtil = jwtUtil;
      this.userDetailsService = userDetailsService;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse,
                                   FilterChain filterChain) throws ServletException, IOException {

      String token = httpServletRequest.getHeader(AUTH_HEADER);

      if (token != null && token.startsWith(TOKEN_PREFIX)) {
         token = token.substring(7);

         String username = jwtUtil.getUsername(token);

         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
         }
      }

      filterChain.doFilter(httpServletRequest, httpServletResponse);
   }

}
