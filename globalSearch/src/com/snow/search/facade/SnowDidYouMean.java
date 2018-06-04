package com.snow.search.facade;

import java.io.IOException;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.search.service.SolrjClientConnectService;
import com.snow.util.SnowUtils;

public class SnowDidYouMean {

	public String getNumFound(String queryResponse) throws Exception {
		//SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
		//CommonSearchResponseDTO responseDTO= new CommonSearchResponseDTO();
		//CommonSearchResponseDTO convertedObj = null;
		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.readValue(queryResponse, JsonNode.class);
	    JsonNode responseNode = node.get("response");
	    //System.out.println("response :"+responseNode);
	   //System.out.println("numfound: "+responseNode.get("numFound"));
	   String numfound=responseNode.get("numFound").toString();
	    

		/* try {
			 //String solrDocumentList =solrjClientConnectService.
			 convertedObj = (CommonSearchResponseDTO)SnowSearchUtils.convertJSONtoDTO(queryResponse, responseDTO);
			 //logger.debug("test log:" +convertedObj );
			System.out.println(convertedObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return  numfound;
	 }	
	
	public String spellcheck(String queryParam) throws IOException, SolrServerException {
		
		  SnowUtils snowSearchUtils= new SnowUtils();
		  Properties values= snowSearchUtils.getPropertyValues();
		  SolrjClientConnectService solrjClientConnectService= new SolrjClientConnectService();
		  CloudSolrClient solr=solrjClientConnectService.getSolrConnection(values);
		  ModifiableSolrParams params = new ModifiableSolrParams();
		  params.set("qt",values.getProperty("spellCheck.requestHandler").trim());
		  params.set("spellcheck.q", queryParam);
		  params.set("wt","json");
		  params.set("json.nl","map");
		  
		 // QueryResponse queryResponse=solr.query(params);
		 // queryResponse= (queryResponse)queryResponse;
		  
		 // System.out.println("NumFound :"+queryResponse.getResults().getNumFound());

		   
		    SolrQuery query=new SolrQuery();
		 	query.add(params);
		  /*query.set("wt","json");
		  query.add("json.nl","map");*/
		  QueryRequest req = new QueryRequest(query);
		//  System.out.println("Query String :"+query);
		  NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		   rawJsonResponseParser.setWriterType("json");
		   req.setResponseParser(rawJsonResponseParser);

		   NamedList<Object> resp = solr.request(req);
		   String jsonResponse = (String) resp.get("response");

		   		solr.close();
		  		return jsonResponse;
	}
		
		
		
		
	
}
