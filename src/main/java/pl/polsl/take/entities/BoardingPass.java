package pl.polsl.take.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boarding_pass")
@NoArgsConstructor
public class BoardingPass {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pass_id")
	@Getter @Setter
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "flight")
	@Getter @Setter
	private Flight flight;
	
	@ManyToOne
	@JoinColumn(name = "passenger")
	@Getter @Setter
	private Passenger passenger;
	
	@Column(name = "class")
	@Getter @Setter
	private String flightClass;
	
	@Getter @Setter
	private Integer seat;
}
