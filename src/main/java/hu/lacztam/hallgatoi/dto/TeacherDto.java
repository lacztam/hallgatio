package hu.lacztam.hallgatoi.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDto {
	private int id;
	private String name;
	private LocalDate birthdate;
}
