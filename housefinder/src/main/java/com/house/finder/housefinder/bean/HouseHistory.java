package com.house.finder.housefinder.bean;

public class HouseHistory {

	public Integer id;
	public House house;
	public HouseStatus oldStatus;
	public HouseStatus newStatus;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public HouseStatus getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(HouseStatus oldStatus) {
		this.oldStatus = oldStatus;
	}
	public HouseStatus getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(HouseStatus newStatus) {
		this.newStatus = newStatus;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HouseHistory [id=");
		builder.append(id);
		builder.append(", house=");
		builder.append(house);
		builder.append(", oldStatus=");
		builder.append(oldStatus);
		builder.append(", newStatus=");
		builder.append(newStatus);
		builder.append("]");
		return builder.toString();
	}
	
}
