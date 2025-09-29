package com.ndh.ShopTechnology.exception;

import com.ndh.ShopTechnology.constant.MessageConstant;
import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundEntityException.class)
    public ResponseEntity<APIResponse> handleNotFound(NotFoundEntityException ex) {
        log.error("NotFoundEntityException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, HttpStatus.NOT_FOUND.value(),
                        DefRes.RES_ERS, ex.getMessage()
                ));
    }

    @ExceptionHandler(value = AuthenticationFailedException.class)
    public ResponseEntity<APIResponse> handleAuthFailed(AuthenticationFailedException ex) {
        log.error("AuthenticationFailedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, HttpStatus.UNAUTHORIZED.value(),
                        DefRes.RES_DES,ex.getMessage()
                ));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, HttpStatus.BAD_REQUEST.value(),
                        DefRes.RES_DES, MessageConstant.VALIDATION_FAILED,
                        DefRes.RES_ERS, errors
                ));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("AccessDeniedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, HttpStatus.FORBIDDEN.value(),
                        DefRes.RES_DES, MessageConstant.ACCESS_DENIED
                ));
    }

    @ExceptionHandler(value = CustomApiException.class)
    public ResponseEntity<APIResponse> handleCustomApiException(CustomApiException ex) {
        log.error("CustomApiException: status={}, message={}", ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, ex.getStatus().value(),
                        DefRes.RES_DES, ex.getMessage()
                ));
    }
}
