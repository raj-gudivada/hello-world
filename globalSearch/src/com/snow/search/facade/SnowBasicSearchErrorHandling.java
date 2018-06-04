package com.snow.search.facade;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.search.dto.ErrorhandlerDTO;
import com.snow.search.dto.MessageDTO;

public class SnowBasicSearchErrorHandling extends SnowErrorHandling {

	final static Logger LOG = Logger.getLogger(SnowBasicSearchErrorHandling.class);

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
