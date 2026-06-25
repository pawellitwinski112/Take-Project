package pl.polsl.take.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.dto.PassengerManifestDTO;
import pl.polsl.take.dto.PassengerRequestDTO;
import pl.polsl.take.repositories.PassengerRepository;
import pl.polsl.take.repositories.BoardingPassRepository;
import pl.polsl.take.dto.PassengerDTO;
import pl.polsl.take.dto.BoardingPassDTO;
import org.springframework.hateoas.CollectionModel;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerRepository passengerRepository;
    private final BoardingPassRepository boardingPassRepository;

    public PassengerController(PassengerRepository passengerRepository, BoardingPassRepository boardingPassRepository) {
        this.passengerRepository = passengerRepository;
        this.boardingPassRepository = boardingPassRepository;
    }

    // ==========================================
    // C - CREATE (POST)
    // ==========================================
    @PostMapping
    public PassengerDTO addPassenger(@RequestBody PassengerRequestDTO dto) {
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania pasażera nie podawaj ID.");
        }
        Passenger passenger = new Passenger();
        mapDtoToEntity(dto, passenger);
        
        Passenger savedPassenger = passengerRepository.save(passenger);
        return new PassengerDTO(savedPassenger);
    }

    // ==========================================
    // R - READ (GET)
    // ==========================================
    // 1. Pobieranie listy wszystkich pasażerów
    @GetMapping
    public CollectionModel<PassengerDTO> getAllPassengers() {
        java.util.List<PassengerDTO> passengers = StreamSupport
                .stream(passengerRepository.findAll().spliterator(), false)
                .map(PassengerDTO::new)
                .collect(Collectors.toList());
        return CollectionModel.of(passengers, 
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(PassengerController.class).getAllPassengers()).withSelfRel()
                );
    }
    // 2. Pobieranie konkretnego pasażera po ID (np. /passengers/1)
    @GetMapping("/{id}")
    public PassengerDTO getPassengerById(@PathVariable Long id) {
            return passengerRepository.findById(id)
                    .map(PassengerDTO::new)
                    .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono pasażera o ID " + id));
    }
    // ==========================================
    // METODA DOCIĄGAJĄCA KARTY POKŁADOWE PASAŻERA
    // ==========================================
    @GetMapping("/{id}/boarding-passes")
    public org.springframework.hateoas.CollectionModel<pl.polsl.take.dto.BoardingPassDTO> getBoardingPassesForPassenger(@PathVariable Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono pasażera o ID " + id));
        
        java.util.List<pl.polsl.take.dto.BoardingPassDTO> passes = passenger.getBoardingPasses().stream()
                .map(pl.polsl.take.dto.BoardingPassDTO::new)
                .collect(java.util.stream.Collectors.toList());
                
        return org.springframework.hateoas.CollectionModel.of(passes);
    }
    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================
    @PutMapping
    public PassengerDTO updatePassenger(@RequestBody PassengerRequestDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować pasażera, musisz podać jego ID.");
        }
        Passenger passenger = passengerRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Błąd: Pasażer o podanym ID nie istnieje."));
        
        mapDtoToEntity(dto, passenger);
        Passenger updatedPassenger = passengerRepository.save(passenger);
        return new PassengerDTO(updatedPassenger);
    }

    // ==========================================
    // D - DELETE (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        // Najpierw pobieramy pasażera z bazy
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono pasażera o ID " + id));

        // Zabezpieczenie biznesowe: Sprawdzamy, czy pasażer posiada karty pokładowe
        if (boardingPassRepository.existsByPassengerId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć pasażera, ponieważ posiada przypisane karty pokładowe.");
        }

        // Jeśli jest czysto - usuwamy
        passengerRepository.delete(passenger);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/manifest/{flightId}")
    public List<PassengerManifestDTO> getPassengerManifest(@PathVariable Long flightId){
    	return passengerRepository.findPassengerManifestByFlightId(flightId);
    }
    
    private void mapDtoToEntity(PassengerRequestDTO dto, Passenger passenger) {
        passenger.setName(dto.getName());
        passenger.setSurname(dto.getSurname());
        passenger.setMail(dto.getMail());
        passenger.setPhoneNum(dto.getPhoneNum());
    }
}