package pl.polsl.take.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDTO {
    private Long id;
    private Long airplaneId;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Long airlineId;
}