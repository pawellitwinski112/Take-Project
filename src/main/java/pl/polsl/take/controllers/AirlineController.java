package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.repositories.AirlineRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.dto.AirlineDTO;
import org.springframework.hateoas.CollectionModel;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import pl.polsl.take.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/airlines")
public class AirlineController {

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;

    public AirlineController(AirlineRepository airlineRepository, FlightRepository flightRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public Airline addAirline(@RequestBody Airline airline) {
        if (airline.getId() != null && airline.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania linii lotniczej nie podawaj ID.");
        }
        return airlineRepository.save(airline);
    }

    // ==========================================
    // R - READ (GET) z HATEOAS
    // ==========================================
    @GetMapping
    public CollectionModel<AirlineDTO> getAllAirlines() {
        java.util.List<AirlineDTO> airlines = StreamSupport
                .stream(airlineRepository.findAll().spliterator(), false)
                .map(AirlineDTO::new)
                .collect(Collectors.toList());
        return CollectionModel.of(airlines, 
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(AirlineController.class).getAllAirlines()).withSelfRel()
                );
    }

    @GetMapping("/{id}")
    public AirlineDTO getAirlineById(@PathVariable Long id) {
        return airlineRepository.findById(id)
                .map(AirlineDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID " + id));
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
            throw new ResourceNotFoundException("Błąd: Linia lotnicza o podanym ID nie istnieje.");
        }
        return airlineRepository.save(airline);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID " + id));

        if (flightRepository.existsByAirlineId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć linii lotniczej, ponieważ obsługuje ona zaplanowane loty.");
        }

        airlineRepository.delete(airline);
        return ResponseEntity.noContent().build();
    }
}	