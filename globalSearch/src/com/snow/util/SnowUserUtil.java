package com.snow.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.snow.search.dto.AttributesDTO;
import com.snow.search.dto.RequestDTO;

public class SnowUserUtil {

	public final static Logger LOG = Logger.getLogger(SnowUserUtil.class);

	public String fetchUserRoles(RequestDTO requestDTO) throws IOException {
		List<String> searchType = requestDTO.getSearchType();
		String roles = null;
		String response = null;

		Properties values = SnowPropertiesUtil.getPropertyValues();
		if (searchType.contains("LOC") || searchType.contains("UD") || searchType.contains("APP")) {
			roles = values.getProperty("ud.loc.all.roles");
		} else {
			roles = values.getProperty("acl.all.roles");
		}
		List<String> userRoles = requestDTO.getUserRoles();
		if (userRoles.isEmpty()
				|| (!userRoles.isEmpty() && userRoles.size() == 1 && userRoles.get(0).trim().equals(""))) {
			response = null;
		} else {
			String userRole = roles + ":(";
			String appender = " OR ";
			if (!userRoles.isEmpty() && userRoles.size() > 1) {
				for (String role : userRoles) {
					userRole = userRole + "\"" + role + "\"";
					userRole = userRole + appender;
				}
				userRole = userRole.substring(0, userRole.length() - 4);
			} else if (!userRoles.isEmpty() && userRoles.size() == 1) {
				userRole = userRole + "\"" + userRoles + "\"";
			}
			userRole = userRole + ")";
			response = userRole;
		}

		return response;
	}

	public AttributesDTO fetchSearchTypeUserFilter(List<String> searchTypes, RequestDTO requestDTO) throws IOException {

		AttributesDTO attributes = new AttributesDTO();
		Properties values = SnowPropertiesUtil.getPropertyValues();
		StringBuffer sb = new StringBuffer();
		
		for (String multiSearchType : searchTypes) {
			sb.append("\"" + multiSearchType + "\"" + ",");
		}
		
		if (searchTypes.contains("LOC") || searchTypes.contains("UD")) {
			attributes.setSpecialUser("");
		} else {
			attributes.setSpecialUser(fetchUserRoles(requestDTO));
		}
		
		attributes.setSpecialUser(fetchUserRoles(requestDTO));
		String finalFilterType = sb.substring(0, sb.length() - 1);
		
		for (String searchValue : searchTypes) {
			if (searchValue.isEmpty() | searchValue.equalsIgnoreCase("NULL") | searchValue.equalsIgnoreCase("NONE")
					| searchValue.equalsIgnoreCase("ALL")) {
				attributes.setFilterType(values.getProperty("filter.type.all") + ":*)");
			}
			attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "(" + finalFilterType + ")");
			String specialUser = fetchUserRoles(requestDTO);
			attributes.setSpecialUser(specialUser);
			attributes.setBoostField(values.getProperty("all.boosting.order"));
		}

		return attributes;

	}

}