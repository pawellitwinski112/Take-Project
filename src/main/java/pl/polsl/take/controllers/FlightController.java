package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.polsl.take.entities.Airline;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.repositories.AirlineRepository;
import pl.polsl.take.repositories.AirplaneRepository;
import pl.polsl.take.repositories.AirportRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.dto.FlightRequestDTO;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.CollectionModel;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    
    public FlightController(FlightRepository flightRepository, AirplaneRepository airplaneRepository, AirportRepository airportRepository, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.airplaneRepository = airplaneRepository;
        this.airportRepository = airportRepository;
        this.airlineRepository = airlineRepository;
    }

    @PostMapping
    public FlightDTO addFlight(@RequestBody FlightRequestDTO dto) {
        // Złagodzony warunek: przepuszczamy, jeśli Swagger wygenerował null lub 0
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas tworzenia lotu nie podawaj ID.");
        }
        
        // Zabezpieczenie: siłowo ustawiamy null, by baza sama wygenerowała nowy klucz
        dto.setId(null);
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas tworzenia lotu nie podawaj ID.");
        }
        
        Flight flight = new Flight();
        mapDtoToEntity(dto, flight);
        
        Flight savedFlight = flightRepository.save(flight);
        return new FlightDTO(savedFlight);
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
        return CollectionModel.of(flightsDTO, 
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(FlightController.class).getAllFlights()).withSelfRel()
                );
    }

    @GetMapping("/{id}")
    public FlightDTO getFlightById(@PathVariable Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotu o ID " + id));
        return new FlightDTO(flight);
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public FlightDTO updateFlight(@RequestBody FlightRequestDTO dto) {
        // Zabezpieczenie: Czy podano ID lotu do aktualizacji?
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować lot, musisz podać jego ID.");
        }
        
        // Zabezpieczenie: Czy taki lot w ogóle istnieje w bazie?
        Flight flight = flightRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Błąd: Lot o podanym ID nie istnieje."));

        mapDtoToEntity(dto, flight);
        
        Flight updatedFlight = flightRepository.save(flight);
        // Zapisujemy zaktualizowany obiekt
        return new FlightDTO(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Nie znaleziono lotu o ID " + id);
        }
    }
    
    @PutMapping("/delay")
    public String delayFlights(@RequestParam Long airportId, @RequestParam Integer minutes) {
        if (minutes == null || minutes <= 0) {
            throw new IllegalArgumentException("Błąd: Liczba minut opóźnienia musi być większa od zera.");
        }
    	int updatedRows = flightRepository.delayFlightsFromAirport(airportId, minutes);
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Błąd: Nie znaleziono lotów z lotniska o ID " + airportId + " lub lotnisko nie istnieje.");
        }
    	return "Sukces: Opóźniono " + updatedRows + " lotów z lotniska o ID " + airportId + " o " + minutes + " minut.";
    }
    
    @GetMapping("/analysis")
    public Long getFlightCountAnalysis(@RequestParam Long airlineId, @RequestParam Long airportId, 
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate start,
    		@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
        
            // WALIDACJA: Sprawdzamy czy encje istnieją w słownikach
        if (!airlineRepository.existsById(airlineId)) {
            throw new ResourceNotFoundException("Błąd: Linia lotnicza o ID " + airlineId + " nie istnieje.");
        }
        if (!airportRepository.existsById(airportId)) {
            throw new ResourceNotFoundException("Błąd: Lotnisko o ID " + airportId + " nie istnieje.");
        }
    	if (start == null || end == null) {
            throw new IllegalArgumentException("Błąd: Parametry 'start' i 'end' są wymagane.");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Błąd: Data początkowa musi być wcześniejsza niż data końcowa.");
        }
    	java.time.LocalDateTime startOfDay = start.atStartOfDay();
    	java.time.LocalDateTime endOfDay = end.atTime(java.time.LocalTime.MAX);
    	return flightRepository.countFlightsForAirlineAndAirport(airlineId, airportId, startOfDay, endOfDay);
    }
    
    private void mapDtoToEntity(FlightRequestDTO dto, Flight flight) {
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());

        if (dto.getAirplaneId() != null) {
            Airplane airplane = airplaneRepository.findById(dto.getAirplaneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono samolotu o ID: " + dto.getAirplaneId()));
            flight.setAirplane(airplane);
        }

        if (dto.getDepartureAirportId() != null) {
            Airport depAirport = airportRepository.findById(dto.getDepartureAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska wylotu o ID: " + dto.getDepartureAirportId()));
            flight.setDepartureAirport(depAirport);
        }

        if (dto.getArrivalAirportId() != null) {
            Airport arrAirport = airportRepository.findById(dto.getArrivalAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska przylotu o ID: " + dto.getArrivalAirportId()));
            flight.setArrivalAirport(arrAirport);
        }

        if (dto.getAirlineId() != null) {
            Airline airline = airlineRepository.findById(dto.getAirlineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID: " + dto.getAirlineId()));
            flight.setAirline(airline);
        }
    }
}