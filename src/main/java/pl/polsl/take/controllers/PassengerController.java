package pl.polsl.take.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.dto.PassengerManifestDTO;
import pl.polsl.take.dto.PassengerRequestDTO;
import pl.polsl.take.dto.PassengerDTO;
import pl.polsl.take.dto.BoardingPassDTO;
import pl.polsl.take.services.PassengerService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public PassengerDTO addPassenger(@RequestBody PassengerRequestDTO dto) {
        return passengerService.create(dto);
    }

    @GetMapping
    public CollectionModel<PassengerDTO> getAllPassengers() {
        List<PassengerDTO> passengers = passengerService.getAll();
        return CollectionModel.of(passengers, 
                linkTo(methodOn(PassengerController.class).getAllPassengers()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public PassengerDTO getPassengerById(@PathVariable Long id) {
        return passengerService.getById(id);
    }

    @GetMapping("/{id}/boarding-passes")
    public CollectionModel<BoardingPassDTO> getBoardingPassesForPassenger(@PathVariable Long id) {
        List<BoardingPassDTO> passes = passengerService.getBoardingPasses(id);
        return CollectionModel.of(passes);
    }

    @PutMapping
    public PassengerDTO updatePassenger(@RequestBody PassengerRequestDTO dto) {
        return passengerService.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/manifest/{flightId}")
    public List<PassengerManifestDTO> getPassengerManifest(@PathVariable Long flightId){
        return passengerService.getManifest(flightId);
    }
}