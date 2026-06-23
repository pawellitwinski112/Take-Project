package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.BoardingPass;
import pl.polsl.take.controllers.BoardingPassController;
import pl.polsl.take.controllers.FlightController;
import pl.polsl.take.controllers.PassengerController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BoardingPassDTO extends RepresentationModel<BoardingPassDTO> {

    public Long id;
    public String flightClass;
    public Integer seat;

    public BoardingPassDTO(BoardingPass boardingPass) {
        super();
        this.id = boardingPass.getId();
        this.flightClass = boardingPass.getFlightClass();
        this.seat = boardingPass.getSeat();

        // 1. Link do samego siebie (Self)
        this.add(linkTo(methodOn(BoardingPassController.class).getBoardingPassById(boardingPass.getId())).withSelfRel());

        // 2. Link do szczegółów lotu
        if (boardingPass.getFlight() != null) {
            this.add(linkTo(methodOn(FlightController.class).getFlightById(boardingPass.getFlight().getId())).withRel("flight"));
        }

        // 3. Link do profilu pasażera
        if (boardingPass.getPassenger() != null) {
            this.add(linkTo(methodOn(PassengerController.class).getPassengerById(boardingPass.getPassenger().getId())).withRel("passenger"));
        }

    }
}