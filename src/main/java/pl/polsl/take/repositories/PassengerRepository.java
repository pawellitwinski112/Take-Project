package pl.polsl.take.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.dto.PassengerManifestDTO;

import org.springframework.data.repository.query.Param;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
	@Query("SELECT new pl.polsl.take.dto.PassengerManifestDTO(p.name, p.surname, p.mail, p.phoneNum, bp.seat) FROM BoardingPass bp JOIN bp.passenger p WHERE bp.flight.id = :flightId")
	List<PassengerManifestDTO> findPassengerManifestByFlightId(@Param("flightId") Long flightId);
}

