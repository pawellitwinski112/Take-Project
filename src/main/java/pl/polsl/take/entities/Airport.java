package pl.polsl.take.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "airports")
@NoArgsConstructor
@Getter @Setter
public class Airport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "airport_id")
	
	private Long id;
	
	@Column(name = "icao_code")
	private String icaoCode;
	
	@Column(name = "airport_name")
	private String airportName;
	
	private String country;
	
	private String city;
	
	@OneToMany(mappedBy = "departureAirport")
	@JsonIgnore
	private List<Flight> departingFlights;

	@OneToMany(mappedBy = "arrivalAirport")
	@JsonIgnore
	private List<Flight> arrivingFlights;
	
	@Version
	@JsonIgnore
	@jakarta.persistence.Column(nullable = false, columnDefinition = "int default 0")
	private Long version;
}

