package com.piyou.views.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableLayoutManager {

	
	List<FieldDetail> columns = new ArrayList<>();

	ResultView defaultResultView;
	
	Set<ResultView> resultView = new HashSet<>();
	
	
	public List<FieldDetail> getColumns() {
		return columns;
	}

	public void setColumns(List<FieldDetail> columns) {
		this.columns = columns;
	}

	public ResultView getDefaultResultView() {
		return defaultResultView;
	}

	public void setDefaultResultView(ResultView defaultResultView) {
		this.defaultResultView = defaultResultView;
	}

	public Set<ResultView> getResultView() {
		return resultView;
	}

	public void setResultView(Set<ResultView> resultView) {
		this.resultView = resultView;
	}
	
	
	
	
	
}
