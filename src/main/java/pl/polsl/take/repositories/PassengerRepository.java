package pl.polsl.take.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Passenger;
import pl.polsl.take.entities.PassengerManifestDto;

import org.springframework.data.repository.query.Param;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
	@Query("SELECT new pl.polsl.take.entities.PassengerManifestDto(p, bp.seat) FROM BoardingPass bp JOIN bp.passenger p WHERE bp.flight.id = :flightId")
	List<PassengerManifestDto> findPassengerManifestByFlightId(@Param("flightId") Long flightId);
}

