package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.dto.FlightDTO;
import org.springframework.hateoas.CollectionModel;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @PostMapping
    public Flight addFlight(@RequestBody Flight flight) {
        if (flight.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas tworzenia lotu nie podawaj ID.");
        }
        return flightRepository.save(flight);
    }

   // ==========================================
    // R - READ (GET) z obsługą HATEOAS
    // ==========================================
    @GetMapping
    public CollectionModel<FlightDTO> getAllFlights() {
        List<FlightDTO> flightsDTO = new ArrayList<>();
        for(Flight flight : flightRepository.findAll()) {
            flightsDTO.add(new FlightDTO(flight));
        }
        return CollectionModel.of(flightsDTO);
    }

    @GetMapping("/{id}")
    public FlightDTO getFlightById(@PathVariable Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotu o ID " + id));
        return new FlightDTO(flight);
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public Flight updateFlight(@RequestBody Flight updatedFlight) {
        // Zabezpieczenie: Czy podano ID lotu do aktualizacji?
        if (updatedFlight.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować lot, musisz podać jego ID.");
        }
        
        // Zabezpieczenie: Czy taki lot w ogóle istnieje w bazie?
        if (!flightRepository.existsById(updatedFlight.getId())) {
            throw new RuntimeException("Błąd: Lot o podanym ID nie istnieje.");
        }

        // Zapisujemy zaktualizowany obiekt
        return flightRepository.save(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        // Tu z kolei zadziała automatyczne usuwanie kaskadowe kart pokładowych (BoardingPass),
        // które skonfigurowaliśmy wcześniej w encji Flight!
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Nie znaleziono lotu o ID " + id);
        }
        flightRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/delay")
    public String delayFlights(@RequestParam Long airportId, @RequestParam Integer minutes) {
        if (minutes == null || minutes <= 0) {
            throw new IllegalArgumentException("Błąd: Liczba minut opóźnienia musi być większa od zera.");
        }
    	int updatedRows = flightRepository.delayFlightsFromAirport(airportId, minutes);
        if (updatedRows == 0) {
            throw new RuntimeException("Błąd: Nie znaleziono lotów z lotniska o ID " + airportId + " lub lotnisko nie istnieje.");
        }
    	return "Sukces: Opóźniono " + updatedRows + " lotów z lotniska o ID " + airportId + " o " + minutes + " minut.";
    }
    
    @GetMapping("/analysis")
    public Long getFlightCountAnalysis(@RequestParam Long airlineId, @RequestParam Long airportId, 
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate start,
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
    	if (start == null || end == null) {
            throw new IllegalArgumentException("Błąd: Parametry 'start' i 'end' są wymagane.");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Błąd: Data początkowa musi być wcześniejsza niż data końcowa.");
        }
    	java.time.LocalDateTime startOfDay = start.atStartOfDay();
    	java.time.LocalDateTime endOfDay = end.atTime(java.time.LocalTime.MAX);
    	return flightRepository.countFlightsForAirlineAndAirport(airlineId, airportId, startOfDay, endOfDay);
    }
}