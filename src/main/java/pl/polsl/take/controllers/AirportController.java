package pl.polsl.take.controllers;

import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.repositories.AirportRepository;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final AirportRepository airportRepository;

    // Wstrzykiwanie zależności przez konstruktor (zastępuje @Autowired z wykładu)
    public AirportController(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public Airport addAirport(@RequestBody Airport airport) {
        if (airport.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania lotniska nie podawaj ID.");
        }
        return airportRepository.save(airport);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
    // 1. Pobieranie listy wszystkich lotnisk
    @GetMapping
    public Iterable<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    // 2. Pobieranie konkretnego lotniska po ID (np. /airports/1)
    @GetMapping("/{id}")
    public Airport getAirportById(@PathVariable Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotniska o ID " + id));
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public Airport updateAirport(@RequestBody Airport airport) {
        if (airport.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować lotnisko, musisz podać jego ID.");
        }
        if (!airportRepository.existsById(airport.getId())) {
            throw new RuntimeException("Błąd: Lotnisko o podanym ID nie istnieje.");
        }
        return airportRepository.save(airport);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable Long id) {
        // Najpierw pobieramy lotnisko z bazy
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotniska o ID " + id));

        // Zabezpieczenie biznesowe: Sprawdzamy, czy są przypisane loty
        boolean hasDepartures = airport.getDepartingFlights() != null && !airport.getDepartingFlights().isEmpty();
        boolean hasArrivals = airport.getArrivingFlights() != null && !airport.getArrivingFlights().isEmpty();

        if (hasDepartures || hasArrivals) {
            throw new IllegalStateException("Konflikt: Nie można usunąć lotniska, ponieważ posiada zaplanowane loty.");
        }

        // Jeśli jest czysto - usuwamy
        airportRepository.delete(airport);
    }
}