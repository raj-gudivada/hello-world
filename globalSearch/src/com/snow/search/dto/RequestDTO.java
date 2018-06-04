package com.snow.search.dto;

import java.util.List;

public class RequestDTO {
	private List<String> userRoles;
	private String userId;
	private String vendorId;
	private String searchContext;
	private String searchText;
	private String sortBy;
	private String mode;
	private String userType;
	private String queryParam;
	private Integer start;
	private Integer rows;
	private List<String> searchType;
	private List<String> facetSelection;
	private Integer maxRows;

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSearchContext() {
		return searchContext;
	}

	public void setSearchContext(String searchContext) {
		this.searchContext = searchContext;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public List<String> getSearchType() {
		return searchType;
	}

	public void setSearchType(List<String> searchType) {
		this.searchType = searchType;
	}

	public List<String> getFacetSelection() {
		return facetSelection;
	}

	public void setFacetSelection(List<String> facetSelection) {
		this.facetSelection = facetSelection;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

}
