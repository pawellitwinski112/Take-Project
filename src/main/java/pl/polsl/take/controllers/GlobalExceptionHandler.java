package pl.polsl.take.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.OptimisticLockingFailureException;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = { "pl.polsl.take.controllers" })
public class GlobalExceptionHandler {

    // ==========================================
    // 404 - Not Found
    // Gdy szukamy zasobu po ID i nie istnieje
    // ==========================================
   @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ==========================================
    // 400 - Bad Request
    // Gdy klient wysyła niepoprawne dane (np. podaje ID w POST)
    // ==========================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ==========================================
    // 409 - Conflict
    // Gdy usunięcie zasobu jest zablokowane przez powiązane rekordy
    // ==========================================
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(IllegalStateException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ==========================================
    // 400 - Niepoprawny format JSON w ciele żądania
    // Np. brakujące nawiasy, złe typy danych
    // ==========================================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Błąd: Niepoprawny format JSON w ciele żądania. Sprawdź składnię i typy danych.");
    }

    // ==========================================
    // 400 - Niepoprawny typ parametru w URL
    // Np. /flights/abc zamiast /flights/1
    // ==========================================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Błąd: Parametr '%s' ma niepoprawny typ. Oczekiwano: %s.",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "właściwy typ"
        );
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // ==========================================
    // 500 - Internal Server Error (catch-all)
    // Dla wszystkich nieoczekiwanych błędów
    // ==========================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Błąd krytyczny.");
    }

    // ==========================================
    // Pomocnicza metoda budująca ujednolicone ciało odpowiedzi błędu
    // ==========================================
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    // ==========================================
    // 409 - Konflikt (Wyścig współbieżny / Optimistic Locking)
    // Gdy dwa wątki próbują zmodyfikować/usunąć ten sam zasób
    // ==========================================
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleConcurrentModification(OptimisticLockingFailureException ex) {
        return buildResponse(HttpStatus.CONFLICT, 
                "Konflikt: Ktoś inny zmodyfikował lub usunął ten zasób w tym samym czasie. Odśwież dane i spróbuj ponownie.");
    }
}
