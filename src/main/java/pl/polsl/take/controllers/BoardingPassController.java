package pl.polsl.take.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.polsl.take.entities.BoardingPass;
import pl.polsl.take.repositories.BoardingPassRepository;

@RestController
@RequestMapping("/boarding-passes")
public class BoardingPassController {

    private final BoardingPassRepository boardingPassRepository;

    public BoardingPassController(BoardingPassRepository boardingPassRepository) {
        this.boardingPassRepository = boardingPassRepository;
    }

    @PostMapping
    public BoardingPass addBoardingPass(@RequestBody BoardingPass boardingPass) {
        if (boardingPass.getId() != null) {
            throw new IllegalArgumentException("Błąd: Podczas generowania karty pokładowej nie podawaj ID.");
        }
        return boardingPassRepository.save(boardingPass);
    }

    @GetMapping
    public Iterable<BoardingPass> getAllBoardingPasses() {
        return boardingPassRepository.findAll();
    }

    @GetMapping("/{id}")
    public BoardingPass getBoardingPassById(@PathVariable Long id) {
        return boardingPassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono karty pokładowej o ID " + id));
    }

    @PutMapping
    public BoardingPass updateBoardingPass(@RequestBody BoardingPass boardingPass) {
        if (boardingPass.getId() == null || !boardingPassRepository.existsById(boardingPass.getId())) {
            throw new IllegalArgumentException("Błąd: Podaj poprawne ID do aktualizacji karty pokładowej.");
        }
        return boardingPassRepository.save(boardingPass);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardingPass(@PathVariable Long id) {
        if (!boardingPassRepository.existsById(id)) {
            throw new RuntimeException("Błąd: Nie znaleziono karty pokładowej o ID " + id);
        }
        boardingPassRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}