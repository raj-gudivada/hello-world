package com.snow.dih;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.DataImportHandlerException;
import org.apache.solr.handler.dataimport.URLDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom URL Data Source for supporting below parameters Basic Authentication
 * XML/JSON request & response content type Query Parameters rowLimit rowOffset
 * 
 * @author Ananth_Anthony
 *
 */
public class SnowURLDataSource extends URLDataSource {

	/**
	 * Constant for URL Method
	 */
	static final Pattern URIMETHOD = Pattern.compile("\\w{3,}:/");

	/**
	 * Constant for Basic Authentication
	 */
	public static final String BASIC_AUTH = "basicAuth";

	/**
	 * Constant for Record Limit
	 */
	public static final String ROW_LIMIT = "rowLimit";

	/**
	 * Constant for Record Limit
	 */
	public static final String DEFAULT_ROW_LIMIT = "10";

	/**
	 * Constant for Record Offset
	 */
	public static final String ROW_OFFSET = "rowOffset";

	/**
	 * Constant for Record Offset
	 */
	public static final String DEFAULT_ROW_OFFSET = "10";

	/**
	 * Constant for Request Header Content Type
	 */
	public static final String REQ_H_CONTENT_TYPE = "contentType";

	/**
	 * Constant for Request Header Accept
	 */
	public static final String REQ_H_ACCEPT = "accept";

	/**
	 * Constant for Query Param
	 */
	public static final String REQ_QUERY_PARAM = "queryParam";

	/**
	 * Variable for LOGGER
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Variable for Charset Pattern
	 */
	private static final Pattern CHARSET_PATTERN = Pattern.compile(".*?charset=(.*)$", 2);

	/**
	 * Variable for Query count
	 */
	static final ThreadLocal<AtomicLong> QUERY_COUNT = new ThreadLocal<AtomicLong>() {
		@Override
		protected AtomicLong initialValue() {
			return new AtomicLong();
		}
	};

	/**
	 * Variable for rowLimit
	 */
	private String rowLimit;

	/**
	 * Variable for rowOffset
	 */
	private String rowOffset;

	/**
	 * Variable for Query Param
	 */
	private String queryParam;

	/**
	 * Variable for baseURL
	 */
	private String contentType;

	/**
	 * Variable for accpetParam
	 */
	private String acceptParam;

	/**
	 * Variable for baseURL
	 */
	private String baseUrl;

	/**
	 * Variable for context
	 */
	private Context context;

	/**
	 * Variable for Intit Properties
	 */
	private Properties initProps;

	/**
	 * Variable for encoding
	 */
	private String encoding;

	/**
	 * Variable for Basic Authentication
	 */
	private String basicAuth;

	/**
	 * Variable for Connection timeout
	 */
	private int connectionTimeout = 5000;

	/**
	 * Variable for Read Timeout
	 */
	private int readTimeout = 10000;

	/**
	 * Getter for Row Limit
	 * 
	 * @return
	 */
	public String getRowLimit() {
		return rowLimit;
	}

	/**
	 * Getter for Row Offset
	 * 
	 * @return
	 */
	public String getRowOffset() {
		return rowOffset;
	}

	/**
	 * Getter for Query Param
	 * 
	 * @return
	 */
	public String getQueryParam() {
		return queryParam;
	}

	/**
	 * Getter for Connection Timeout
	 * 
	 * @return
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Setter for Connection Timeout
	 * 
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Getter for Read Timeout
	 * 
	 * @return
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Setter for Read Timeout
	 * 
	 * @return
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * Getter for Encoding
	 * 
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Setter for Encoding
	 * 
	 * @return
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Getter for Basic Authentication
	 * 
	 * @return
	 */
	public String getBasicAuth() {
		return basicAuth;
	}

	/**
	 * Setter for Basic Authentication
	 * 
	 * @return
	 */
	public void setBasicAuth(String basicAuth) {
		this.basicAuth = basicAuth;
	}

	/**
	 * Getter for Base URL
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Setter for Base URL
	 * 
	 * @return
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Getter for Context
	 * 
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Setter for Context
	 * 
	 * @return
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Getter for Init Properties
	 * 
	 * @return
	 */
	public Properties getInitProps() {
		return initProps;
	}

	/**
	 * Setter for Init Properties
	 * 
	 * @return
	 */
	public void setInitProps(Properties initProps) {
		this.initProps = initProps;
	}

