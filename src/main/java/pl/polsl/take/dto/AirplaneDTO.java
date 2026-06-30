package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.Airplane;
import pl.polsl.take.controllers.AirplaneController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AirplaneDTO extends RepresentationModel<AirplaneDTO> {

    public Long id;
    public Integer seats;
    public String producent;
    public String model;
    public BigDecimal maxRange;
    public LocalDateTime productionDate;
    public String registration;

    public AirplaneDTO(Airplane airplane) {
        super();
        this.id = airplane.getId();
        this.seats = airplane.getSeats();
        this.producent = airplane.getProducent();
        this.model = airplane.getModel();
        this.maxRange = airplane.getMaxRange();
        this.productionDate = airplane.getProductionDate();
        this.registration = airplane.getRegistration();

        // 1. Link do samego siebie (Self)
        this.add(linkTo(methodOn(AirplaneController.class).getAirplaneById(airplane.getId())).withSelfRel());
    }
}