package com.snow.search.facade;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import com.snow.search.dto.AttributesDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.search.service.SnowSearchService;
import com.snow.util.SnowPropertiesUtil;

public class SnowSearchFacade {
	
	public final static Logger LOG = Logger.getLogger(SnowAutoSuggestFacade.class);
	
	SnowSearchService snowSearchService;

	public String fetchUserRoles(RequestDTO reqDto) throws IOException {

		Properties values = SnowPropertiesUtil.getPropertyValues();
		List<String> searchType = reqDto.getSearchType();
		String roleValue = null;
		if (searchType.contains("LOC") || searchType.contains("UD") || searchType.contains("APP")) {
			roleValue = values.getProperty("ud.loc.all.roles");
		} else {
			roleValue = values.getProperty("acl.all.roles");
		}
		List<String> userRoles = reqDto.getUserRoles();
		String response = null;
		if (userRoles.isEmpty()
				|| (!userRoles.isEmpty() && userRoles.size() == 1 && userRoles.get(0).trim().equals(""))) {
			response = null;
		} else {
			String finalUser = roleValue + ":(";
			String appender = " OR ";
			if (!userRoles.isEmpty() && userRoles.size() > 1) {
				for (String role : userRoles) {
					finalUser = finalUser + "\"" + role + "\"";
					finalUser = finalUser + appender;
				}
				finalUser = finalUser.substring(0, finalUser.length() - 4);
			} else if (!userRoles.isEmpty() && userRoles.size() == 1) {
				finalUser = finalUser + "\"" + userRoles + "\"";
			}
			finalUser = finalUser + ")";
			response = finalUser;
		}
		return response;
	}

