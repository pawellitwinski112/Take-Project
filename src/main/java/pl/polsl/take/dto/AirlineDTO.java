package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.Airline;
import pl.polsl.take.controllers.AirlineController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AirlineDTO extends RepresentationModel<AirlineDTO> {

    public Long id;
    public String name;
    public String icaoName;
    public Integer helpdeskNumber;

    public AirlineDTO(Airline airline) {
        super();
        this.id = airline.getId();
        this.name = airline.getName();
        this.icaoName = airline.getIcaoName();
        this.helpdeskNumber = airline.getHelpdeskNumber();

        // 1. Link do samego siebie (Self)
        this.add(linkTo(methodOn(AirlineController.class).getAirlineById(airline.getId())).withSelfRel());
    }
}