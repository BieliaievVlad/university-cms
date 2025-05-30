package ua.foxminded.tasks.university_cms.form;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogsFormData {
	
	private List<String> usernames = Collections.emptyList();
	private List<String> tableNames = Collections.emptyList();
	private List<String> operations = Collections.emptyList();
}
