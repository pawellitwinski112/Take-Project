package pl.polsl.take.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airplanes")
@NoArgsConstructor
public class Airplane {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "airplane_id")
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private Integer seats;
	
	@Getter @Setter
	private String producent;
	
	@Getter @Setter
	private String model;
	
	@Column(name = "max_range")
	@Getter @Setter
	private BigDecimal maxRange;
	
	@Column(name = "production_date")
	@Getter @Setter
	private LocalDateTime productionDate;
	
	@Getter @Setter
	private String registration;
	
	@Version
	@jakarta.persistence.Column(nullable = false, columnDefinition = "int default 0")
	private Long version = 0L;
}
