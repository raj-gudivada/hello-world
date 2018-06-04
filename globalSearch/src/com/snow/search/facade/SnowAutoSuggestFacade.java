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
	final static Logger logger = Logger.getLogger(SnowAutoSuggestFacade.class);
	public AttributesDTO fetchAutoFillFields(RequestDTO requestDTO) throws IOException {
		SnowUtils snowSearchUtils =new SnowUtils();
		AttributesDTO attributes=new AttributesDTO();
		List<String> searchTypes=requestDTO.getSearchType();
		/*try{
*/			Properties values= snowSearchUtils.getPropertyValues();
			if(searchTypes.size()>1) {
				SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, requestDTO);
				for (String searchValue : searchTypes) {
					if(!searchValue.isEmpty()| searchValue.equalsIgnoreCase("NULL")|searchValue.equalsIgnoreCase("NONE")|searchValue.equalsIgnoreCase("ALL")) {
						System.out.println("searchValue - "+searchValue);
						//SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						//attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						//System.out.println("Field list -"+attributes.getFields());
						/*attributes.setFilterType(values.getProperty("filter.type.all")+":*");
				SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
				attributes.setSpecialUser(functions.fetchUserRoles(autoSuggestDTO));
						 */				attributes.setFacet(Boolean.TRUE);
						 attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
						 //attributes.setBoostField(values.getProperty("all.boosting.order"));

					}
					if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("SC")) {
						//SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						//attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);

						//System.out.println("searchValue - "+searchValue);

						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						//System.out.println("Field list -"+attributes.getFields());

						attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
						//System.out.println("Facet value -"+attributes.getFacetValue());

						attributes.setFacet(Boolean.TRUE);
						//attributes.setBoostField(values.getProperty("sc.boosting.order"));

						//System.out.println("Facet value -"+attributes.getFacet());

						/*attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.sc.filter.type"));
				//System.out.println("Facet value -"+attributes.getFilterType());
				SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
				attributes.setSpecialUser(functions.fetchUserRoles(autoSuggestDTO));*/


					}

					if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("KB")) {
						//System.out.println("searchValue - "+searchValue);
						//SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						//attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						//System.out.println("Field list -"+attributes.getFields());

						//	attributes.setFacetValue(values.getProperty("autoSuggest.kb.facets"));
						//System.out.println("Facet value -"+attributes.getFacetValue());

						attributes.setFacet(Boolean.TRUE);
						attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));

						//attributes.setBoostField(values.getProperty("kb.boosting.order"));

						//System.out.println("Facet value -"+attributes.getFacet());

						/*attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.kb.filter.type"));
				//System.out.println("Facet value -"+attributes.getFilterType());
				SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
				attributes.setSpecialUser(functions.fetchUserRoles(autoSuggestDTO));
						 */

					}

					if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("UD")) {
						//System.out.println("searchValue - "+searchValue);
						//SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						//attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);

						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						//System.out.println("Field list -"+attributes.getFields());

						attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
						//System.out.println("Facet value -"+attributes.getFacetValue());

						attributes.setFacet(Boolean.TRUE);
						//attributes.setBoostField(values.getProperty("ud.boosting.order"));

						//System.out.println("Facet value -"+attributes.getFacet());

						/*attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.ud.filter.type"));
				//System.out.println("Facet value -"+attributes.getFilterType());
				SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
				attributes.setSpecialUser(functions.fetchUserRoles(autoSuggestDTO));
						 */

					}

					if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("LOC")) {
						//System.out.println("searchValue - "+searchValue);

						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						//System.out.println("Field list -"+attributes.getFields());
						/*SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				//attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						 */				//attributes.setFacetValue(values.getProperty("autoSuggest.loc.facets"));
						//System.out.println("Facet value -"+attributes.getFacetValue());

						attributes.setFacet(Boolean.TRUE);
						attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));

						//attributes.setBoostField(values.getProperty("loc.boosting.order"));

						//System.out.println("Facet value -"+attributes.getFacet());

						/*attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.loc.filter.type"));
				System.out.println("Facet value -"+attributes.getFilterType());


				SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
				attributes.setSpecialUser(functions.fetchUserRoles(autoSuggestDTO));
						 */
					}
					if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("APP")) {
						//attributes.setFields(values.getProperty("basic.fl.app"));
						attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));


						attributes.setFacet(Boolean.TRUE);
						//attributes.setBoostField(values.getProperty("app.boosting.order"));

						//attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("app.filter.type"));
						//SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						//attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(autoSuggestDTO));


					}
					 attributes.setActiveProperty(values.getProperty("active.property")+":"+Boolean.TRUE);


				}
				//attributes.setFacet(Boolean.TRUE);
				
			}

			//else{

			//****************************

			/*List<String> searchTypes=reqDto.getSearchType();
		Properties values= snowSearchUtils.getPropertyValues();
		AttributesDTO attributes= new AttributesDTO();
		//System.out.println("Here in fetchAutoFillFields");
		//System.out.println("searchType len : "+searchTypes.size());
			 */		else{
				 for (String searchValue : searchTypes) {
					 if(!searchValue.isEmpty()| searchValue.equalsIgnoreCase("NULL")|searchValue.equalsIgnoreCase("NONE")|searchValue.equalsIgnoreCase("ALL")) {
						 System.out.println("searchValue - "+searchValue);
						 //SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						 //attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						 attributes.setFields(values.getProperty("autoSuggest.fl.all"));
						 //System.out.println("Field list -"+attributes.getFields());
						 attributes.setFilterType(values.getProperty("filter.type.all")+":*");
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setFacet(Boolean.TRUE);
						 attributes.setFacetValue(values.getProperty("autoSuggest.facet.all"));
						 attributes.setBoostField(values.getProperty("all.boosting.order"));

					 }
					 if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("SC")) {
						 /*SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						  */
						 //System.out.println("searchValue - "+searchValue);

						 attributes.setFields(values.getProperty("autoSuggest.fl.sc")); 
						 //System.out.println("Field list -"+attributes.getFields());

						 attributes.setFacetValue(values.getProperty("autoSuggest.sc.facets"));
						 //System.out.println("Facet value -"+attributes.getFacetValue());

						 attributes.setFacet(Boolean.TRUE);
						 //System.out.println("Facet value -"+attributes.getFacet());

						 attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.sc.filter.type"));
						 //System.out.println("Facet value -"+attributes.getFilterType());
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setBoostField(values.getProperty("sc.boosting.order"));



					 }

					 if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("KB")) {
						 //System.out.println("searchValue - "+searchValue);
						 /*SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);*/
						 attributes.setFields(values.getProperty("autoSuggest.fl.kb")); 
						 //System.out.println("Field list -"+attributes.getFields());

						 attributes.setFacetValue(values.getProperty("autoSuggest.kb.facets"));
						 //System.out.println("Facet value -"+attributes.getFacetValue());

						 attributes.setFacet(Boolean.FALSE);
						 //System.out.println("Facet value -"+attributes.getFacet());

						 attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.kb.filter.type"));
						 //System.out.println("Facet value -"+attributes.getFilterType());
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setBoostField(values.getProperty("kb.boosting.order"));



					 }

					 if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("UD")) {
						 //System.out.println("searchValue - "+searchValue);
						 /*SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);
						  */
						 attributes.setFields(values.getProperty("autoSuggest.fl.ud")); 
						 //System.out.println("Field list -"+attributes.getFields());

						 attributes.setFacetValue(values.getProperty("autoSuggest.ud.facets"));
						 //System.out.println("Facet value -"+attributes.getFacetValue());

						 attributes.setFacet(Boolean.TRUE);
						 //System.out.println("Facet value -"+attributes.getFacet());

						 attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.ud.filter.type"));
						 //System.out.println("Facet value -"+attributes.getFilterType());
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setBoostField(values.getProperty("ud.boosting.order"));



					 }

					 if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("LOC")) {
						 //System.out.println("searchValue - "+searchValue);

						 attributes.setFields(values.getProperty("autoSuggest.fl.loc")); 
						 //System.out.println("Field list -"+attributes.getFields());
						 /*SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
				attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, autoSuggestDTO);*/
						 attributes.setFacetValue(values.getProperty("autoSuggest.loc.facets"));
						 //System.out.println("Facet value -"+attributes.getFacetValue());

						 attributes.setFacet(Boolean.FALSE);
						 //System.out.println("Facet value -"+attributes.getFacet());

						 attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("autoSuggest.loc.filter.type"));
						 //System.out.println("Facet value -"+attributes.getFilterType());
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setBoostField(values.getProperty("loc.boosting.order"));



					 }
					 if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("APP")) {
						 //attributes.setFields(values.getProperty("basic.fl.app"));
						 attributes.setFields(values.getProperty("autoSuggest.fl.app")); 

						 attributes.setFacet(Boolean.FALSE);
						 attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("app.filter.type"));
						 //SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
						 //attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(autoSuggestDTO));
						 SnowUtiltyFunctions functions=new SnowUtiltyFunctions();
						 attributes.setSpecialUser(functions.fetchUserRoles(requestDTO));
						 attributes.setBoostField(values.getProperty("app.boosting.order"));



					 }
					 attributes.setActiveProperty(values.getProperty("active.property")+":"+Boolean.TRUE);


				 }}
		//}
		/*catch(IOException exception){
			logger.error("Eror occured while populating attributes DTO");		

		}
*/
		return attributes;
	}


	public String getAutoQuerySearch(RequestDTO requestDTO,AttributesDTO attributesDTO) throws IOException, SolrServerException {
		AutoSuggestSolrjClientConnectService solrjClientConnectService= new AutoSuggestSolrjClientConnectService();
		String queryResponse=null;
		
			queryResponse=solrjClientConnectService.autoFillQuerySearch(requestDTO,attributesDTO);
	
		

		return queryResponse;
	}

	// AutoSuggestTerms Module

	public String autoSuggestTermsQuerySearch(RequestDTO requestDTO, AttributesDTO attributes) throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		AutoSuggestSolrjClientConnectService solrjClientConnectService= new AutoSuggestSolrjClientConnectService();
		String queryResponse=null;

		 queryResponse=solrjClientConnectService.autoSuggestTermsSearch(requestDTO.getQueryParam(), requestDTO.getMaxRows(), attributes);

		


		return queryResponse;
	}

	public AttributesDTO fetchAutoSuggestTerms(RequestDTO requestDTO) throws IOException  {
		SnowUtils snowSearchUtils =new SnowUtils();
		AttributesDTO attributes=new AttributesDTO();
		List<String> searchTypes=requestDTO.getSearchType();
		//try{
		Properties values= snowSearchUtils.getPropertyValues();

		// If SearchType is more than one value Ex: "searchType":["SC","UD"]
		//StringBuffer sb= new StringBuffer();
		if(searchTypes.size()>1) {
			SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
			attributes=snowUtiltyFunctions.fetchSearchTypeUserFilter(searchTypes, requestDTO);
			attributes.setFacet(Boolean.TRUE);
			 attributes.setActiveProperty(values.getProperty("active.property")+":"+Boolean.TRUE);

		}

		else{
			for (String searchValue : searchTypes) {

				if(!searchValue.isEmpty()| searchValue.equalsIgnoreCase("NULL")|searchValue.equalsIgnoreCase("NONE")|searchValue.equalsIgnoreCase("ALL")) {
					//System.out.println("searchValue - "+searchValue);
					attributes.setFacet(Boolean.TRUE);
					//System.out.println("Field list -"+attributes.getFields());
					attributes.setFilterType(values.getProperty("filter.type.all")+":*");
					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));
					//	attributes.setSpecialUse;
				}
				if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("SC")) {
					//System.out.println("searchValue - "+searchValue);

					attributes.setFacet(Boolean.TRUE);
					//System.out.println("Facet value -"+attributes.getFacet());

					attributes.setFilterType(values.getProperty("filter.type.all")+":"+"\""+searchValue+"\"");
					//System.out.println("filter value -"+attributes.getFilterType());

					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("KB")) {
					//System.out.println("searchValue - "+searchValue);


					attributes.setFacet(Boolean.TRUE);
					//System.out.println("Facet value -"+attributes.getFacet());

					attributes.setFilterType(values.getProperty("filter.type.all")+":"+"\""+searchValue+"\"");
					//System.out.println("Facet value -"+attributes.getFilterType());
					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));


				}

				if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("UD")) {
					//System.out.println("searchValue - "+searchValue);

					//attributes.setFields(values.getProperty("autoFill.fl.ud")); 
					//System.out.println("Field list -"+attributes.getFields());

					//	attributes.setFacetValue(values.getProperty("autoFill.ud.facets"));
					//System.out.println("Facet value -"+attributes.getFacetValue());

					attributes.setFacet(Boolean.TRUE);
					//System.out.println("Facet value -"+attributes.getFacet());

					attributes.setFilterType(values.getProperty("filter.type.all")+":"+"\""+searchValue+"\"");
					//System.out.println("Facet value -"+attributes.getFilterType());
					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));


				}

				if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("LOC")) {
					//System.out.println("searchValue - "+searchValue);

					//attributes.setFields(values.getProperty("autoFill.fl.loc")); 
					//System.out.println("Field list -"+attributes.getFields());

					//attributes.setFacetValue(values.getProperty("autoFill.loc.facets"));
					//System.out.println("Facet value -"+attributes.getFacetValue());

					attributes.setFacet(Boolean.TRUE);
					//System.out.println("Facet value -"+attributes.getFacet());

					attributes.setFilterType(values.getProperty("filter.type.all")+":"+"\""+searchValue+"\"");
					//System.out.println("Facet value -"+attributes.getFilterType());
					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));


				}
				if(!searchValue.isEmpty()&& searchValue.equalsIgnoreCase("APP")) {
					//attributes.setFields(values.getProperty("basic.fl.app"));
					attributes.setFacet(Boolean.TRUE);
					attributes.setFilterType(values.getProperty("filter.type.all")+":"+values.getProperty("app.filter.type"));
					SnowUtiltyFunctions snowUtiltyFunctions=new SnowUtiltyFunctions();
					attributes.setSpecialUser(snowUtiltyFunctions.fetchUserRoles(requestDTO));

				}

				 attributes.setActiveProperty(values.getProperty("active.property")+":"+Boolean.TRUE);


			}
		}
/*	}
		catch(Exception exception){
			logger.error("Eror occured while populating attributes DTO");		

		}
*/
		return attributes;
	}

}
