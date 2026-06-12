package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Airline;

@Repository
public interface AirlineRepository extends CrudRepository<Airline, Long> {
    // Spring Data JPA sam dostarczy implementację metody save()
}