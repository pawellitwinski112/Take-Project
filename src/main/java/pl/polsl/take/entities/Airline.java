package pl.polsl.take.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airline")
@NoArgsConstructor
public class Airline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "airlane_id")
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String name;
	
	@Column(name = "icao_name")
	@Getter @Setter
	private String icaoName;
	
	@Column(name = "helpdesk_number", nullable = true)
	@Getter @Setter
	private Integer helpdeskNumber;
	
	@Version
	@jakarta.persistence.Column(nullable = false, columnDefinition = "int default 0")
	private Long version;
}