	public AttributesDTO fetchAll(RequestDTO reqDTO) throws IOException {
		AttributesDTO attributes = new AttributesDTO();
		Properties values = SnowPropertiesUtil.getPropertyValues();
		List<String> searchTypes = reqDTO.getSearchType();
		List<String> facetselected = reqDTO.getFacetSelection();
		StringBuffer sb = new StringBuffer();
		StringBuilder facetBuilder = new StringBuilder();
		if (searchTypes.size() > 1) {
			for (String multiSearchType : searchTypes) {
				sb.append("\"" + multiSearchType + "\"" + ",");
			}
			if (searchTypes.contains("LOC") || searchTypes.contains("UD")) {
				attributes.setSpecialUser("");
			} else {
				attributes.setSpecialUser(fetchUserRoles(reqDTO));
			}
			String finalFilterType = sb.substring(0, sb.length() - 1);
			if (facetselected.contains("") || facetselected.isEmpty() || facetselected.contains(" ")
					|| facetselected == null) {
				attributes.setFacetValue(values.getProperty("basic.all.facets"));
				attributes.setFacet(Boolean.TRUE);
			} else {
				fetchMultiplefacets(attributes, facetselected, facetBuilder);
			}
			attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "(" + finalFilterType + ")");
			attributes.setFields(values.getProperty("basic.fl.all"));
			attributes.setBoostField(values.getProperty("all.boosting.order"));
			attributes.setBoostQuery(values.getProperty("all.boosting.query.order"));
		} else {
			for (String eachSearchType : searchTypes) {

				if (eachSearchType.isEmpty() || eachSearchType.equalsIgnoreCase("NULL")
						|| eachSearchType.equalsIgnoreCase("NONE") || eachSearchType.equalsIgnoreCase("ALL")) {
					attributes.setFields(values.getProperty("basic.fl.all"));
					if (facetselected.contains("") || facetselected.isEmpty() || facetselected.contains(" ")
							|| facetselected == null) {
						attributes.setFacetValue(values.getProperty("basic.all.facets"));
						attributes.setFacet(Boolean.TRUE);
					} else {
						fetchMultiplefacets(attributes, facetselected, facetBuilder);
					}
					attributes.setBoostField(values.getProperty("all.boosting.order"));
					attributes.setBoostQuery(values.getProperty("all.boosting.query.order"));
				}
				if (!eachSearchType.isEmpty() && eachSearchType.equalsIgnoreCase("SC")) {
					attributes.setFields(values.getProperty("basic.fl.sc"));
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("sc.filter.type"));
					if (facetselected.contains("") || facetselected.isEmpty() || facetselected.contains(" ")
							|| facetselected == null) {
						attributes.setFacetValue(values.getProperty("basic.sc.facets"));
						attributes.setFacet(Boolean.TRUE);
					} else {
						fetchMultiplefacets(attributes, facetselected, facetBuilder);
					}
					attributes.setSpecialUser(fetchUserRoles(reqDTO));
					attributes.setBoostField(values.getProperty("sc.boosting.order"));
				}
				if (!eachSearchType.isEmpty() && eachSearchType.equalsIgnoreCase("UD")) {
					attributes.setFields(values.getProperty("basic.fl.ud"));
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("ud.filter.type"));
					if (facetselected.contains("") || facetselected.isEmpty() || facetselected.contains(" ")
							|| facetselected == null) {
						attributes.setFacetValue(values.getProperty("basic.ud.facets"));
						attributes.setFacet(Boolean.TRUE);
					} else {
						fetchMultiplefacets(attributes, facetselected, facetBuilder);
					}
					attributes.setSpecialUser(fetchUserRoles(reqDTO));
					attributes.setBoostField(values.getProperty("ud.boosting.order"));
				}
				if (!eachSearchType.isEmpty() && eachSearchType.equalsIgnoreCase("KB")) {
					attributes.setFields(values.getProperty("basic.fl.kb"));
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("kb.filter.type"));
					if (facetselected.contains("") || facetselected.isEmpty() || facetselected.contains(" ")
							|| facetselected == null) {
						attributes.setFacetValue(values.getProperty("basic.kb.facets"));
						attributes.setFacet(Boolean.TRUE);
					} else {
						fetchMultiplefacets(attributes, facetselected, facetBuilder);
					}
					attributes.setSpecialUser(fetchUserRoles(reqDTO));
					attributes.setBoostField(values.getProperty("kb.boosting.order"));
				}
				if (!eachSearchType.isEmpty() && eachSearchType.equalsIgnoreCase("LOC")) {
					attributes.setFields(values.getProperty("basic.fl.loc"));
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("loc.filter.type"));
					attributes.setSpecialUser(fetchUserRoles(reqDTO));
					attributes.setBoostField(values.getProperty("loc.boosting.order"));
				}
				if (!eachSearchType.isEmpty() && eachSearchType.equalsIgnoreCase("APP")) {
					attributes.setFields(values.getProperty("basic.fl.app"));
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("app.filter.type"));
					attributes.setSpecialUser(fetchUserRoles(reqDTO));
					attributes.setBoostField(values.getProperty("app.boosting.order"));
				}
			}

		}
		attributes.setActiveProperty(values.getProperty("active.property") + ":" + Boolean.TRUE);
		return attributes;
	}

	private void fetchMultiplefacets(AttributesDTO attributes, List<String> facetselected, StringBuilder facetBuilder) {
		for (String selectedFacet : facetselected) {
			String[] splitedFacet = selectedFacet.split(":");
			facetBuilder.append(splitedFacet[1]);
			facetBuilder.append(",");
		}
		String finalfacetValue = facetBuilder.substring(0, facetBuilder.length() - 1);
		attributes.setFacetValue(finalfacetValue);
		attributes.setFacet(Boolean.TRUE);
	}

	public String getQuerySearch(RequestDTO requestDTO, String user, AttributesDTO attributes)
			throws IOException, SolrServerException {
		String queryResponse = snowSearchService.querySearch(requestDTO.getQueryParam(), requestDTO.getStart(),
				requestDTO.getRows(), user, attributes);
		return queryResponse;

	}

	public SnowSearchService getSnowSearchService() {
		return snowSearchService;
	}

	@Resource
	public void setSnowSearchService(SnowSearchService snowSearchService) {
		this.snowSearchService = snowSearchService;
	}

}
