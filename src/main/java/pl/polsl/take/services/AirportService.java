package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.AirportDTO;
import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.repositories.AirportRepository;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<AirportDTO> getAll() {
        return StreamSupport
                .stream(airportRepository.findAll().spliterator(), false)
                .map(AirportDTO::new)
                .collect(Collectors.toList());
    }

    public AirportDTO getById(Long id) {
        return airportRepository.findById(id)
                .map(AirportDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska o ID " + id));
    }

    public Airport create(Airport airport) {
        if (airport.getId() != null && airport.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas dodawania lotniska nie podawaj ID.");
        }
        airport.setId(null);
        return airportRepository.save(airport);
    }

    public Airport update(Airport airport) {
        if (airport.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować lotnisko, musisz podać jego ID.");
        }
        if (!airportRepository.existsById(airport.getId())) {
            throw new ResourceNotFoundException("Błąd: Lotnisko o podanym ID nie istnieje.");
        }
        return airportRepository.save(airport);
    }

    public void delete(Long id) {
        try {
            airportRepository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalStateException("Konflikt: Nie można usunąć lotniska, ponieważ posiada zaplanowane loty.");
        } catch (org.springframework.dao.EmptyResultDataAccessException | jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Błąd: Nie znaleziono lotniska o ID " + id);
        }
    }

    public List<FlightDTO> getDepartingFlights(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska o ID " + id));
        
        List<FlightDTO> flightsDTO = new ArrayList<>();
        if (airport.getDepartingFlights() != null) {
            for (Flight f : airport.getDepartingFlights()) {
                flightsDTO.add(new FlightDTO(f));
            }
        }
        return flightsDTO;
    }

    public List<FlightDTO> getArrivingFlights(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska o ID " + id));
        
        List<FlightDTO> flightsDTO = new ArrayList<>();
        if (airport.getArrivingFlights() != null) {
            for (Flight f : airport.getArrivingFlights()) {
                flightsDTO.add(new FlightDTO(f));
            }
        }
        return flightsDTO;
    }
}