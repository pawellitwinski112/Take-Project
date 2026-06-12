package pl.polsl.take.controllers;

import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.repositories.AirlineRepository;

@RestController
@RequestMapping("/airlines")
public class AirlineController {

    private final AirlineRepository airlineRepository;

    // Wstrzykiwanie zależności przez konstruktor (zastępuje @Autowired z wykładu)
    public AirlineController(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public Airline addAirline(@RequestBody Airline airline) {
        if (airline.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania linii lotniczej nie podawaj ID.");
        }
        return airlineRepository.save(airline);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
    // 1. Pobieranie listy wszystkich linii lotniczych
    @GetMapping
    public Iterable<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    // 2. Pobieranie konkretnego lotniska po ID (np. /airports/1)
    @GetMapping("/{id}")
    public Airline getAirlineById(@PathVariable Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono linii lotniczej o ID " + id));
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public Airline updateAirline(@RequestBody Airline airline) {
        if (airline.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować linię lotniczą, musisz podać jej ID.");
        }
        if (!airlineRepository.existsById(airline.getId())) {
            throw new RuntimeException("Błąd: Linia lotnicza o podanym ID nie istnieje.");
        }
        return airlineRepository.save(airline);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public void deleteAirline(@PathVariable Long id) {
        // Najpierw pobieramy linię lotniczą z bazy
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono linii lotniczej o ID " + id));

       

        // Jeśli jest czysto - usuwamy
        airlineRepository.delete(airline);
    }
}