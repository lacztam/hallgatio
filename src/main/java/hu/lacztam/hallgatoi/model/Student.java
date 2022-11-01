package hu.lacztam.hallgatoi.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Cacheable
@Audited
public class Student {
	
	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include()
	private int id;
	
	private String name;
	private LocalDate birthdate;
	private int semester;
	
	@Column(name = "central_identifier")
	private int centralIdentifier;
	@Column(name = "used_free_semester")
	private int usedFreeSemester;
	
	 

}
