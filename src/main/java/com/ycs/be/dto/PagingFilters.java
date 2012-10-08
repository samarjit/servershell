package com.ycs.be.dto;

import java.util.List;



public class PagingFilters {
	
private String groupOp;
private List<PagingFilterRule> rules;
private List<PagingFilters> groups;

public String getGroupOp() {
	return groupOp;
}
public void setGroupOp(String groupOp) {
	this.groupOp = groupOp;
}
public List<PagingFilterRule> getRules() {
	return rules;
}
public void setRules(List<PagingFilterRule> rules) {
	this.rules = rules;
}
@Override
public String toString() {
	return "PagingFilters [groupOp=" + groupOp + ", rules=" + rules + "]";
}

public List<PagingFilters> getGroups() {
	return groups;
}
public void setGroups(List<PagingFilters> groups) {
	this.groups = groups;
}

}
