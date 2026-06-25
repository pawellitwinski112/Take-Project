package pl.polsl.take.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengers")
@NoArgsConstructor
public class Passenger {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "passenger_id")
	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String surname;
	
	@Getter @Setter
	private String mail;
	
	@Column(name = "phone_num")
	@Getter @Setter
	private Integer phoneNum;
	
	@OneToMany(mappedBy = "passenger")
	@Getter @Setter
	@JsonIgnore 
	private List<BoardingPass> boardingPasses;
	
	@Version
	@jakarta.persistence.Column(nullable = false, columnDefinition = "int default 0")
	private Long version;
}
