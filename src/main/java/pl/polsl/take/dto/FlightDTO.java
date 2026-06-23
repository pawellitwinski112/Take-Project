package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.Flight;
import pl.polsl.take.controllers.AirplaneController;
import pl.polsl.take.controllers.AirportController;
import pl.polsl.take.controllers.AirlineController;
import pl.polsl.take.controllers.FlightController;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class FlightDTO extends RepresentationModel<FlightDTO> {

    // Kopiujemy tylko płaskie dane z oryginalnej encji Flight
    public Long id;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;

    // Konstruktor, który przyjmuje encję bazodanową i zamienia ją na DTO z linkami
    public FlightDTO(Flight flight) {
        super();
        this.id = flight.getId();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();

        // 1. Link do samego siebie (self) - standard REST
        this.add(linkTo(methodOn(FlightController.class).getFlightById(flight.getId())).withSelfRel());

        // 2. Link do samolotu, który obsługuje ten lot
        if (flight.getAirplane() != null) {
            this.add(linkTo(methodOn(AirplaneController.class).getAirplaneById(flight.getAirplane().getId())).withRel("airplane"));
        }

        // 3. Link do lotniska startowego
        if (flight.getDepartureAirport() != null) {
            this.add(linkTo(methodOn(AirportController.class).getAirportById(flight.getDepartureAirport().getId())).withRel("departureAirport"));
        }

        // 4. Link do lotniska końcowego
        if (flight.getArrivalAirport() != null) {
            this.add(linkTo(methodOn(AirportController.class).getAirportById(flight.getArrivalAirport().getId())).withRel("arrivalAirport"));
        }

        // 5. Link do linii lotniczej
        if (flight.getAirline() != null) {
            this.add(linkTo(methodOn(AirlineController.class).getAirlineById(flight.getAirline().getId())).withRel("airline"));
        }
    }
}