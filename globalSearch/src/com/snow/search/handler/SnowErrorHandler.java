package com.snow.search.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snow.search.dto.ErrorhandlerDTO;
import com.snow.search.dto.MessageDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.util.SnowPropertiesUtil;

public class SnowErrorHandler {

	final static Logger LOG = Logger.getLogger(SnowErrorHandler.class);


	public List<ErrorhandlerDTO> errorHandling(RequestDTO requestDTO) throws IOException {
		String queryParam = requestDTO.getQueryParam();
		List<String> userRole = requestDTO.getUserRoles();
		List<String> searchType = requestDTO.getSearchType();
		Integer maxRows = requestDTO.getMaxRows();
		List<ErrorhandlerDTO> errorhandlerDTOs = new ArrayList<ErrorhandlerDTO>();
		Properties values = SnowPropertiesUtil.getPropertyValues();
		if (queryParam.equalsIgnoreCase("") || queryParam == null || queryParam.isEmpty() || queryParam.length() == 0
				|| queryParam.equalsIgnoreCase(" ")) {
			ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();
			errorhandling.setErrorMessage(values.getProperty("errorMessage.queryParam"));
			errorhandling.setErrorCode(values.getProperty("errorcode.queryParam"));
			errorhandlerDTOs.add(errorhandling);
		}
		if (userRole == null || userRole.isEmpty() || userRole.size() == 0 || userRole.contains(" ")) {
			ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();
			errorhandling.setErrorMessage(values.getProperty("errorMessage.userRole"));
			errorhandling.setErrorCode(values.getProperty("errorcode.userRole"));

			errorhandlerDTOs.add(errorhandling);
		}
		if (searchType == null || searchType.isEmpty() || searchType.size() == 0) {
			ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();

			errorhandling.setErrorMessage(values.getProperty("errorMessage.searchType"));
			errorhandling.setErrorCode(values.getProperty("errorcode.searchType"));

			errorhandlerDTOs.add(errorhandling);
		}
		if (requestDTO.getMode() != null) {
			ErrorhandlerDTO errorhandling = validateMode(requestDTO);
			if (errorhandling.getErrorCode() != null && errorhandling.getErrorMessage() != null) {
				errorhandlerDTOs.add(errorhandling);
			}
		}

		if (requestDTO.getMode() == null || requestDTO.getMode().isEmpty() || requestDTO.getMode().equalsIgnoreCase("")
				|| requestDTO.getMode().equalsIgnoreCase(" ")) {
			ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();
			errorhandling.setErrorMessage(values.getProperty("errorMessage.mode"));
			errorhandling.setErrorCode(values.getProperty("errorcode.mode"));
			errorhandlerDTOs.add(errorhandling);
		}

		if (searchType != null) {
			ErrorhandlerDTO errorhandling = searchtypeCheck(requestDTO);
			if (errorhandling.getErrorCode() != null && errorhandling.getErrorMessage() != null) {
				errorhandlerDTOs.add(errorhandling);
			}
		}
		if (maxRows == null) {
			requestDTO.setMaxRows(Integer.parseInt(values.getProperty("autoSuggest.facet.limit")));
		}
		return errorhandlerDTOs;
	}

	private ErrorhandlerDTO searchtypeCheck(RequestDTO requestDTO) throws IOException {
		Properties values = SnowPropertiesUtil.getPropertyValues();
		List<String> inputSearchType = requestDTO.getSearchType();
		ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();
		List<String> supportedSearchTypes = Arrays.asList(values.getProperty("supported.searchTypes").split(","));
		for (String string : inputSearchType) {
			if (supportedSearchTypes.contains(string.toUpperCase())) {
				continue;
			} else {
				errorhandling.setErrorMessage(values.getProperty("errorMessage.searchType"));
				errorhandling.setErrorCode(values.getProperty("errorcode.searchType"));
				break;

			}
		}
		return errorhandling;
	}

	private ErrorhandlerDTO validateMode(RequestDTO requestDTO) throws IOException {
		Properties values = SnowPropertiesUtil.getPropertyValues();
		String inputMode = requestDTO.getMode().toUpperCase();
		ErrorhandlerDTO errorhandling = new ErrorhandlerDTO();
		List<String> supportedModes = Arrays.asList(values.getProperty("supported.mode").split(","));
		if (supportedModes.contains(inputMode) == false) {
			errorhandling.setErrorMessage(values.getProperty("errorMessage.mode"));
			errorhandling.setErrorCode(values.getProperty("errorcode.mode"));
		}
		return errorhandling;
	}
	
	public String errorListResponse(List<ErrorhandlerDTO> errorhandlerDTOs, MessageDTO messageDTO)
			throws JsonProcessingException, JSONException {
		//implementation at the child class
		return null;
	}
}
