package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Flight;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
}