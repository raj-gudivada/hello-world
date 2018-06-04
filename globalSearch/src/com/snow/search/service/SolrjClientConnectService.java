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
import com.snow.search.dto.AttributesDTO;
import com.snow.util.SnowUtils;

public class SolrjClientConnectService {

	final static Logger LOG = Logger.getLogger(SolrjClientConnectService.class);


	public String querySearch(String queryParam, Integer start, Integer rows, String user, AttributesDTO attributes)
			throws IOException, SolrServerException {
		Properties values = SnowUtils.getPropertyValues();
		CloudSolrClient solr = getSolrConnection(values);
		SolrQuery query = new SolrQuery();
		query.setQuery(queryParam);
		query.setStart(start);
		query.setRows(rows);
		query.set("wt", "json");
		String filterType = attributes.getFilterType();
		String facetValue = attributes.getFacetValue();
		String activeProperty = attributes.getActiveProperty();
		String specialUser = attributes.getSpecialUser();
		if (filterType != null) {
			if (specialUser != null) {
				query.addFilterQuery(attributes.getSpecialUser(), filterType, activeProperty);
			} else {
				query.addFilterQuery(filterType, activeProperty);
			}
			if (facetValue != null) {
				query.add("json.nl", "map");
				setMultipleFacets(attributes, query);
			}
		} else {
			if (user != null) {
				query.addFilterQuery(user, activeProperty);
			} else {
				query.addFilterQuery(activeProperty);
			}
			if (facetValue != null) {
				query.add("json.nl", "map");
				setMultipleFacets(attributes, query);
			}
		}
		if (user == null && facetValue == null && specialUser == null && filterType == null) {
			query.addFilterQuery(activeProperty);
		}
		query.setFields(attributes.getFields());
		query.add("qf", attributes.getBoostField());
		if (attributes.getBoostQuery() != null) {
			query.add("bq", attributes.getBoostQuery());
		}
		query.add("defType", "edismax");

		if (null != query.toString()) {
			LOG.info("queryFormed:" + query);
		}

		QueryRequest req = new QueryRequest(query);
		NoOpResponseParser rawJsonResponseParser = new NoOpResponseParser();
		rawJsonResponseParser.setWriterType("json");
		req.setResponseParser(rawJsonResponseParser);
		NamedList<Object> resp = solr.request(req);
		String jsonResponse = (String) resp.get("response");

		if (null != query.toString()) {
			LOG.info("jsonResponse:" + jsonResponse);
		}

		solr.close();
		return jsonResponse;
	}

	public CloudSolrClient getSolrConnection(Properties values) {
		CloudSolrClient solr = null;
		String zkHostString = values.getProperty("zookeeper.connection");
		solr = new CloudSolrClient.Builder().withZkHost(zkHostString).build();
		solr.setDefaultCollection(values.getProperty("serviceNowCollection"));
		solr.setIdField("id");
		solr.connect();
		return solr;
	}

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

}
