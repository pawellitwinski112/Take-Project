package pl.polsl.take.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardingPassRequestDTO {
    private Long id;
    private Long flightId;
    private Long passengerId;
    private String flightClass;
    private Integer seat;
}