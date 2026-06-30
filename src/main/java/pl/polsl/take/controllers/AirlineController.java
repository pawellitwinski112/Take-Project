package pl.polsl.take.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.dto.AirlineDTO;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.services.AirlineService;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public ResponseEntity<AirlineDTO> addAirline(@RequestBody Airline airline) {
        // Delegujemy do serwisu; zwracamy 201 Created z nowym zasobem w ciele
        AirlineDTO created = airlineService.create(airline);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
    @GetMapping
    public CollectionModel<AirlineDTO> getAllAirlines() {
        List<AirlineDTO> airlines = airlineService.getAll();
        // Link HATEOAS do samej kolekcji (self)
        return CollectionModel.of(airlines, linkTo(methodOn(AirlineController.class).getAllAirlines()).withSelfRel());
    }

    @GetMapping("/{id}")
    public AirlineDTO getAirlineById(@PathVariable Long id) {
        return airlineService.getById(id);
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public AirlineDTO updateAirline(@RequestBody Airline airline) {
        return airlineService.update(airline);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.delete(id);
        // jeśli program jest tutaj -> 204 No Content
        return ResponseEntity.noContent().build();
    }
}
