package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.AirplaneDTO;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.repositories.AirplaneRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;
    private final FlightRepository flightRepository;

    public AirplaneService(AirplaneRepository airplaneRepository, FlightRepository flightRepository) {
        this.airplaneRepository = airplaneRepository;
        this.flightRepository = flightRepository;
    }

    public List<AirplaneDTO> getAll() {
        return StreamSupport
                .stream(airplaneRepository.findAll().spliterator(), false)
                .map(AirplaneDTO::new)
                .collect(Collectors.toList());
    }

    public AirplaneDTO getById(Long id) {
        return airplaneRepository.findById(id)
                .map(AirplaneDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono samolotu o ID " + id));
    }

    public Airplane create(Airplane airplane) {
        if (airplane.getId() != null && airplane.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania samolotu nie podawaj ID.");
        }
        airplane.setId(null);
        return airplaneRepository.save(airplane);
    }

    public Airplane update(Airplane airplane) {
        if (airplane.getId() == null || !airplaneRepository.existsById(airplane.getId())) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować samolot, podaj poprawne ID.");
        }
        
        Airplane originalAirplane = airplaneRepository.findById(airplane.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Samolot o podanym ID nie istnieje."));
        
        originalAirplane.setSeats(airplane.getSeats());
        originalAirplane.setProducent(airplane.getProducent());
        originalAirplane.setModel(airplane.getModel());
        originalAirplane.setMaxRange(airplane.getMaxRange());
        originalAirplane.setProductionDate(airplane.getProductionDate());
        originalAirplane.setRegistration(airplane.getRegistration());

        Airplane updated = airplaneRepository.save(originalAirplane);
        return airplaneRepository.save(updated);
    }

    public void delete(Long id) {
        if (!airplaneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Błąd: Nie znaleziono samolotu o ID " + id);
        }

        if (flightRepository.existsByAirplaneId(id)) {
            throw new IllegalStateException("Konflikt: Nie można usunąć samolotu, ponieważ bierze on udział w zaplanowanych lotach.");
        }

        try {
            airplaneRepository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("Konflikt: Nie można usunąć samolotu z powodu więzów integralności bazy danych.");
        }
    }
}