package com.lexter.sample;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Table {
	
	private List arrList;
	private Set setOfKeys;

	public Table() {
		arrList = new ArrayList<LinkedHashMap<String, String>>();
		setOfKeys = new HashSet<String>();
	}

	public Table(List<LinkedHashMap<String, String>> arrList) {
		this();
		this.arrList = arrList;
	}

	public void setArrList(List arrList) {
		this.arrList = arrList;
	}

	public List getArrList() {
		return arrList;
	}

	public Set getKeys() {
		return setOfKeys;
	}

	public boolean addKey(String key) {
		return setOfKeys.add(key);
	}

}