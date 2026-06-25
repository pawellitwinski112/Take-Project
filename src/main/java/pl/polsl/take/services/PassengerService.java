package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.BoardingPassDTO;
import pl.polsl.take.dto.PassengerDTO;
import pl.polsl.take.dto.PassengerManifestDTO;
import pl.polsl.take.dto.PassengerRequestDTO;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.repositories.BoardingPassRepository;
import pl.polsl.take.repositories.PassengerRepository;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BoardingPassRepository boardingPassRepository;

    public PassengerService(PassengerRepository passengerRepository, BoardingPassRepository boardingPassRepository) {
        this.passengerRepository = passengerRepository;
        this.boardingPassRepository = boardingPassRepository;
    }

    public List<PassengerDTO> getAll() {
        return StreamSupport
                .stream(passengerRepository.findAll().spliterator(), false)
                .map(PassengerDTO::new)
                .collect(Collectors.toList());
    }

    public PassengerDTO getById(Long id) {
        return passengerRepository.findById(id)
                .map(PassengerDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono pasażera o ID " + id));
    }

    public PassengerDTO create(PassengerRequestDTO dto) {
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania pasażera nie podawaj ID.");
        }
        Passenger passenger = new Passenger();
        mapDtoToEntity(dto, passenger);
        
        Passenger savedPassenger = passengerRepository.save(passenger);
        return new PassengerDTO(savedPassenger);
    }

    public PassengerDTO update(PassengerRequestDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować pasażera, musisz podać jego ID.");
        }
        Passenger passenger = passengerRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Pasażer o podanym ID nie istnieje."));
        
        mapDtoToEntity(dto, passenger);
        Passenger updatedPassenger = passengerRepository.save(passenger);
        return new PassengerDTO(updatedPassenger);
    }

    public void delete(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Błąd: Nie znaleziono pasażera o ID " + id);
        }

        if (boardingPassRepository.existsByPassengerId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć pasażera, ponieważ posiada przypisane karty pokładowe.");
        }

        try {
            passengerRepository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("Konflikt: Nie można usunąć pasażera z powodu więzów integralności w bazie.");
        }
    }

    public List<BoardingPassDTO> getBoardingPasses(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono pasażera o ID " + id));
        
        return passenger.getBoardingPasses().stream()
                .map(BoardingPassDTO::new)
                .collect(Collectors.toList());
    }

    public List<PassengerManifestDTO> getManifest(Long flightId) {
        return passengerRepository.findPassengerManifestByFlightId(flightId);
    }

    private void mapDtoToEntity(PassengerRequestDTO dto, Passenger passenger) {
        passenger.setName(dto.getName());
        passenger.setSurname(dto.getSurname());
        passenger.setMail(dto.getMail());
        passenger.setPhoneNum(dto.getPhoneNum());
    }
}