package pl.polsl.take.controllers;

import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.repositories.FlightRepository;

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

    @GetMapping
    public Iterable<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono lotu o ID " + id));
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
    public void deleteFlight(@PathVariable Long id) {
        // Tu z kolei zadziała automatyczne usuwanie kaskadowe kart pokładowych (BoardingPass), 
        // które skonfigurowaliśmy wczoraj w encji Flight!
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Lot o podanym ID nie istnieje.");
        }
        flightRepository.deleteById(id);
    }
    
    @PutMapping("/delay")
    public String delayFlights(@RequestParam Long airportId, @RequestParam Integer minutes) {
    	int updatedRows = flightRepository.delayFlightsFromAirport(airportId, minutes);
    	return "Sukces: Opóźniono " + updatedRows + " lotów z lotniska o ID " + airportId + " o " + minutes + " minut.";
    }
    
    @GetMapping("/analysis")
    public Long getFlightCountAnalysis(@RequestParam Long airlineId, @RequestParam Long airportId, 
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate start,
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
    	if (start.isAfter(end)) {
    	    throw new IllegalArgumentException("Data początkowa nie może być późniejsza niż data końcowa.");
    	}
    	java.time.LocalDateTime startOfDay = start.atStartOfDay();
    	java.time.LocalDateTime endOfDay = end.atTime(java.time.LocalTime.MAX);
    	return flightRepository.countFlightsForAirlineAndAirport(airlineId, airportId, startOfDay, endOfDay);
    }
}