package ua.foxminded.tasks.university_cms.service;

import static ua.foxminded.tasks.university_cms.util.Constants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ua.foxminded.tasks.university_cms.util.DBUtil;

@Service
public class DataGeneratorService {

	private final JdbcTemplate jdbcTemplate;
	Logger logger = LoggerFactory.getLogger(DataGeneratorService.class);

	private String createTables;
	private String generateGroups;
	private String generateStudents;
	private String generateGroupsCourses;
	private String dropTempTables;
	private String updateGroups;
	private String generateTeachers;
	private String generateTeachersCourses;
	private String generateSchedule;
	private String generateUsers;
	private String generateRoles;

	@Autowired
	public DataGeneratorService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void generateData() {

		if (isDatabaseEmpty()) {

			prepareSqlQuery();

			executeSqlQuery(createTables);
			executeSqlQuery(generateUsers);
			executeSqlQuery(generateRoles);
			executeSqlQuery(generateGroups);
			executeSqlQuery(generateStudents);
			executeSqlQuery(generateGroupsCourses);
			executeSqlQuery(updateGroups);
			executeSqlQuery(generateTeachers);
			executeSqlQuery(generateTeachersCourses);
			executeSqlQuery(generateSchedule);
			executeSqlQuery(dropTempTables);

			logger.info("Data successfully generated");

		} else {

			logger.info("Database is not empty. Data generation skipped");
		}
	}

	private void prepareSqlQuery() {

		createTables = DBUtil.readQueryFromFile(CREATE_TABLES);
		generateGroups = DBUtil.readQueryFromFile(GENERATE_GROUPS);
		generateStudents = DBUtil.readQueryFromFile(GENERATE_STUDENTS);
		generateGroupsCourses = DBUtil.readQueryFromFile(GENERATE_GROUPS_COURSES);
		updateGroups = DBUtil.readQueryFromFile(UPDATE_GROUPS);
		dropTempTables = DBUtil.readQueryFromFile(DROP_TEMP_TABLES);
		generateTeachers = DBUtil.readQueryFromFile(GENERATE_TEACHERS);
		generateTeachersCourses = DBUtil.readQueryFromFile(GENERATE_TEACHERS_COURSES);
		generateSchedule = DBUtil.readQueryFromFile(GENERATE_SCHEDULE);
		generateUsers = DBUtil.readQueryFromFile(GENERATE_USERS);
		generateRoles = DBUtil.readQueryFromFile(GENERATE_ROLES);
	}

	private void executeSqlQuery(String sql) {

		try {
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			logger.error("Error executing SQL query: {%s}, %s", sql, e);
		}
	}

	private boolean isDatabaseEmpty() {

		String sql = "SELECT COUNT(*) FROM university.students";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count == null || count == 0;
	}

}
