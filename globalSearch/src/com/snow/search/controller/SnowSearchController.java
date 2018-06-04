package com.snow.search.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snow.search.dto.AttributesDTO;
import com.snow.search.dto.ErrorhandlerDTO;
import com.snow.search.dto.MessageDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.search.facade.SnowAutoSuggestFacade;
import com.snow.search.facade.SnowBasicSearchErrorHandling;
import com.snow.search.facade.SnowDidYouMean;
import com.snow.search.facade.SnowErrorHandling;
import com.snow.search.facade.SnowSearchFacade;
import com.snow.util.SnowUtils;

@RestController
public class SnowSearchController {
	final static Logger logger = Logger.getLogger(SnowSearchController.class);
	SnowUtils snowSearchUtils= new SnowUtils();

	@RequestMapping(value = "/basicSearch", method = RequestMethod.POST,consumes="application/json")
	public String basicSolrSearchPost(@RequestBody RequestDTO requestDTO){
		String queryResponse=null;

		try{// SnowUtils snowUtils= new SnowUtils();
			//snowUtils.isUserAuthenticated(authCredentials);
			/*if(!isUserAuthenticated(authCredentials)){
			return "{\"error\":\"User not authenticated\"}";
		}  */

			//isUserAuthenticated(authCredentials);

			AttributesDTO attributes=null;
			//Error Handling
			SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
			List<ErrorhandlerDTO> errorhandlerDTOs=basicSearchErrorHandling.errorHandling(requestDTO);
			// Get an iterator.
			Iterator<ErrorhandlerDTO> ite = errorhandlerDTOs.iterator();

			/* Remove the second value of the list, while iterating over its elements,
			 * using the iterator's remove method. */
			while(ite.hasNext()) {

				ErrorhandlerDTO value = ite.next();
				if(value.getErrorCode().equalsIgnoreCase("106"))
					ite.remove();
			}
			/*ErrorhandlerDTO facetErroHandlerDTO=basicSearchErrorHandling.basicSearchFacetErrorHandling(requestDTO);
			if(facetErroHandlerDTO!=null){
				errorhandlerDTOs.add(facetErroHandlerDTO);

			}*/
			if (errorhandlerDTOs.size()!=0||!errorhandlerDTOs.isEmpty()) {
				MessageDTO messageDTO = SetResponseDTO();

				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);

			}
			else{
				SnowSearchFacade searchFacade= new SnowSearchFacade();
				String user=searchFacade.fetchUserRoles(requestDTO);
				attributes= searchFacade.fetchAll(requestDTO);
				queryResponse= searchFacade.getQuerySearch(requestDTO,user,attributes);
				//DidyouMean
				SnowDidYouMean didYouMean=new SnowDidYouMean();
				if(didYouMean.getNumFound(queryResponse).equalsIgnoreCase("0")){
					//System.out.println("here");
					queryResponse=didYouMean.spellcheck(requestDTO.getQueryParam());
				}
				logger.info("final Response:" +queryResponse);
			}
		}
		catch(Exception  e){
			logger.error("Some error occured ! Please check.",e);
			logger.error(e.getStackTrace());
			SnowUtils snowUtils=new SnowUtils();
			try {
				Properties values= snowUtils.getPropertyValues();


				MessageDTO messageDTO=new MessageDTO();
				// values=new Properties();
				messageDTO.setResponseCode(values.getProperty("response.error.responseCode"));
				messageDTO.setResponseMessage(values.getProperty("response.error.responseMessage"));
				List<ErrorhandlerDTO> errorhandlerDTOs=new ArrayList<ErrorhandlerDTO>();
				ErrorhandlerDTO errorhandlerDTO=new ErrorhandlerDTO();
				errorhandlerDTO.setErrorCode(values.getProperty("errorcode.exception"));
				errorhandlerDTO.setErrorMessage(values.getProperty("errorMessage.exception"));
				errorhandlerDTOs.add(errorhandlerDTO);

				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();


				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);
			} catch (JSONException| IOException e1) {
				// TODO Auto-generated catch block
				logger.error("Some error occured ! Please check.",e);
				queryResponse="Some Internal Error Occured !";
				e1.printStackTrace();
			} 
		}

		return queryResponse;
	}

	private MessageDTO SetResponseDTO() throws IOException {
		MessageDTO messageDTO=new MessageDTO();
		SnowUtils snowUtils=new SnowUtils();
		Properties values=snowUtils.getPropertyValues();
		messageDTO.setResponseCode(values.getProperty("response.success.responseCode"));
		messageDTO.setResponseMessage(values.getProperty("response.success.responseMessage"));
		return messageDTO;
	}

	public boolean isUserAuthenticated(String authCredentials) throws IOException {
		Properties values= snowSearchUtils.getPropertyValues();
		String userName = new String( Base64.getDecoder().decode(values.getProperty("serviceNow.user.name")),"UTF-8");
		String password = new String(Base64.getDecoder().decode(values.getProperty("serviceNow.user.password")),"UTF-8");
		String userNameAndPassword = userName+":"+password;
		String encodedCredentials = Base64.getEncoder().encodeToString(userNameAndPassword.getBytes(StandardCharsets.UTF_8));
		String finalCredentials = "Basic "+encodedCredentials;
		System.out.println("abzs:"+finalCredentials);
		if(finalCredentials.equals(authCredentials)) {
			return true;
		}
		if (null == authCredentials) {
			return false;
		}
		return false;
	}
	@RequestMapping(value = "/basicSearch", method = RequestMethod.GET,produces="application/json")
	public String basicSolrSearchGet(@RequestParam String queryParam,@RequestParam Integer start,@RequestParam Integer rows,@RequestParam List<String> userRoles,@RequestParam List<String> searchType,@RequestParam List<String> facetSelection){
		String queryResponse=null;

		try{
			SnowSearchFacade searchFacade= new SnowSearchFacade();
			RequestDTO requestDTO= new RequestDTO();
			requestDTO.setQueryParam(queryParam);
			requestDTO.setRows(rows);
			requestDTO.setStart(start);
			requestDTO.setSearchType(searchType);
			requestDTO.setUserRoles(userRoles);
			requestDTO.setFacetSelection(facetSelection);
			String user=searchFacade.fetchUserRoles(requestDTO);
			//String queryResponse=null;
			AttributesDTO attributes=null;

			//Error Handling
			SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
			List<ErrorhandlerDTO> errorhandlerDTOs=basicSearchErrorHandling.errorHandling(requestDTO);
			Iterator<ErrorhandlerDTO> ite = errorhandlerDTOs.iterator();
			while(ite.hasNext()) {
				ErrorhandlerDTO value = ite.next();
				if(value.getErrorCode().equalsIgnoreCase("106"))
					ite.remove();
			}
			/*ErrorhandlerDTO facetErroHandlerDTO=basicSearchErrorHandling.basicSearchFacetErrorHandling(requestDTO);
			if(facetErroHandlerDTO!=null){
				errorhandlerDTOs.add(facetErroHandlerDTO);
			}*/
			if (errorhandlerDTOs.size()!=0||!errorhandlerDTOs.isEmpty()) {
				//SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
				MessageDTO messageDTO = SetResponseDTO();

				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);
			}
			else{
				attributes= searchFacade.fetchAll(requestDTO);
				queryResponse= searchFacade.getQuerySearch(requestDTO,user,attributes);
				logger.info("final Response:" +queryResponse);
			}	
		}
		catch(Exception e){
			logger.error("Some error occured ! Please check.",e);
			logger.error(e.getStackTrace());
			SnowUtils snowUtils=new SnowUtils();
			try {
				Properties values= snowUtils.getPropertyValues();
				MessageDTO messageDTO=new MessageDTO();
				// values=new Properties();
				messageDTO.setResponseCode(values.getProperty("response.error.responseCode"));
				messageDTO.setResponseMessage(values.getProperty("response.error.responseMessage"));
				List<ErrorhandlerDTO> errorhandlerDTOs=new ArrayList<ErrorhandlerDTO>();
				ErrorhandlerDTO errorhandlerDTO=new ErrorhandlerDTO();
				errorhandlerDTO.setErrorCode(values.getProperty("errorcode.exception"));
				errorhandlerDTO.setErrorMessage(values.getProperty("errorMessage.exception"));
				errorhandlerDTOs.add(errorhandlerDTO);
				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);
			} catch (JSONException| IOException e1) {
				// TODO Auto-generated catch block
				logger.error("Some error occured ! Please check.",e);
				queryResponse="Some Internal Error Occured !";
				e1.printStackTrace();
			} 
		}
		return queryResponse;
	}

	//AutoSuggestTerms Service
	@RequestMapping(value = "/autoSuggestTerms", method = RequestMethod.POST,consumes="application/json")
	public String autoSuggestSolrSearchPost(@RequestBody RequestDTO requestDTO){
		SnowAutoSuggestFacade snowAutoSuggestFacade= new SnowAutoSuggestFacade();
		String queryResponse=null;
		try{
			//Error Handling

			SnowErrorHandling errorHandling=new SnowErrorHandling();
			List<ErrorhandlerDTO> errorhandlerDTOs=errorHandling.errorHandling(requestDTO);
			if (errorhandlerDTOs.size()!=0||!errorhandlerDTOs.isEmpty()) {
				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
				MessageDTO messageDTO = SetResponseDTO();

				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);

			}		
			else{
				//AutoFill module
				if(requestDTO.getMode()!=null &&requestDTO.getMode().equalsIgnoreCase("AutoSuggest")) {
					AttributesDTO attributes=null;
					attributes = snowAutoSuggestFacade.fetchAutoFillFields(requestDTO);
					queryResponse= snowAutoSuggestFacade.getAutoQuerySearch(requestDTO,attributes);
				} 
				if (requestDTO.getMode()!=null &&requestDTO.getMode().equalsIgnoreCase("AutoSuggestTerms")) {
					AttributesDTO attributes=null;
					attributes = snowAutoSuggestFacade.fetchAutoSuggestTerms(requestDTO);
					queryResponse=snowAutoSuggestFacade.autoSuggestTermsQuerySearch(requestDTO,attributes);
				}
			}
		}
		catch(Exception e){
			logger.error("Some error occured ! Please check.",e);
			logger.error(e.getStackTrace());
			SnowUtils snowUtils=new SnowUtils();
			try {
				Properties values= snowUtils.getPropertyValues();


				MessageDTO messageDTO=new MessageDTO();
				// values=new Properties();
				messageDTO.setResponseCode(values.getProperty("response.error.responseCode"));
				messageDTO.setResponseMessage(values.getProperty("response.error.responseMessage"));
				List<ErrorhandlerDTO> errorhandlerDTOs=new ArrayList<ErrorhandlerDTO>();
				ErrorhandlerDTO errorhandlerDTO=new ErrorhandlerDTO();
				errorhandlerDTO.setErrorCode(values.getProperty("errorcode.exception"));
				errorhandlerDTO.setErrorMessage(values.getProperty("errorMessage.exception"));
				errorhandlerDTOs.add(errorhandlerDTO);

				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();


				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);
			} catch (JSONException| IOException e1) {
				// TODO Auto-generated catch block
				logger.error("Some error occured ! Please check.",e);
				queryResponse="Some Internal Error Occured !";
				e1.printStackTrace();
			} 
		}


		return queryResponse; 
	}

	@RequestMapping(value = "/autoSuggestTerms", method = RequestMethod.GET,consumes="application/json")
	public String autoSuggestSolrSearchGet(@RequestParam String queryParam, @RequestParam Integer maxRows,@RequestParam List<String> userRoles,@RequestParam List<String> searchType,@RequestParam String mode ) throws IOException {
		SnowAutoSuggestFacade snowAutoSuggestFacade= new SnowAutoSuggestFacade();
		RequestDTO requestDTO= new RequestDTO();
		requestDTO.setQueryParam(queryParam);
		requestDTO.setMaxRows(maxRows);
		requestDTO.setMode(mode);
		requestDTO.setUserRoles(userRoles);
		requestDTO.setSearchType(searchType);
		String queryResponse=null;
		try{
			//Error Handling

			SnowErrorHandling errorHandling=new SnowErrorHandling();
			List<ErrorhandlerDTO> errorhandlerDTOs=errorHandling.errorHandling(requestDTO);
			if (errorhandlerDTOs.size()!=0||!errorhandlerDTOs.isEmpty()) {
				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();
				MessageDTO messageDTO = SetResponseDTO();
				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);

			}		
			else{
				//AutoFill module

				if(requestDTO.getMode()!=null &&requestDTO.getMode().equalsIgnoreCase("AutoSuggest")) {
					AttributesDTO attributes=null;
					attributes = snowAutoSuggestFacade.fetchAutoFillFields(requestDTO);
					queryResponse= snowAutoSuggestFacade.getAutoQuerySearch(requestDTO,attributes);
				} 
				if (requestDTO.getMode()!=null &&requestDTO.getMode().equalsIgnoreCase("AutoSuggestTerms")) {
					AttributesDTO attributes=null;
					attributes = snowAutoSuggestFacade.fetchAutoSuggestTerms(requestDTO);
					queryResponse=snowAutoSuggestFacade.autoSuggestTermsQuerySearch(requestDTO,attributes);
				}
			}
		}
		catch(Exception e){
			logger.error("Some error occured ! Please check.",e);
			logger.error(e.getStackTrace());
			SnowUtils snowUtils=new SnowUtils();
			try {
				Properties values= snowUtils.getPropertyValues();


				MessageDTO messageDTO=new MessageDTO();
				// values=new Properties();
				messageDTO.setResponseCode(values.getProperty("response.error.responseCode"));
				messageDTO.setResponseMessage(values.getProperty("response.error.responseMessage"));
				List<ErrorhandlerDTO> errorhandlerDTOs=new ArrayList<ErrorhandlerDTO>();
				ErrorhandlerDTO errorhandlerDTO=new ErrorhandlerDTO();
				errorhandlerDTO.setErrorCode(values.getProperty("errorcode.exception"));
				errorhandlerDTO.setErrorMessage(values.getProperty("errorMessage.exception"));
				errorhandlerDTOs.add(errorhandlerDTO);

				SnowBasicSearchErrorHandling basicSearchErrorHandling=new SnowBasicSearchErrorHandling();


				queryResponse=basicSearchErrorHandling.errorListResponse(errorhandlerDTOs,messageDTO);
			} catch (JSONException| IOException e1) {
				// TODO Auto-generated catch block
				logger.error("Some error occured ! Please check.",e);
				queryResponse="Some Internal Error Occured !";
				e1.printStackTrace();
			} 
		}

		return queryResponse; 

	}
	/*@RequestMapping(value = "/advancedSearch", method = RequestMethod.POST,consumes="application/json")
 public String solrAdvancedSearch(@RequestBody AdvancedRequestDTO advancedRequestDTO) throws Exception{
	 SnowAdvancedSearchFacade advancedSearchFacade= new SnowAdvancedSearchFacade();
	 String user= advancedSearchFacade.fetchAdvancedUserRoles(advancedRequestDTO);
	 AttributesDTO attributes= advancedSearchFacade.fetchAll(advancedRequestDTO);
	 String queryResponse=advancedSearchFacade.getQuerySearch(advancedRequestDTO, user, attributes);
	 return queryResponse;

 }*/



	/* @RequestMapping(value = "/basicSearch", method = RequestMethod.POST,consumes="application/json")
public CommonSearchResponseDTO basicSolrSearchPost(@RequestBody RequestDTO requestDTO) throws Exception {
	 SnowSearchFacade searchFacade= new SnowSearchFacade();
	 String user=searchFacade.fetchUserRoles(requestDTO);
	 AttributesDTO attributes= searchFacade.fetchFacets(requestDTO);
	 return getConvertedObject(requestDTO.getQueryParam(),requestDTO.getStart(),requestDTO.getRows(),user,attributes);
 }*/

	/*private CommonSearchResponseDTO getConvertedObject(String queryParam, Integer start, Integer rows,String user,AttributesDTO attributes) throws Exception {
		SolrjClientConnectService solrjClientConnectService= new SolrjClientConnectService();
		CommonSearchResponseDTO commonSnowSearchDTO= new CommonSearchResponseDTO();
		CommonSearchResponseDTO convertedObj = null;
		 try {
	 	 String queryResponse=solrjClientConnectService.querySearch(queryParam,start,rows,user,attributes);
		 convertedObj = (CommonSearchResponseDTO)SnowSearchUtils.convertJSONtoDTO(queryResponse, commonSnowSearchDTO);
		 }
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return  convertedObj;
	}*/
	/* private CommonSearchResponseDTO getAdvancedSearchConvertedObject() throws Exception {
		SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
		 CommonSearchResponseDTO advancedSearchDTO= new CommonSearchResponseDTO();
		 CommonSearchResponseDTO convertedObj = null;
		 try {
			 String solrDocumentList =solrjClientConnectService.search();
			 convertedObj = (CommonSearchResponseDTO)SnowSearchUtils.convertJSONtoDTO(solrDocumentList, advancedSearchDTO);
			 logger.debug("test log:" +convertedObj );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  convertedObj;
	 }*/

	/* @RequestMapping(value = "/search/{name}", method = RequestMethod.GET,produces="application/json")
 public SnowSearchDTO solrSearchList(@PathVariable String name) throws Exception {
	 return getConvertedObject();
 }
 private SnowSearchDTO getConvertedObject() throws Exception {
	SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
	 SnowSearchDTO snowSearchDTO= new SnowSearchDTO();
	 SnowSearchDTO convertedObj = null;
	 try {
		 String solrDocumentList =solrjClientConnectService.search();
		 convertedObj = (SnowSearchDTO)SnowSearchUtils.convertJSONtoDTO(solrDocumentList, snowSearchDTO);
		 logger.debug("test log:" +convertedObj );
		System.out.println(convertedObj);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SolrServerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return  convertedObj;
 }*/

	/* @RequestMapping(value = "/{name}", method = RequestMethod.GET)
 public SnowSearchDTO hello(@PathVariable String name) {
 //String result="Hello "+name; 
 SnowSearchDTO snowSearchDTO= new SnowSearchDTO();
 //snowSearchDTO.setId("123");
// snowSearchDTO.setDescription("abc");
 return snowSearchDTO;
 }*/

	/* @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
 public SnowSearchDTO solrSearch(@PathVariable String name) {
	 SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
	 SnowSearchDTO snowSearchDTO= new SnowSearchDTO();
	 try {
		 QueryResponse SolrDocumentList =solrjClientConnectService.search();
		 snowSearchDTO.setDescription(SolrDocumentList.get(0).toString()); 
	//	 snowSearchDTO.setId("456");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SolrServerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return snowSearchDTO;
	 //
 }
	 */

	/*@RequestMapping(value = "/basicSearch", method = RequestMethod.POST,consumes="application/json")
 public BasicSearchResponseDTO basicSolrSearchPost(@RequestBody RequestDTO requestDTO) throws Exception {
	 SnowSearchFacade searchFacade= new SnowSearchFacade();
	 String user=searchFacade.fetchUserRoles(requestDTO);
	 String queryResponse=null;
	 AttributesDTO attributes=null;
	//AutoFill module

		attributes= searchFacade.fetchAll(requestDTO);
	    queryResponse= searchFacade.getQuerySearch(requestDTO,user,attributes);
	    BasicSearchResponseDTO basicSearchResponseDTO = new BasicSearchResponseDTO();
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {

	        JsonNode node = objectMapper.readValue(queryResponse, JsonNode.class);
	        JsonNode headerNode = node.get("responseHeader");
	        String headerValue=headerNode.toString();
	        JsonNode responseNode = node.get("response");
	        String responseNodeValue= responseNode.toString();

	        ResponseHeaderDTO headerDto= new ResponseHeaderDTO();
	        ResponseHeaderDTO   convertedObj = (ResponseHeaderDTO)SnowSearchUtils.convertJSONtoDTO(headerValue, headerDto);
	        convertedObj.setResponseCode("200");
	        convertedObj.setResponseMessage("Success");

	        basicSearchResponseDTO.setResponseHeaderDTO(convertedObj);
	        basicSearchResponseDTO.setResponse(responseNodeValue);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return basicSearchResponseDTO;
  }*/



}