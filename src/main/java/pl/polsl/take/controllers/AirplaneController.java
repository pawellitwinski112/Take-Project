package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.dto.AirplaneDTO;
import pl.polsl.take.services.AirplaneService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService; // Tylko jedno wstrzyknięcie – naszego serwisu!

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @PostMapping
    public Airplane addAirplane(@RequestBody Airplane airplane) {
        return airplaneService.create(airplane);
    }

    @GetMapping
    public CollectionModel<AirplaneDTO> getAllAirplanes() {
        List<AirplaneDTO> airplanes = airplaneService.getAll();
        return CollectionModel.of(airplanes, 
                linkTo(methodOn(AirplaneController.class).getAllAirplanes()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public AirplaneDTO getAirplaneById(@PathVariable Long id) {
        return airplaneService.getById(id);
    }

    @PutMapping
    public Airplane updateAirplane(@RequestBody Airplane airplane) {
        return airplaneService.update(airplane);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirplane(@PathVariable Long id) {
        airplaneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}