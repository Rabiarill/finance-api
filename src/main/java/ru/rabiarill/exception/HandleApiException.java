package ru.rabiarill.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rabiarill.dto.response.ErrResponse;

@ControllerAdvice
public class HandleApiException {

   @ExceptionHandler(value = {BadCredentialsException.class})
   public ResponseEntity<ErrResponse> handle(BadCredentialsException e){

      ErrResponse errDTO = new ErrResponse(e);

      return new ResponseEntity<>(errDTO, HttpStatus.BAD_REQUEST);
   }

}
