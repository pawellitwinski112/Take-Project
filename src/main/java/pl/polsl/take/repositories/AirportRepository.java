package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Airport;

@Repository
public interface AirportRepository extends CrudRepository<Airport, Long> {
    // Spring Data JPA sam dostarczy implementację metody save()
}