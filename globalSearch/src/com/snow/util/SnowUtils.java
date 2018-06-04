package com.snow.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SnowUtils {

	public static final Logger LOG = Logger.getLogger(SnowUtils.class);

	public static Properties prop = null;
	public static long lastModified = 0;

	/*public static Object convertJSONtoDTO(final String response, final Object objectToConvert) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response, objectToConvert.getClass());
	}*/

	public static Properties getPropertyValues() throws IOException {

		File file = new File(File.separator + "data" + File.separator + "snowsearch" + File.separator
				+ "searchConigurations" + File.separator + "config.properties");
		InputStream fis = null;
		String fileName = File.separator + "data" + File.separator + "snowsearch" + File.separator
				+ "searchConigurations" + File.separator + "config.properties" ;

		if (file.exists() && ((file.lastModified() != lastModified))) {

			fis = Files.newInputStream(Paths.get(fileName));
			prop = new Properties();
			prop.load(fis);
			lastModified = file.lastModified();
			if (fis != null)
				fis.close();
			return prop;
		} else if (file.exists() && ((file.lastModified() == lastModified))) {

			return prop;
		} else {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("config.properties");
			prop = new Properties();

			prop.load(inputStream);
			return prop;
		}

	}

}
