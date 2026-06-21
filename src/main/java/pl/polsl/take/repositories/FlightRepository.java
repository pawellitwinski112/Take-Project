package pl.polsl.take.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.polsl.take.entities.Flight;

@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
    // Spring sam przetłumaczy to na: SELECT COUNT(*) > 0 FROM flights WHERE airplane_id = ?
    boolean existsByAirplaneId(Long airplaneId);
   
    @jakarta.transaction.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE Flight f SET f.departureTime = TIMESTAMPADD(MINUTE, :minutes, f.departureTime) WHERE f.departureAirport.id = :airportId")
    int delayFlightsFromAirport(@Param("airportId") Long airportId, @Param("minutes") Integer minutes);
    
    @Query("SELECT COUNT(f) FROM Flight f WHERE f.airline.id = :airlineId AND f.departureAirport.id = :airportId AND f.departureTime BETWEEN :start AND :end")
    Long countFlightsForAirlineAndAirport(@Param("airlineId") Long airlineId, @Param("airportId") Long airportId, @Param("start") java.time.LocalDateTime start, @Param("end") java.time.LocalDateTime end);
}