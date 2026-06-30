package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Airplane;

@Repository
public interface AirplaneRepository extends CrudRepository<Airplane, Long> {}