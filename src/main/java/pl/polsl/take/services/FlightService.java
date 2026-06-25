package pl.polsl.take.services;

import org.springframework.stereotype.Service;
import pl.polsl.take.dto.FlightDTO;
import pl.polsl.take.dto.FlightRequestDTO;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.repositories.AirlineRepository;
import pl.polsl.take.repositories.AirplaneRepository;
import pl.polsl.take.repositories.AirportRepository;
import pl.polsl.take.repositories.FlightRepository;
import pl.polsl.take.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirplaneRepository airplaneRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;

    public FlightService(FlightRepository flightRepository, AirplaneRepository airplaneRepository, 
                         AirportRepository airportRepository, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.airplaneRepository = airplaneRepository;
        this.airportRepository = airportRepository;
        this.airlineRepository = airlineRepository;
    }

    public List<FlightDTO> getAll() {
        List<FlightDTO> flightsDTO = new ArrayList<>();
        for (Flight flight : flightRepository.findAll()) {
            flightsDTO.add(new FlightDTO(flight));
        }
        return flightsDTO;
    }

    public FlightDTO getById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotu o ID " + id));
        return new FlightDTO(flight);
    }

    public FlightDTO create(FlightRequestDTO dto) {
        if (dto.getId() != null && dto.getId() != 0) {
            throw new IllegalArgumentException("Błąd: Podczas tworzenia lotu nie podawaj ID.");
        }
        
        dto.setId(null);
        Flight flight = new Flight();
        mapDtoToEntity(dto, flight);
        
        Flight savedFlight = flightRepository.save(flight);
        return new FlightDTO(savedFlight);
    }

    public FlightDTO update(FlightRequestDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Błąd: Aby zaktualizować lot, musisz podać jego ID.");
        }
        
        Flight flight = flightRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Lot o podanym ID nie istnieje."));

        mapDtoToEntity(dto, flight);
        Flight updatedFlight = flightRepository.save(flight);
        return new FlightDTO(updatedFlight);
    }

    public void delete(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotu o ID " + id));
        flightRepository.delete(flight);
    }

    public String delayFlights(Long airportId, Integer minutes) {
        if (minutes == null || minutes <= 0) {
            throw new IllegalArgumentException("Błąd: Liczba minut opóźnienia musi być większa od zera.");
        }
        int updatedRows = flightRepository.delayFlightsFromAirport(airportId, minutes);
        if (updatedRows == 0) {
            throw new ResourceNotFoundException("Błąd: Nie znaleziono lotów z lotniska o ID " + airportId + " lub lotnisko nie istnieje.");
        }
        return "Sukces: Opóźniono " + updatedRows + " lotów z lotniska o ID " + airportId + " o " + minutes + " minut.";
    }

    public Long getFlightCountAnalysis(Long airlineId, Long airportId, LocalDate start, LocalDate end) {
        if (!airlineRepository.existsById(airlineId)) {
            throw new ResourceNotFoundException("Błąd: Linia lotnicza o ID " + airlineId + " nie istnieje.");
        }
        if (!airportRepository.existsById(airportId)) {
            throw new ResourceNotFoundException("Błąd: Lotnisko o ID " + airportId + " nie istnieje.");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Błąd: Parametry 'start' i 'end' są wymagane.");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Błąd: Data początkowa musi być wcześniejsza niż data końcowa.");
        }
        
        LocalDateTime startOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.atTime(LocalTime.MAX);
        return flightRepository.countFlightsForAirlineAndAirport(airlineId, airportId, startOfDay, endOfDay);
    }

    private void mapDtoToEntity(FlightRequestDTO dto, Flight flight) {
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());

        if (dto.getAirplaneId() != null) {
            Airplane airplane = airplaneRepository.findById(dto.getAirplaneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono samolotu o ID: " + dto.getAirplaneId()));
            flight.setAirplane(airplane);
        }

        if (dto.getDepartureAirportId() != null) {
            Airport depAirport = airportRepository.findById(dto.getDepartureAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska wylotu o ID: " + dto.getDepartureAirportId()));
            flight.setDepartureAirport(depAirport);
        }

        if (dto.getArrivalAirportId() != null) {
            Airport arrAirport = airportRepository.findById(dto.getArrivalAirportId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono lotniska przylotu o ID: " + dto.getArrivalAirportId()));
            flight.setArrivalAirport(arrAirport);
        }

        if (dto.getAirlineId() != null) {
            Airline airline = airlineRepository.findById(dto.getAirlineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Błąd: Nie znaleziono linii lotniczej o ID: " + dto.getAirlineId()));
            flight.setAirline(airline);
        }
    }
}