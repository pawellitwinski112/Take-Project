package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Passenger;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
    // Spring Data JPA sam dostarczy implementację metody save()
}