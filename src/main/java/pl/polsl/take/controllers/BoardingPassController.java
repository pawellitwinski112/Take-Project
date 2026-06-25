package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.dto.BoardingPassDTO;
import pl.polsl.take.dto.BoardingPassRequestDTO;
import pl.polsl.take.services.BoardingPassService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/boarding-passes")
public class BoardingPassController {

    private final BoardingPassService boardingPassService;

    public BoardingPassController(BoardingPassService boardingPassService) {
        this.boardingPassService = boardingPassService;
    }
    
    @PostMapping
    public BoardingPassDTO addBoardingPass(@RequestBody BoardingPassRequestDTO dto) {
        return boardingPassService.create(dto);
    }

    @GetMapping
    public CollectionModel<BoardingPassDTO> getAllBoardingPasses() {
        List<BoardingPassDTO> passes = boardingPassService.getAll();
        return CollectionModel.of(passes, 
                linkTo(methodOn(BoardingPassController.class).getAllBoardingPasses()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public BoardingPassDTO getBoardingPassById(@PathVariable Long id) {
        return boardingPassService.getById(id);
    }

    @PutMapping
    public BoardingPassDTO updateBoardingPass(@RequestBody BoardingPassRequestDTO dto) {
        return boardingPassService.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardingPass(@PathVariable Long id) {
        boardingPassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}