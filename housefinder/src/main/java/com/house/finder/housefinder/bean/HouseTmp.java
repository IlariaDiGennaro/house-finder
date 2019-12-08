package com.house.finder.housefinder.bean;

import java.util.List;

public class HouseTmp {

	private Integer id;
	private String adId;
	private String title;
	private String link;
	private String description;
	private String price;
	private String rooms;
	private String mq;
	private String wcs;
	private String floor;
	private String agency;
	private String phoneNumbersAgency;
	private String adRif;
	private String adDate;
	private List<HouseImage> houseImages;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAdId() {
		return adId;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRooms() {
		return rooms;
	}
	public void setRooms(String rooms) {
		this.rooms = rooms;
	}
	public String getMq() {
		return mq;
	}
	public void setMq(String mq) {
		this.mq = mq;
	}
	public String getWcs() {
		return wcs;
	}
	public void setWcs(String wcs) {
		this.wcs = wcs;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getPhoneNumbersAgency() {
		return phoneNumbersAgency;
	}
	public void setPhoneNumbersAgency(String phoneNumbersAgency) {
		this.phoneNumbersAgency = phoneNumbersAgency;
	}
	public String getAdRif() {
		return adRif;
	}
	public void setAdRif(String adRif) {
		this.adRif = adRif;
	}
	public String getAdDate() {
		return adDate;
	}
	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}
	public List<HouseImage> getHouseImages() {
		return houseImages;
	}
	public void setHouseImages(List<HouseImage> houseImages) {
		this.houseImages = houseImages;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HouseTmp [id=");
		builder.append(id);
		builder.append(", adId=");
		builder.append(adId);
		builder.append(", title=");
		builder.append(title);
		builder.append(", link=");
		builder.append(link);
		builder.append(", description=");
		builder.append(description);
		builder.append(", price=");
		builder.append(price);
		builder.append(", rooms=");
		builder.append(rooms);
		builder.append(", mq=");
		builder.append(mq);
		builder.append(", wcs=");
		builder.append(wcs);
		builder.append(", floor=");
		builder.append(floor);
		builder.append(", agency=");
		builder.append(agency);
		builder.append(", phoneNumbersAgency=");
		builder.append(phoneNumbersAgency);
		builder.append(", adRif=");
		builder.append(adRif);
		builder.append(", adDate=");
		builder.append(adDate);
		builder.append(", houseImages=");
		builder.append(houseImages);
		builder.append("]");
		return builder.toString();
	}
	
}
