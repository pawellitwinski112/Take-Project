package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.BoardingPassDTO;
import pl.polsl.take.dto.BoardingPassRequestDTO;
import pl.polsl.take.entities.BoardingPass;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.repositories.BoardingPassRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.repositories.PassengerRepository;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardingPassService {

    private final BoardingPassRepository boardingPassRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    public BoardingPassService(BoardingPassRepository boardingPassRepository, FlightRepository flightRepository, PassengerRepository passengerRepository) {
        this.boardingPassRepository = boardingPassRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }

    public List<BoardingPassDTO> getAll() {
        List<BoardingPassDTO> passesDTO = new ArrayList<>();
        for (BoardingPass pass : boardingPassRepository.findAll()) {
            passesDTO.add(new BoardingPassDTO(pass));
        }
        return passesDTO;
    }

    public BoardingPassDTO getById(Long id) {
        BoardingPass pass = boardingPassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono karty pokładowej o ID " + id));
        return new BoardingPassDTO(pass);
    }

    public BoardingPassDTO create(BoardingPassRequestDTO dto) {
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas generowania karty pokładowej nie podawaj ID.");
        }
        BoardingPass pass = new BoardingPass();
        mapDtoToEntity(dto, pass);
        
        BoardingPass savedPass = boardingPassRepository.save(pass);
        return new BoardingPassDTO(savedPass);
    }

    public BoardingPassDTO update(BoardingPassRequestDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Podaj poprawne ID do aktualizacji karty pokładowej.");
        }
        
        BoardingPass pass = boardingPassRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Karta pokładowa o podanym ID nie istnieje."));
        
        mapDtoToEntity(dto, pass);
        BoardingPass updatedPass = boardingPassRepository.save(pass);
        return new BoardingPassDTO(updatedPass);
    }

    public void delete(Long id) {
        BoardingPass pass = boardingPassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono karty pokładowej o ID " + id));
        boardingPassRepository.delete(pass);
    }

    private void mapDtoToEntity(BoardingPassRequestDTO dto, BoardingPass pass) {
        pass.setFlightClass(dto.getFlightClass());
        pass.setSeat(dto.getSeat());

        if (dto.getFlightId() != null) {
            Flight flight = flightRepository.findById(dto.getFlightId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotu o ID: " + dto.getFlightId()));
            pass.setFlight(flight);
        }

        if (dto.getPassengerId() != null) {
            Passenger passenger = passengerRepository.findById(dto.getPassengerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono pasażera o ID: " + dto.getPassengerId()));
            pass.setPassenger(passenger);
        }
    }
}