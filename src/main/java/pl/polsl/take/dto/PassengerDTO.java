package pl.polsl.take.dto;

import org.springframework.hateoas.RepresentationModel;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.controllers.PassengerController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PassengerDTO extends RepresentationModel<PassengerDTO> {

    public Long id;
    public String name;
    public String surname;
    public String mail;
    public Integer phoneNum;

    public PassengerDTO(Passenger passenger) {
        super();
        this.id = passenger.getId();
        this.name = passenger.getName();
        this.surname = passenger.getSurname();
        this.mail = passenger.getMail();
        this.phoneNum = passenger.getPhoneNum();

        // 1. Link do samego siebie (Self)
        this.add(linkTo(methodOn(PassengerController.class).getPassengerById(passenger.getId())).withSelfRel());

        // 2. Link do metody dociągającej karty pokładowe tego pasażera
        this.add(linkTo(methodOn(PassengerController.class).getBoardingPassesForPassenger(passenger.getId())).withRel("boardingPasses"));
    }
}