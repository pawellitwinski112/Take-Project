package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.Airport;
import pl.polsl.take.controllers.AirportController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AirportDTO extends RepresentationModel<AirportDTO> {

    public Long id;
    public String icaoCode;
    public String airportName;
    public String country;
    public String city;

    public AirportDTO(Airport airport) {
        super();
        this.id = airport.getId();
        this.icaoCode = airport.getIcaoCode();
        this.airportName = airport.getAirportName();
        this.country = airport.getCountry();
        this.city = airport.getCity();

        // 1. Link do samego siebie (Self)
        this.add(linkTo(methodOn(AirportController.class).getAirportById(airport.getId())).withSelfRel());

        // 2. Linki do metod dociągających
        this.add(linkTo(methodOn(AirportController.class).getDepartingFlights(airport.getId())).withRel("departures"));
        this.add(linkTo(methodOn(AirportController.class).getArrivingFlights(airport.getId())).withRel("arrivals"));
    }
}