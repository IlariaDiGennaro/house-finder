package com.house.finder.housefinder.bean;

public class HouseImage {

	public Integer id;
	public HouseTmp houseTmp;
	public House house;
	public String url;
	public String type;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public HouseTmp getHouseTmp() {
		return houseTmp;
	}
	public void setHouseTmp(HouseTmp houseTmp) {
		this.houseTmp = houseTmp;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HouseImage [id=");
		builder.append(id);
		builder.append(", houseTmp=");
		builder.append(houseTmp);
		builder.append(", house=");
		builder.append(house);
		builder.append(", url=");
		builder.append(url);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
	
}
