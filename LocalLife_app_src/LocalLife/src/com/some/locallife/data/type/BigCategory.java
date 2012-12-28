package com.some.locallife.data.type;

public class BigCategory implements LocalType{

	private String name;
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
