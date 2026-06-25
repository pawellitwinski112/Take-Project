package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.AirlineDTO;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.exceptions.ResourceNotFoundException;
import pl.polsl.take.repositories.AirlineRepository;
import pl.polsl.take.repositories.FlightRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Cała logika biznesowa, która wcześniej znajdowała się w kontrolerze,
 * zostaje przeniesiona, a kontroler staje się czystą warstwą HTTP.
 */
@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;

    public AirlineService(AirlineRepository airlineRepository, FlightRepository flightRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
    }

    // ==========================================
    // C - CREATE
    // ==========================================

    public AirlineDTO create(Airline airline) {
        // Reguła biznesowa: klient nie może narzucać ID przy tworzeniu zasobu
        if (airline.getId() != null && airline.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania linii lotniczej nie podawaj ID.");
        }
        airline.setId(null);
        Airline saved = airlineRepository.save(airline);
        return new AirlineDTO(saved);
    }

    // ==========================================
    // R - READ
    // ==========================================

    public List<AirlineDTO> getAll() {
        return StreamSupport
                .stream(airlineRepository.findAll().spliterator(), false)
                .map(AirlineDTO::new)
                .collect(Collectors.toList());
    }

    public AirlineDTO getById(Long id) {
        return airlineRepository.findById(id)
                .map(AirlineDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID " + id));
    }

    // ==========================================
    // U - UPDATE
    // ==========================================

    public AirlineDTO update(Airline airline) {
        // Reguła biznesowa: aktualizacja wymaga podania ID
        if (airline.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować linię lotniczą, musisz podać jej ID.");
        }
        // Reguła biznesowa: zasób o tym ID musi istnieć
        if (!airlineRepository.existsById(airline.getId())) {
            throw new ResourceNotFoundException("Błąd: Linia lotnicza o podanym ID nie istnieje.");
        }
        Airline updated = airlineRepository.save(airline);
        return new AirlineDTO(updated);
    }

    // ==========================================
    // D - DELETE
    // ==========================================

    public void delete(Long id) {
        // Reguła biznesowa #1: zasób musi istnieć
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID " + id));

        // Reguła biznesowa #2: nie można usunąć linii obsługującej aktywne loty
        if (flightRepository.existsByAirlineId(id)) {
            throw new IllegalStateException(
                    "Konflikt: Nie można usunąć linii lotniczej, ponieważ obsługuje ona zaplanowane loty.");
        }

        airlineRepository.delete(airline);
    }
}
