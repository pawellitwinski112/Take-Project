package pl.polsl.take.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.entities.PassengerManifestDto;
import pl.polsl.take.repositories.PassengerRepository;
import pl.polsl.take.repositories.BoardingPassRepository;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerRepository passengerRepository;
    private final BoardingPassRepository boardingPassRepository;

    // Wstrzykiwanie zależności przez konstruktor (zastępuje @Autowired z wykładu)
    public PassengerController(PassengerRepository passengerRepository, BoardingPassRepository boardingPassRepository) {
        this.passengerRepository = passengerRepository;
        this.boardingPassRepository = boardingPassRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public Passenger addPassenger(@RequestBody Passenger passenger) {
        if (passenger.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania pasażera nie podawaj ID.");
        }
        return passengerRepository.save(passenger);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
    // 1. Pobieranie listy wszystkich pasażerów
    @GetMapping
    public Iterable<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    // 2. Pobieranie konkretnego pasażera po ID (np. /passengers/1)
    @GetMapping("/{id}")
    public Passenger getPassengerById(@PathVariable Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono pasażera o ID " + id));
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public Passenger updatePassenger(@RequestBody Passenger passenger) {
        if (passenger.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować pasażera, musisz podać jego ID.");
        }
        if (!passengerRepository.existsById(passenger.getId())) {
            throw new RuntimeException("Błąd: Pasażer o podanym ID nie istnieje.");
        }
        return passengerRepository.save(passenger);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        // Najpierw pobieramy pasażera z bazy
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono pasażera o ID " + id));

        // Zabezpieczenie biznesowe: Sprawdzamy, czy pasażer posiada karty pokładowe
        if (boardingPassRepository.existsByPassengerId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć pasażera, ponieważ posiada przypisane karty pokładowe.");
        }

        // Jeśli jest czysto - usuwamy
        passengerRepository.delete(passenger);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/manifest/{flightId}")
    public List<PassengerManifestDto> getPassengerManifest(@PathVariable Long flightId){
    	return passengerRepository.findPassengerManifestByFlightId(flightId);
    }
}