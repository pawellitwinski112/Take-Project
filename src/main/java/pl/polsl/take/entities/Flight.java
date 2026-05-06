package pl.polsl.take.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flights")
@NoArgsConstructor
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "flight_id")
	@Getter @Setter
	private Long id;

	@ManyToOne
	@JoinColumn(name = "airplane")
	@Getter @Setter
	private Airplane airplane;

	@ManyToOne
	@JoinColumn(name = "dep_airport")
	@Getter @Setter
	private Airport departureAirport;

	@ManyToOne
	@JoinColumn(name = "arr_airport")
	@Getter @Setter
	private Airport arrivalAirport;
	
	@Column(name = "dep_time")
	@Getter @Setter
	private LocalDateTime departureTime;

	@Column(name = "arr_time")
	@Getter @Setter
	private LocalDateTime arrivalTime;
	
	@ManyToOne
	@JoinColumn(name = "flight_lane")
	@Getter @Setter
	private Airline airline;
	
	@OneToMany(mappedBy = "flight")
	private List<BoardingPass> boardingPasses;
}
