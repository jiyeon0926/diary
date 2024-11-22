package diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(ex.getStatusCode().value()));  // HTTP 상태 코드
        errorResponse.put("error", ex.getReason());  // 지정한 에러 메시지
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("유효성 검사 오류\n");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append("에 대한 오류\n").append(error.getDefaultMessage()).append("\n ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;

    }

    // NoSuchElementException 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("error: " + ex.getMessage());
    }

}