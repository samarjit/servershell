package com.ycs.be.dto;



public class PaginationDTO {

	private int page; //pageno
	private int rows; //number of rows per page
	private String sidx; //order by sidx
	private String sord; //asc or desc
	private String searchField;
	private String searchString;
	private String searchOper;
	private PagingFilters filters;
	
	public PaginationDTO(int page,int rows,String sidx,String sord){
		this.page = page;
		this.rows = rows;
		this.sidx = sidx;
		this.sord = sord;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}
	public PaginationDTO(){
		
	}
	
		
	public PagingFilters getFilters() {
		return filters;
	}

	public void setFilters(PagingFilters filters) {
		this.filters = filters;
	}

	public static boolean sqlInjectionCheck(String s){
		return s.matches("[a-zA-Z0-9%_]*");
	}

	@Override
	public String toString() {
		return "PaginationDTO [page=" + page + ", rows=" + rows + ", sidx=" + sidx + ", sord=" + sord + ", searchField=" + searchField + ", searchString=" + searchString + ", searchOper="
				+ searchOper + ", filters=" + filters + "]";
	}
	
	
}
