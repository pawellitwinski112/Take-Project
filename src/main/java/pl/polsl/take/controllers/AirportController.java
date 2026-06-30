package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.dto.AirportDTO;
import pl.polsl.take.services.AirportService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping
    public Airport addAirport(@RequestBody Airport airport) {
        return airportService.create(airport);
    }

    @GetMapping
    public CollectionModel<AirportDTO> getAllAirports() {
        List<AirportDTO> airports = airportService.getAll();
        return CollectionModel.of(airports, 
                linkTo(methodOn(AirportController.class).getAllAirports()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public AirportDTO getAirportById(@PathVariable Long id) {
        return airportService.getById(id);
    }

    @PutMapping
    public Airport updateAirport(@RequestBody Airport airport) {
        return airportService.update(airport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/departures")
    public CollectionModel<FlightDTO> getDepartingFlights(@PathVariable Long id) {
        List<FlightDTO> flights = airportService.getDepartingFlights(id);
        return CollectionModel.of(flights, 
                linkTo(methodOn(AirportController.class).getDepartingFlights(id)).withSelfRel()
        );
    }

    @GetMapping("/{id}/arrivals")
    public CollectionModel<FlightDTO> getArrivingFlights(@PathVariable Long id) {
        List<FlightDTO> flights = airportService.getArrivingFlights(id);
        return CollectionModel.of(flights, 
                linkTo(methodOn(AirportController.class).getArrivingFlights(id)).withSelfRel()
        );
    }
}