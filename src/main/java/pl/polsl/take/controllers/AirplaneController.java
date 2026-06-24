package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.repositories.AirplaneRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.dto.AirplaneDTO;
import org.springframework.hateoas.CollectionModel;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneRepository airplaneRepository;
    private final FlightRepository flightRepository;

    public AirplaneController(AirplaneRepository airplaneRepository, FlightRepository flightRepository) {
        this.airplaneRepository = airplaneRepository;
        this.flightRepository = flightRepository;
    }

    @PostMapping
    public Airplane addAirplane(@RequestBody Airplane airplane) {
        if (airplane.getId() != null && airplane.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania samolotu nie podawaj ID.");
        }
        return airplaneRepository.save(airplane);
    }

   // ==========================================
    // R - READ (GET) z HATEOAS
    // ==========================================
    @GetMapping
    public CollectionModel<AirplaneDTO> getAllAirplanes() {
        java.util.List<AirplaneDTO> airplanes = StreamSupport
                .stream(airplaneRepository.findAll().spliterator(), false)
                .map(AirplaneDTO::new)
                .collect(Collectors.toList());
        return CollectionModel.of(airplanes);
    }

    @GetMapping("/{id}")
    public AirplaneDTO getAirplaneById(@PathVariable Long id) {
        return airplaneRepository.findById(id)
                .map(AirplaneDTO::new)
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
    public ResponseEntity<Void> deleteAirplane(@PathVariable Long id) {
        if (!airplaneRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Nie znaleziono samolotu o ID " + id);
        }
        if (flightRepository.existsByAirplaneId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć samolotu, ponieważ bierze on udział w zaplanowanych lotach.");
        }
        airplaneRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}