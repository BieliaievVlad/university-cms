package ua.foxminded.tasks.university_cms.db.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import ua.foxminded.tasks.university_cms.db.entity.Schedule;
import ua.foxminded.tasks.university_cms.db.repository.ScheduleRepository;

@Service
public class ScheduleService {
	
	private final ScheduleRepository repository;
	
	@Autowired
	public ScheduleService(ScheduleRepository repository) {
		this.repository = repository;
	}
	
	public List<Schedule> findAll() {
		return repository.findAll();
	}
	
	public Schedule findById(Long searchId) {
		
		Optional<Schedule> optSсhedule = repository.findById(searchId);
		
		if (optSсhedule.isPresent()) {
			return optSсhedule.get();
			
		} else {
			throw new EntityNotFoundException("Sсhedule is not found.");
		}
	}
	
	public void save(Schedule schedule) {
		
		if (!isSсheduleValid(schedule)) {
			throw new IllegalArgumentException("Sсhedule is not valid.");
		}
		repository.save(schedule);
	}
	
	public void delete(Long sсheduleId) {
		
		Optional<Schedule> optSсhedule = repository.findById(sсheduleId);
		
		if (optSсhedule.isPresent()) {
			
			repository.delete(optSсhedule.get());
			
		} else {
			throw new EntityNotFoundException("Shedule is not found.");
		}
	}
	
	private boolean isSсheduleValid(Schedule schedule) {
		return schedule != null &&
				schedule.getId() != null &&
				schedule.getDateTime() != null;
	}

}
