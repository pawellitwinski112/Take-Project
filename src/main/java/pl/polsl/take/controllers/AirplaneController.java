package pl.polsl.take.controllers;

import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.repositories.AirplaneRepository;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneRepository airplaneRepository;

    public AirplaneController(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    @PostMapping
    public Airplane addAirplane(@RequestBody Airplane airplane) {
        if (airplane.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania samolotu nie podawaj ID.");
        }
        return airplaneRepository.save(airplane);
    }

    @GetMapping
    public Iterable<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    @GetMapping("/{id}")
    public Airplane getAirplaneById(@PathVariable Long id) {
        return airplaneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono samolotu o ID " + id));
    }

    @PutMapping
    public Airplane updateAirplane(@RequestBody Airplane airplane) {
        if (airplane.getId() == null || !airplaneRepository.existsById(airplane.getId())) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować samolot, podaj poprawne ID.");
        }
        return airplaneRepository.save(airplane);
    }

    @DeleteMapping("/{id}")
    public void deleteAirplane(@PathVariable Long id) {
        if (!airplaneRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Samolot o podanym ID nie istnieje.");
        }
        // BŁYSKAWICZNE SPRAWDZENIE W BAZIE (Bez pobierania list do pamięci RAM)
        if (flightRepository.existsByAirplaneId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć samolotu, ponieważ bierze on udział w zaplanowanych lotach.");
        }
        airplaneRepository.deleteById(id);
    }
}