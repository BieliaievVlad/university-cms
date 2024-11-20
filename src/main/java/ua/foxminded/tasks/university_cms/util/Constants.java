package ua.foxminded.tasks.university_cms.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

	public static final Path DROP_TEMP_TABLES = Paths.get("sql", "testDataGeneration", "drop_temp_tables.sql");
	public static final Path CREATE_TABLES = Paths.get("sql", "testDataGeneration", "create_tables.sql");
	public static final Path GENERATE_GROUPS = Paths.get("sql", "testDataGeneration", "generate_groups.sql");
	public static final Path GENERATE_STUDENTS = Paths.get("sql", "testDataGeneration", "generate_students.sql");
	public static final Path GENERATE_STUDENTS_COURSES = Paths.get("sql", "testDataGeneration",
			"generate_students_courses.sql");
	public static final Path UPDATE_GROUPS = Paths.get("sql", "testDataGeneration", "update_groups_table.sql");
	public static final Path GENERATE_TEACHERS = Paths.get("sql", "testDataGeneration", "generate_teachers.sql");
	public static final Path GENERATE_TEACHERS_COURSES = Paths.get("sql", "testDataGeneration",
			"generate_teachers_courses.sql");
	public static final Path GENERATE_SCHEDULE = Paths.get("sql", "testDataGeneration", "generate_schedule.sql");

}
