package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.dto.FlightRequestDTO;
import pl.polsl.take.services.FlightService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService; // Tylko jedno czyste wstrzyknięcie
    
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public FlightDTO addFlight(@RequestBody FlightRequestDTO dto) {
        return flightService.create(dto);
    }

    @GetMapping
    public CollectionModel<FlightDTO> getAllFlights() {
        List<FlightDTO> flights = flightService.getAll();
        return CollectionModel.of(flights, 
                linkTo(methodOn(FlightController.class).getAllFlights()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public FlightDTO getFlightById(@PathVariable Long id) {
        return flightService.getById(id);
    }

    @PutMapping
    public FlightDTO updateFlight(@RequestBody FlightRequestDTO dto) {
        return flightService.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/delay")
    public String delayFlights(@RequestParam Long airportId, @RequestParam Integer minutes) {
        return flightService.delayFlights(airportId, minutes);
    }
    
    @GetMapping("/analysis")
    public Long getFlightCountAnalysis(@RequestParam Long airlineId, @RequestParam Long airportId, 
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
        
        return flightService.getFlightCountAnalysis(airlineId, airportId, start, end);
    }
}