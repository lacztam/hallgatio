package hu.lacztam.hallgatoi.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import hu.lacztam.hallgatoi.api.TeacherControllerApi;
import hu.lacztam.hallgatoi.api.model.TeacherDto;
import hu.lacztam.hallgatoi.mapper.TeacherMapper;
import hu.lacztam.hallgatoi.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeacherController implements TeacherControllerApi{
	
	private final TeacherRepository teacherRepository;
	private final TeacherMapper teacherMapper;
	private final NativeWebRequest nativeWebRequest;
	
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.of(nativeWebRequest);
	}

	@Override
	public ResponseEntity<TeacherDto> findById(Integer id) {
		return ResponseEntity.ok(
				teacherMapper.teacherToDto(
						teacherRepository.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
	}
}
