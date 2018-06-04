package com.snow.search.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.search.dto.AttributesDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.util.SnowUtils;

public class AutoSuggestSolrjClientConnectService {

	final static Logger LOG = Logger.getLogger(AutoSuggestSolrjClientConnectService.class);

	// AutoFill Query Module
	public void setMultipleFacets(AttributesDTO attributes, SolrQuery query) {
		if (attributes.getFacet() == true) {
			String facetFieldMapValue = "facet.field";
			String[] fieldNames = attributes.getFacetValue().split(",");
			Map<String, String[]> facetFieldNames = new HashMap<String, String[]>();
			facetFieldNames.put(facetFieldMapValue, fieldNames);
			ModifiableSolrParams solrParams = new ModifiableSolrParams(facetFieldNames);
			Map<String, String[]> facetField = solrParams.getMap();
			for (Map.Entry<String, String[]> entry : facetField.entrySet()) {
				String[] fieldValue = entry.getValue();
				String value = null;
				String param = entry.getKey();
				for (String string : fieldValue) {
					value = string;
					query.add(param, value);
				}
			}
		}
		query.setFacet(attributes.getFacet());
	}

	public String autoFillQuerySearch(RequestDTO requestDTO, AttributesDTO attributesDTO)
			throws IOException, SolrServerException {
		String jsonResponse = null;
		String activeProperty = attributesDTO.getActiveProperty();
		Properties values = null;
		values = SnowUtils.getPropertyValues();
		SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
		CloudSolrClient solr = solrjClientConnectService.getSolrConnection(values);
		SolrQuery query = new SolrQuery();
		query.setQuery(requestDTO.getQueryParam());
		query.setRows(requestDTO.getMaxRows());
		query.set("wt", "json");
		String filterType = attributesDTO.getFilterType();
		if (filterType != null) {
			String specialUser = attributesDTO.getSpecialUser();
			if (specialUser != null) {
				query.addFilterQuery(attributesDTO.getSpecialUser(), filterType, activeProperty);
			} else {
				query.addFilterQuery(filterType, activeProperty);
			}
		} else {
			query.addFilterQuery(activeProperty);
		}
		// Multi facet . module
		setMultipleFacets(attributesDTO, query);
		query.setFields(attributesDTO.getFields());
		query.add("qf", attributesDTO.getBoostField());
		query.add("defType", "edismax");
		QueryRequest req = new QueryRequest(query);
		NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		rawJsonResponseParser.setWriterType("json");
		req.setResponseParser(rawJsonResponseParser);
		NamedList<Object> resp = solr.request(req);
		jsonResponse = (String) resp.get("response");
		LOG.info("Solr Query :" + query.toString());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readValue(jsonResponse, JsonNode.class);
		JsonNode responseHeaderNode = node.get("responseHeader");
		String qTime = responseHeaderNode.get("QTime").toString();
		LOG.info("Query Time :" + qTime + "mSec");
		return jsonResponse;
	}

	public String autoSuggestTermsSearch(String queryParam, Integer maxRows, AttributesDTO attributes)
			throws IOException, SolrServerException {

		String activeProperty = attributes.getActiveProperty();
		Properties values = null;
		String jsonResponse = null;
		values = SnowUtils.getPropertyValues();
		SolrjClientConnectService solrjClientConnectService = new SolrjClientConnectService();
		CloudSolrClient solr = solrjClientConnectService.getSolrConnection(values);
		SolrQuery query = new SolrQuery();
		query.setFacet(attributes.getFacet());
		query.setQuery(values.getProperty("autoSuggest.terms.q"));
		query.add("facet.field", (values.getProperty("autoSuggest.terms.facet.field")));
		query.setFacetPrefix(queryParam.toLowerCase());
		String filterType = attributes.getFilterType();
		if (filterType != null) {
			String specialUser = attributes.getSpecialUser();
			if (specialUser != null) {
				query.addFilterQuery(attributes.getSpecialUser(), filterType, activeProperty);
			} else {
				query.addFilterQuery(filterType, activeProperty);
			}
		} else {
			query.addFilterQuery(activeProperty);
		}
		query.setFacetLimit(maxRows);
		String facetSort = values.getProperty("autoSuggest.facet.sort");
		query.setFacetSort(facetSort.trim());
		String minCount = values.getProperty("autoSuggest.facet.minCount");
		query.setFacetMinCount(Integer.valueOf(minCount.trim()));
		String rows = values.getProperty("autoSuggest.terms.rows");
		query.setRows(Integer.valueOf(rows.trim()));
		query.set("wt", "json");
		query.add("json.nl", "map");
		QueryRequest req = new QueryRequest(query);
		NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		rawJsonResponseParser.setWriterType("json");
		req.setResponseParser(rawJsonResponseParser);
		NamedList<Object> resp = solr.request(req);
		jsonResponse = (String) resp.get("response");
		LOG.info("Solr Query :" + query.toString());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readValue(jsonResponse, JsonNode.class);
		JsonNode responseHeaderNode = node.get("responseHeader");
		String qTime = responseHeaderNode.get("QTime").toString();
		LOG.info("Query Time :" + qTime + "mSec");
		solr.close();
		return jsonResponse;
	}
}
