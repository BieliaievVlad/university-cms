package ua.foxminded.tasks.university_cms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {

	static Logger logger = LoggerFactory.getLogger(DBUtil.class);

	public static String readQueryFromFile(Path filePath) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String resourcePath = filePath.toString().replace("\\", "/");

		StringBuilder query = new StringBuilder();

		try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			if (inputStream == null) {
				logger.error("Resource not found: " + resourcePath);

			}

			String line;
			while ((line = reader.readLine()) != null) {
				query.append(line).append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
		return query.toString();
	}

}
