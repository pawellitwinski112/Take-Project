package pl.polsl.take.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "airports")
@NoArgsConstructor
public class Airport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "airport_id")
	@Getter @Setter
	private Long id;
	
	@Column(name = "icao_code")
	@Getter @Setter
	private String icaoCode;
	
	@Column(name = "airport_name")
	@Getter @Setter
	private String airportName;
	
	@Getter @Setter
	private String country;
	
	@Getter @Setter
	private String city;
	
	@OneToMany(mappedBy = "departureAirport")
	private List<Flight> departingFlights;

	@OneToMany(mappedBy = "arrivalAirport")
	private List<Flight> arrivingFlights;
}

