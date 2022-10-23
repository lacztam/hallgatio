package hu.lacztam.hallgatoi.service;

import org.springframework.stereotype.Service;

import hu.lacztam.hallgatoi.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherService {

	private final TeacherRepository teacherResository;
	
	
	
}
