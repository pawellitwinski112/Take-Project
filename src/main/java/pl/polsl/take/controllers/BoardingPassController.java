package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.BoardingPass;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.repositories.BoardingPassRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.repositories.PassengerRepository;
import pl.polsl.take.dto.BoardingPassDTO;
import pl.polsl.take.dto.BoardingPassRequestDTO;

import org.springframework.hateoas.CollectionModel;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/boarding-passes")
public class BoardingPassController {

    private final BoardingPassRepository boardingPassRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    public BoardingPassController(BoardingPassRepository boardingPassRepository, FlightRepository flightRepository, PassengerRepository passengerRepository) {
        this.boardingPassRepository = boardingPassRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }
    
    @PostMapping
    public BoardingPassDTO addBoardingPass(@RequestBody BoardingPassRequestDTO dto) {
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas generowania karty pokładowej nie podawaj ID.");
        }
        BoardingPass pass = new BoardingPass();
        mapDtoToEntity(dto, pass);
        
        BoardingPass savedPass = boardingPassRepository.save(pass);
        return new BoardingPassDTO(savedPass);
    }
    
    // ==========================================
    // R - READ (GET)
    // ==========================================

    @GetMapping
    public CollectionModel<BoardingPassDTO> getAllBoardingPasses() {
        List<BoardingPassDTO> passesDTO = new ArrayList<>();
        for(BoardingPass pass : boardingPassRepository.findAll()) {
            passesDTO.add(new BoardingPassDTO(pass));
        }
        return CollectionModel.of(passesDTO, 
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo(
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(BoardingPassController.class).getAllBoardingPasses()).withSelfRel()
                );
    }

    @GetMapping("/{id}")
    public BoardingPassDTO getBoardingPassById(@PathVariable Long id) {
        BoardingPass pass = boardingPassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono karty pokładowej o ID " + id));
        return new BoardingPassDTO(pass);
    }

    // ==========================================
    // U - UPDATE (PUT)
    // ==========================================

    @PutMapping
    public BoardingPassDTO updateBoardingPass(@RequestBody BoardingPassRequestDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Podaj poprawne ID do aktualizacji karty pokładowej.");
        }
        
        BoardingPass pass = boardingPassRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Błąd: Karta pokładowa o podanym ID nie istnieje."));
        
        mapDtoToEntity(dto, pass);
        
        BoardingPass updatedPass = boardingPassRepository.save(pass);
        return new BoardingPassDTO(updatedPass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardingPass(@PathVariable Long id) {
    	try {
    		boardingPassRepository.deleteById(id);
    		return ResponseEntity.noContent().build();
    	} catch (org.springframework.dao.EmptyResultDataAccessException | jakarta.persistence.EntityNotFoundException e) {
            throw new RuntimeException("Błąd: Nie znaleziono lotu o ID " + id);
        }
    }
    
    private void mapDtoToEntity(BoardingPassRequestDTO dto, BoardingPass pass) {
        pass.setFlightClass(dto.getFlightClass());
        pass.setSeat(dto.getSeat());

        if (dto.getFlightId() != null) {
            Flight flight = flightRepository.findById(dto.getFlightId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono lotu o ID: " + dto.getFlightId()));
            pass.setFlight(flight);
        }

        if (dto.getPassengerId() != null) {
            Passenger passenger = passengerRepository.findById(dto.getPassengerId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono pasażera o ID: " + dto.getPassengerId()));
            pass.setPassenger(passenger);
        }
    }
}