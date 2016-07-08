/*package com.example.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;  

@ControllerAdvice  
@RestController  
public class GlobalExceptionHandler {  
  
    @ResponseStatus(HttpStatus.BAD_REQUEST)  
    @ExceptionHandler(value = BaseException.class)  
    public String handleBaseException(BaseException e){  
        return e.getMessage();  
   }  
  
    @ExceptionHandler(value = Exception.class)  
    public String handleException(Exception e){return e.getMessage();}  
  
  
}  
*/