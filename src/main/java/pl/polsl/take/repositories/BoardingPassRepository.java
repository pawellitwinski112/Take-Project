package pl.polsl.take.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.BoardingPass;

@Repository
public interface BoardingPassRepository extends CrudRepository<BoardingPass, Long> {
}