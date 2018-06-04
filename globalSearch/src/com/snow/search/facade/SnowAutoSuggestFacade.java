package com.snow.search.facade;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import com.snow.search.dto.AttributesDTO;
import com.snow.search.dto.RequestDTO;
import com.snow.search.service.AutoSuggestSolrjClientConnectService;
import com.snow.util.SnowUtils;;

public class SnowAutoSuggestFacade {
	final static Logger LOG = Logger.getLogger(SnowAutoSuggestFacade.class);

	public AttributesDTO fetchAutoFillFields(RequestDTO requestDTO) throws IOException {
		SnowUtils snowSearchUtils = new SnowUtils();
		AttributesDTO attributes = new AttributesDTO();
		List<String> searchTypes = requestDTO.getSearchType();
		Properties values = snowSearchUtils.getPropertyValues();
		if (searchTypes.size() > 1) {
			SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
			attributes = snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, requestDTO);
			for (String searchValue : searchTypes) {
				if (!searchValue.isEmpty() | searchValue.equalsIgnoreCase("NULL") | searchValue.equalsIgnoreCase("NONE")
						| searchValue.equalsIgnoreCase("ALL")) {
					LOG.info("searchValue - " + searchValue);
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));

				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("SC")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
					attributes.setFacet(Boolean.TRUE);
				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("KB")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("UD")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
					attributes.setFacet(Boolean.TRUE);
				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("LOC")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("APP")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
					attributes.setFacet(Boolean.TRUE);
				}
				attributes.setActiveProperty(values.getProperty("active.property") + ":" + Boolean.TRUE);
			}
		} else {
			for (String searchValue : searchTypes) {
				if (!searchValue.isEmpty() | searchValue.equalsIgnoreCase("NULL") | searchValue.equalsIgnoreCase("NONE")
						| searchValue.equalsIgnoreCase("ALL")) {
					LOG.info("searchValue - " + searchValue);
					attributes.setFields(values.getProperty("autoSuggest.fl.all"));
					attributes.setFilterType(values.getProperty("filter.type.all") + ":*");
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
					attributes.setBoostField(values.getProperty("all.boosting.order"));
				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("SC")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.sc"));
					attributes.setFacetValue(values.getProperty("autoSuggest.sc.facets"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":"
							+ values.getProperty("autoSuggest.sc.filter.type"));
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setBoostField(values.getProperty("sc.boosting.order"));
				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("KB")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.kb"));
					attributes.setFacetValue(values.getProperty("autoSuggest.kb.facets"));
					attributes.setFacet(Boolean.FALSE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":"
							+ values.getProperty("autoSuggest.kb.filter.type"));
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setBoostField(values.getProperty("kb.boosting.order"));
				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("UD")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.ud"));
					attributes.setFacetValue(values.getProperty("autoSuggest.ud.facets"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":"
							+ values.getProperty("autoSuggest.ud.filter.type"));
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setBoostField(values.getProperty("ud.boosting.order"));

				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("LOC")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.loc"));
					attributes.setFacetValue(values.getProperty("autoSuggest.loc.facets"));
					attributes.setFacet(Boolean.FALSE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":"
							+ values.getProperty("autoSuggest.loc.filter.type"));
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setBoostField(values.getProperty("loc.boosting.order"));

				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("APP")) {
					attributes.setFields(values.getProperty("autoSuggest.fl.app"));
					attributes.setFacet(Boolean.FALSE);
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("app.filter.type"));
					SnowUtiltyFunctions functions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
					attributes.setBoostField(values.getProperty("app.boosting.order"));

				}
				attributes.setActiveProperty(values.getProperty("active.property") + ":" + Boolean.TRUE);

			}
		}
		return attributes;
	}

	public String getAutoQuerySearch(RequestDTO requestDTO, AttributesDTO attributesDTO)
			throws IOException, SolrServerException {
		AutoSuggestSolrjClientConnectService solrjClientConnectService = new AutoSuggestSolrjClientConnectService();
		String queryResponse = null;

		queryResponse = solrjClientConnectService.autoFillQuerySearch(requestDTO, attributesDTO);

		return queryResponse;
	}

	// AutoSuggestTerms Module

	public String autoSuggestTermsQuerySearch(RequestDTO requestDTO, AttributesDTO attributes)
			throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		AutoSuggestSolrjClientConnectService solrjClientConnectService = new AutoSuggestSolrjClientConnectService();
		String queryResponse = null;

		queryResponse = solrjClientConnectService.autoSuggestTermsSearch(requestDTO.getQueryParam(),
				requestDTO.getMaxRows(), attributes);

		return queryResponse;
	}

	public AttributesDTO fetchAutoSuggestTerms(RequestDTO requestDTO) throws IOException {
		SnowUtils snowSearchUtils = new SnowUtils();
		AttributesDTO attributes = new AttributesDTO();
		List<String> searchTypes = requestDTO.getSearchType();
		Properties values = snowSearchUtils.getPropertyValues();

		// If SearchType is more than one value Ex: "searchType":["SC","UD"]
		if (searchTypes.size() > 1) {
			SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
			attributes = snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, requestDTO);
			attributes.setFacet(Boolean.TRUE);
			attributes.setActiveProperty(values.getProperty("active.property") + ":" + Boolean.TRUE);

		}

		else {
			for (String searchValue : searchTypes) {

				if (!searchValue.isEmpty() | searchValue.equalsIgnoreCase("NULL") | searchValue.equalsIgnoreCase("NONE")
						| searchValue.equalsIgnoreCase("ALL")) {
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":*");
					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));
				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("SC")) {

					attributes.setFacet(Boolean.TRUE);

					attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "\"" + searchValue + "\"");

					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("KB")) {

					attributes.setFacet(Boolean.TRUE);

					attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "\"" + searchValue + "\"");
					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("UD")) {
					attributes.setFacet(Boolean.TRUE);

					attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "\"" + searchValue + "\"");
					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("LOC")) {
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(values.getProperty("filter.type.all") + ":" + "\"" + searchValue + "\"");
					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}
				if (!searchValue.isEmpty() && searchValue.equalsIgnoreCase("APP")) {
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(
							values.getProperty("filter.type.all") + ":" + values.getProperty("app.filter.type"));
					SnowUtiltyFunctions snowUtiltyFunctions = new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				attributes.setActiveProperty(values.getProperty("active.property") + ":" + Boolean.TRUE);

			}
		}
		return attributes;
	}

}
