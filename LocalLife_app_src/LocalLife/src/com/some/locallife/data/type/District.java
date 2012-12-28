package com.some.locallife.data.type;

public class District implements LocalType {
	private String id;
	private String cityId;
	private String name;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityId() {
		return this.cityId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
