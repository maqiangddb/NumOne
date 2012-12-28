package com.some.locallife.data.type;

public class Category implements LocalType{
	private String id;
	private String name;
	private String parentId;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setParentId(String id) {
		this.parentId = id;
	}

	public String getParentId() {
		return this.parentId;
	}

}
