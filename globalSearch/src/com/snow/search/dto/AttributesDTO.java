package com.snow.search.dto;

public class AttributesDTO {

	private String fields;
	private String facetValue;
	private Boolean facet;
	private String filterType;
	private String specialUser;
	private String boostField;
	private String activeProperty;
	private String boostQuery;

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getFacetValue() {
		return facetValue;
	}

	public void setFacetValue(String facetValue) {
		this.facetValue = facetValue;
	}

	public Boolean getFacet() {
		return facet;
	}

	public void setFacet(Boolean facet) {
		this.facet = facet;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getSpecialUser() {
		return specialUser;
	}

	public void setSpecialUser(String specialUser) {
		this.specialUser = specialUser;
	}

	public String getBoostField() {
		return boostField;
	}

	public void setBoostField(String boostField) {
		this.boostField = boostField;
	}

	public String getActiveProperty() {
		return activeProperty;
	}

	public void setActiveProperty(String activeProperty) {
		this.activeProperty = activeProperty;
	}

	public String getBoostQuery() {
		return boostQuery;
	}

	public void setBoostQuery(String boostQuery) {
		this.boostQuery = boostQuery;
	}

}
