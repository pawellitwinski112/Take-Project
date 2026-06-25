package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.repositories.AirportRepository;

import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.entities.Flight;
import org.springframework.hateoas.CollectionModel;
import pl.polsl.take.dto.AirportDTO;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final AirportRepository airportRepository;

    public AirportController(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public Airport addAirport(@RequestBody Airport airport) {
        if (airport.getId() != null && airport.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania lotniska nie podawaj ID.");
        }
        return airportRepository.save(airport);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
   // 1. Pobieranie listy wszystkich lotnisk
    @GetMapping
    public CollectionModel<AirportDTO> getAllAirports() {
        // Używamy StreamSupport, aby zamienić Iterable na Stream 
        java.util.List<AirportDTO> airports = java.util.stream.StreamSupport
                .stream(airportRepository.findAll().spliterator(), false)
                .map(AirportDTO::new)
                .collect(java.util.stream.Collectors.toList());
                
        return CollectionModel.of(airports, 
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(AirportController.class).getAllAirports()).withSelfRel()
                );
    }

    // 2. Pobieranie konkretnego lotniska po ID (np. /airports/1)
    @GetMapping("/{id}")
    public AirportDTO getAirportById(@PathVariable Long id) {
        return airportRepository.findById(id)
                .map(AirportDTO::new) 
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
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        // Najpierw pobieramy lotnisko z bazy
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotniska o ID " + id));

        // Zabezpieczenie biznesowe: Sprawdzamy, czy są przypisane loty
        boolean hasDepartures = airport.getDepartingFlights() != null && !airport.getDepartingFlights().isEmpty();
        boolean hasArrivals = airport.getArrivingFlights() != null && !airport.getArrivingFlights().isEmpty();

        if (hasDepartures || hasArrivals) {
            throw new IllegalStateException("Konflikt: Nie można usunąć lotniska, ponieważ posiada zaplanowane loty.");
        }

        airportRepository.delete(airport);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // METODY DOCIĄGAJĄCE (HATEOAS)
    // ==========================================
    @GetMapping("/{id}/departures")
    public CollectionModel<FlightDTO> getDepartingFlights(@PathVariable Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotniska o ID " + id));
        
        List<FlightDTO> flightsDTO = new ArrayList<>();
        if(airport.getDepartingFlights() != null) {
            for(Flight f : airport.getDepartingFlights()) {
                flightsDTO.add(new FlightDTO(f));
            }
        }
        return CollectionModel.of(flightsDTO, org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                        org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(AirportController.class).getDepartingFlights(id)
                ).withSelfRel());
    }

    @GetMapping("/{id}/arrivals")
    public CollectionModel<FlightDTO> getArrivingFlights(@PathVariable Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotniska o ID " + id));
        
        List<FlightDTO> flightsDTO = new ArrayList<>();
        if(airport.getArrivingFlights() != null) {
            for(Flight f : airport.getArrivingFlights()) {
                flightsDTO.add(new FlightDTO(f));
            }
        }
      // POPRAWKA: Przekazujemy listę ORAZ link do samej siebie (z uwzględnieniem ID lotniska!)
        return CollectionModel.of(flightsDTO,
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                        org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(AirportController.class).getArrivingFlights(id)
                ).withSelfRel());
    }
}