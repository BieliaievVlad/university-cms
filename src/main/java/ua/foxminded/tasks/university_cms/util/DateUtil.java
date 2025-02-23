package ua.foxminded.tasks.university_cms.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	
    private LocalDate getStartOfWeek() {
        LocalDate now = LocalDate.now();
        return now.with(DayOfWeek.MONDAY);
    }

    private LocalDate getEndOfWeek() {
        LocalDate now = LocalDate.now();
        return now.with(DayOfWeek.SUNDAY);
    }
    
    public List<LocalDate> getDateListOfWeek() {
    	
    	LocalDate startOfWeek = getStartOfWeek();
    	LocalDate endOfWeek = getEndOfWeek();
    	
        List<LocalDate> dateList = new ArrayList<>();

        LocalDate currentDate = startOfWeek;
        while (!currentDate.isAfter(endOfWeek)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dateList;
    }
    
    public List<LocalDate> getDateListOfInterval(LocalDate start, LocalDate end) {
    		
        	List<LocalDate> dateList = new ArrayList<>();
        	LocalDate currentDate = start;
        	while(!currentDate.isAfter(end)) {
        		dateList.add(currentDate);
        		currentDate = currentDate.plusDays(1);
        	}
        	return dateList;
    }

}
