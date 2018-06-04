package com.snow.search.service;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import com.snow.search.dto.AttributesDTO;
import com.snow.util.SnowUtils;

public class SolrjClientConnectService {
	final static Logger logger = Logger.getLogger(SolrjClientConnectService.class);
	public String querySearch(String queryParam,Integer start, Integer rows,String user,AttributesDTO attributes) throws IOException, SolrServerException{
		SnowUtils snowSearchUtils= new SnowUtils();
		Properties values= snowSearchUtils.getPropertyValues();
		CloudSolrClient solr = getSolrConnection(values);
		SolrQuery query = new SolrQuery(); 
		query.setQuery(queryParam);
		query.setStart(start);
		query.setRows(rows);
		query.set("wt","json");
		String filterType = attributes.getFilterType();
		String facetValue=attributes.getFacetValue();
		String activeProperty=attributes.getActiveProperty();
		String specialUser= attributes.getSpecialUser();  
		if(filterType!=null) {
			if(specialUser!=null ) {
				query.addFilterQuery(attributes.getSpecialUser(),filterType,activeProperty);
			}
			else {
				query.addFilterQuery(filterType,activeProperty);
			}
			if(facetValue!=null) {
				query.add("json.nl", "map");
				//query.addFilterQuery(activeProperty);
				setMultipleFacets(attributes, query);
			}
		}
		else {
			if(user!=null) {
				query.addFilterQuery(user,activeProperty);
			}
			else {
				query.addFilterQuery(activeProperty);
			}
			if(facetValue!=null) {
				query.add("json.nl", "map");
				setMultipleFacets(attributes, query);
			}
		}
		if(user==null&& facetValue==null&& specialUser==null && filterType==null) {
			query.addFilterQuery(activeProperty);
		}
		query.setFields(attributes.getFields());
		query.add("qf",attributes.getBoostField());
		if(attributes.getBoostQuery()!=null) {
		query.add("bq",attributes.getBoostQuery());
		}
		query.add("defType","edismax");
		logger.info("queryFormed:" +query);
		QueryRequest req = new QueryRequest(query);
		NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		rawJsonResponseParser.setWriterType("json");
		req.setResponseParser(rawJsonResponseParser);
		NamedList<Object> resp = solr.request(req);
		String jsonResponse = (String) resp.get("response");
		logger.info("jsonResponse:" +jsonResponse);
		solr.close();
		return jsonResponse;
	}

	public CloudSolrClient getSolrConnection(Properties values) {
		/*	SolrClient solr = new CloudSolrClient.Builder().withSolrUrl(values.getProperty("serviceNowUrl")).build();
			((CloudSolrClient) solr).setDefaultCollection(values.getProperty("serviceNowCollection"));*/
		    CloudSolrClient solr = null;
		    String zkHostString = values.getProperty("zookeeper.connection");
			   solr = new CloudSolrClient.Builder().withZkHost(zkHostString).build();
			   solr.setDefaultCollection(values.getProperty("serviceNowCollection"));
			   solr.setIdField("id");
			   solr.connect();
		return solr;
	}

	public void setMultipleFacets(AttributesDTO attributes, SolrQuery query) {
		if(attributes.getFacet()==true){
			String facetFieldMapValue="facet.field";
			String[] fieldNames=attributes.getFacetValue().split(",");

			Map<String, String[]> facetFieldNames = new HashMap<String, String[]>();

			facetFieldNames.put(facetFieldMapValue, fieldNames);	

			ModifiableSolrParams solrParams = new ModifiableSolrParams(facetFieldNames);
			Map<String, String[]> facetField = solrParams.getMap();
			for (Map.Entry<String, String[]> entry : facetField.entrySet()) {
				//   System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				String[] fieldValue = entry.getValue();
				String value=null;
				String param = entry.getKey();
				//System.out.println("param "+param);
				for (String string : fieldValue) {
					value = string;
					query.add(param, value); 
				}
				//System.out.println("value "+value);

				// adding multiple identical params like this works. 
				// It creates a single key with multiple values in the underlying LinkedHashMap

			}
		}
		/*  while(iterator.hasNext()){
		       String param = iterator.next();
		       System.out.println("param "+param);
		       String value = solrParams.get(param);
		       System.out.println("value "+value);

		       // adding multiple identical params like this works. 
		       // It creates a single key with multiple values in the underlying LinkedHashMap
		       query.add(param, value); 

		   }
		 */
		query.setFacet(attributes.getFacet());
	}


/*	public String search() throws IOException, SolrServerException {
		SnowUtils snowSearchUtils= new SnowUtils();
		Properties values= snowSearchUtils.getPropertyValues();
		SolrClient solr = new CloudSolrClient.Builder().withSolrUrl(values.getProperty("solrUrl")).build();
		((CloudSolrClient) solr).setDefaultCollection("techproducts");
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.set("wt","json");
		query.setFields("id","name","author");
		QueryRequest req = new QueryRequest(query);

		NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		rawJsonResponseParser.setWriterType("json");
		req.setResponseParser(rawJsonResponseParser);

		NamedList<Object> resp = solr.request(req);
		String jsonResponse = (String) resp.get("response");

		return jsonResponse;
	}

*/

}
