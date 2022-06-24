package com.vmo.utils.common;

//import com.vmo.utils.common.Sort;

public class Item {
	private String name;
	private int property;

	public void Product(String name, int property) {
		this.name = name;
		this.property = property;
	}

	public int getProperty() {
		return property;
	}
	
	public String getName() {
		return name;
	}
}
