package com.snow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SnowUtils {

	public static final Logger LOG = Logger.getLogger(SnowUtils.class);

	public static Properties prop = null;
	public static long lastModified = 0;

	public static Object convertJSONtoDTO(final String response, final Object objectToConvert) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response, objectToConvert.getClass());
	}

	public static Properties getPropertyValues() throws IOException {

		File file = new File(File.separator + "data" + File.separator + "snowsearch" + File.separator
				+ "searchConigurations" + File.separator + "config.properties");
		InputStream fis = null;

		if (file.exists() && ((file.lastModified() != lastModified))) {

			fis = new FileInputStream(file);
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
