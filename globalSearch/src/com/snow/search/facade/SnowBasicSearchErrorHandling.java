package com.snow.search.facade;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.search.dto.ErrorhandlerDTO;
import com.snow.search.dto.MessageDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.util.SnowUtils;

public class SnowBasicSearchErrorHandling extends SnowErrorHandling {
	final static Logger LOGGER = Logger.getLogger(SnowBasicSearchErrorHandling.class);

	/*public ErrorhandlerDTO basicSearchFacetErrorHandling(RequestDTO requestDTO) throws IOException {
		ErrorhandlerDTO errorhandling = null;
		SnowUtils snowSearchUtils = new SnowUtils();
		List<String> facetSelection = requestDTO.getFacetSelection();
		Properties values = snowSearchUtils.getPropertyValues();
		if (facetSelection == null || facetSelection.isEmpty() || facetSelection.size() == 0
				|| facetSelection.contains(" ") || facetSelection.contains("")) {
			errorhandling = new ErrorhandlerDTO();
			errorhandling.setErrorMessage(values.getProperty("errorMessage.facetSelection"));
			errorhandling.setErrorCode(values.getProperty("errorcode.facetSelection"));
		}
		// }
		return errorhandling;
	}*/

	public String errorListResponse(List<ErrorhandlerDTO> errorhandlerDTOs, MessageDTO messageDTO)
			throws JsonProcessingException, JSONException {
		JSONArray list = new JSONArray();
		JSONObject responseObject = new JSONObject();
		for (ErrorhandlerDTO errorhandlerDTO : errorhandlerDTOs) {
			JSONObject errorObject = new JSONObject();
			errorObject.put("errorCode", errorhandlerDTO.getErrorCode());
			errorObject.put("errorMessage", errorhandlerDTO.getErrorMessage());
			list.put(errorObject);
		}
		responseObject.accumulate("errorList", list);
		responseObject.accumulate("responseCode", messageDTO.getResponseCode());
		responseObject.accumulate("responseMessage", messageDTO.getResponseMessage());
		return responseObject.toString(2);
	}

}
