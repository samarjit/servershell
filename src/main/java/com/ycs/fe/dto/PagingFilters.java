package com.ycs.fe.dto;

import java.util.List;

public class PagingFilters {
private String groupOp;
private List<PagingFilterRule> rules;
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


}
