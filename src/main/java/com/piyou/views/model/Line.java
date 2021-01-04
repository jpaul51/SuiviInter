package com.piyou.views.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Line {

	List<FieldDetail> fields = new ArrayList<>();
	
	
	public Line (FieldDetail... fields) {
		this.fields = Arrays.asList(fields);
	}

	public List<FieldDetail> getFields() {
		return fields;
	}

	public void setFields(List<FieldDetail> fields) {
		this.fields = fields;
	}
	
	
	
	
}