	/**
	 * Getter for Content Type
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Getter for Accept Param
	 * 
	 * @return
	 */
	public String getAcceptParam() {
		return acceptParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.solr.handler.dataimport.URLDataSource#getData(java.lang.String)
	 */
	public Reader getData(String query) {
		URL url = null;
		String commonQueryString = "";
		LOG.debug("row limit " + rowLimit + " rowOffset " + rowOffset);
		if (StringUtils.isEmpty(rowLimit) && StringUtils.isEmpty(rowOffset)) {
			commonQueryString = "sysparm_display_value=" + true;
		} else {
			commonQueryString = "sysparm_limit=" + this.rowLimit + "&sysparm_offset=" + this.rowOffset
					+ "&sysparm_display_value=" + true;
		}

		LOG.debug("Inside getData(). query : " + query);

		try {

			if (null != this.queryParam && this.queryParam.length() > 0) {

				if (null != query && query.length() > 0) {
					LOG.debug("Appending QueryParam to Query");
					if (null != this.queryParam && this.queryParam.length() > 0) {
						query = query + "?" + this.queryParam;
					}
				} else {
					LOG.debug("Setting QueryParam to Query");
					if (null != this.queryParam && this.queryParam.length() > 0) {
						query = "?" + this.queryParam;
					}
				}
			} else {
				LOG.debug("Query Param is null or empty");
			}

			LOG.debug("updated query : " + query);
			LOG.debug("updated queryParam : " + this.queryParam);

			if (URIMETHOD.matcher(query).find()) {
				LOG.debug("URL Pattern Matched");
				if (query.contains("?")) {
					url = new URL(query + "&" + commonQueryString);
				} else {
					url = new URL(query + "?" + commonQueryString);
				}
			} else {
				if (query.contains("?")) {
					url = new URL(getBaseUrl() + query + "&" + commonQueryString);
				} else {
					url = new URL(getBaseUrl() + query + "?" + commonQueryString);
				}
			}

			LOG.debug("Accessing URL: " + url.toString());
			URLConnection e = url.openConnection();
			e.setConnectTimeout(getConnectionTimeout());
			e.setReadTimeout(getReadTimeout());

			LOG.debug("Setting Basic Auth :" + getBasicAuth());
			LOG.debug("Setting Content Type :" + getContentType());
			LOG.debug("Setting Accept Param :" + getAcceptParam());

			e.setRequestProperty("Authorization", getBasicAuth());
			e.setRequestProperty("content-type", getContentType());
			e.setRequestProperty("Accept", getAcceptParam());

			InputStream in = e.getInputStream();
			String enc = getEncoding();
			if (null == enc) {
				String cType = e.getContentType();
				if (null != cType) {
					Matcher m = CHARSET_PATTERN.matcher(cType);
					if (m.find()) {
						enc = m.group(1);
					}
				}
			}

			if (null == enc) {
				enc = UTF_8;
			}

			((AtomicLong) QUERY_COUNT.get()).incrementAndGet();
			InputStreamReader isr = new InputStreamReader(in, enc);

			return isr;

		} catch (Exception var8) {
			LOG.error("Exception thrown while getting data", var8);
			throw new DataImportHandlerException(500, "Exception in invoking url " + url, var8);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.solr.handler.dataimport.URLDataSource#init(org.apache.solr.handler
	 * .dataimport.Context, java.util.Properties)
	 */
	@Override
	public void init(Context context, Properties initProps) {
		this.context = context;
		this.initProps = initProps;

		baseUrl = getInitPropWithReplacements(BASE_URL);

		if (null != getInitPropWithReplacements(ENCODING))
			encoding = getInitPropWithReplacements(ENCODING);
		String cTimeout = getInitPropWithReplacements(CONNECTION_TIMEOUT_FIELD_NAME);
		String rTimeout = getInitPropWithReplacements(READ_TIMEOUT_FIELD_NAME);
		String contentType = getInitPropWithReplacements(REQ_H_CONTENT_TYPE);
		String acceptParam = getInitPropWithReplacements(REQ_H_ACCEPT);
		String queryParam = getInitPropWithReplacements(REQ_QUERY_PARAM);
		String rowLimit = getInitPropWithReplacements(ROW_LIMIT);
		String rowOffset = getInitPropWithReplacements(ROW_OFFSET);

		if (null != queryParam) {
			this.queryParam = queryParam;
		} else {
			this.queryParam = "";
		}

		if (null != rowLimit) {
			this.rowLimit = rowLimit;
		} else {
			this.rowLimit = "";
		}

		if (null != rowOffset) {
			this.rowOffset = rowOffset;
		} else {
			this.rowOffset = "";
		}

		if (null != contentType) {
			this.contentType = contentType;
		} else {
			this.contentType = "application/xml";
		}

		if (null != acceptParam) {
			this.acceptParam = acceptParam;
		} else {
			this.acceptParam = "application/xml";
		}

		if (null != cTimeout) {
			try {
				connectionTimeout = Integer.parseInt(cTimeout);
			} catch (NumberFormatException e) {
				LOG.warn("Invalid connection timeout: " + cTimeout);
			}
		}

		if (null != rTimeout) {
			try {
				readTimeout = Integer.parseInt(rTimeout);
			} catch (NumberFormatException e) {
				LOG.warn("Invalid read timeout: " + rTimeout);
			}
		}
		LOG.debug("Fetching Basic Auth credentials");
		String userInfo = getInitPropWithReplacements(BASIC_AUTH);
		if (null != userInfo) {
			Base64.Encoder encoder = Base64.getEncoder();
			try {
				this.basicAuth = "Basic " + encoder.encodeToString(userInfo.getBytes(UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("Encoding Error!");
			}

		} else {
			LOG.info("No Basic Auth Crendentials detected!");
		}
	}

	/**
	 * Get Method for Init Properties
	 * 
	 * @param propertyName
	 * @return String
	 */
	private String getInitPropWithReplacements(String propertyName) {
		final String expr = initProps.getProperty(propertyName);
		if (null == expr) {
			return null;
		}
		return context.replaceTokens(expr);
	}

}
