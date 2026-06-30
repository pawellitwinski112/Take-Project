package pl.polsl.take.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequestDTO {
    private Long id;
    private String name;
    private String surname;
    private String mail;
    private Integer phoneNum;
}