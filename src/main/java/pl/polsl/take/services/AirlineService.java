package pl.polsl.take.services;

import org.springframework.hateoas.CollectionModel;
import pl.polsl.take.dto.AirlineDTO;
import pl.polsl.take.entities.Airline;

/**
 * Kontrakt warstwy serwisowej dla linii lotniczych.
 *
 * Warstwa serwisowa odpowiada za:
 * - całą logikę biznesową (walidacja, reguły konfliktów)
 * - mapowanie encji na DTO
 * - wywoływanie repozytoriów
 *
 * Kontroler NIE powinien znać repozytoriów ani zawierać logiki biznesowej —
 * jego jedynym zadaniem jest obsługa HTTP (routing, kody odpowiedzi).
 */
public interface AirlineService {

    /**
     * Zwraca wszystkie linie lotnicze jako kolekcję DTO z linkami HATEOAS.
     */
    CollectionModel<AirlineDTO> getAll();

    /**
     * Zwraca pojedynczą linię lotniczą po ID.
     * Rzuca ResourceNotFoundException jeśli nie istnieje (→ HTTP 404).
     */
    AirlineDTO getById(Long id);

    /**
     * Tworzy nową linię lotniczą.
     * Rzuca IllegalArgumentException jeśli ciało żądania zawiera ID (→ HTTP 400).
     * Zwraca nowo utworzony zasób jako DTO.
     */
    AirlineDTO create(Airline airline);

    /**
     * Aktualizuje istniejącą linię lotniczą.
     * Rzuca IllegalArgumentException jeśli brak ID w ciele żądania (→ HTTP 400).
     * Rzuca ResourceNotFoundException jeśli linia o podanym ID nie istnieje (→ HTTP 404).
     * Zwraca zaktualizowany zasób jako DTO.
     */
    AirlineDTO update(Airline airline);

    /**
     * Usuwa linię lotniczą po ID.
     * Rzuca ResourceNotFoundException jeśli nie istnieje (→ HTTP 404).
     * Rzuca IllegalStateException jeśli linia obsługuje aktywne loty (→ HTTP 409).
     */
    void delete(Long id);
}
