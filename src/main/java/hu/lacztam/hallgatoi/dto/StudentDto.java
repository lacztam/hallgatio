package hu.lacztam.hallgatoi.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StudentDto {
	private int id;
	private String name;
	private LocalDate birthdate;
	private int semester;
	
	private int centralIdentifier;
	private int usedFreeSemester;
}
