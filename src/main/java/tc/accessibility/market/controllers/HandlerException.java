package tc.accessibility.market.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import tc.accessibility.market.DTOs.SimpleMessage;
import tc.accessibility.market.controllers.errors.CloudVisionApiExpection;

import java.io.IOException;

/*
* Controller responsável pelo tratamento de Erros do projeto
*/
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<SimpleMessage> handlerMethodNotAllowedException(){
        return new ResponseEntity<>(
                new SimpleMessage("Esse metodo não está disponivel nessa rota "),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<SimpleMessage> handlerMultipartException(){
        return new ResponseEntity<>(
                new SimpleMessage("Não foi possivel realizar o request, existem coisas faltando!"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<SimpleMessage> handlerIOException(Exception e){
        return new ResponseEntity<>(
                new SimpleMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CloudVisionApiExpection.class)
    public ResponseEntity<SimpleMessage> handlerCloudVisionApiExpection(Exception e){
        return new ResponseEntity<>(
                new SimpleMessage(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
