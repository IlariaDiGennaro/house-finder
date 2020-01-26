package com.house.finder.housefinder.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "house")
public class House {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "ad_id", nullable = false)
	private String adId;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "link", nullable = false)
	private String link;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "price", nullable = false)
	private String price;
	@Column(name = "price_history")
	private String priceHistory;
	@Column(name = "rooms", nullable = false)
	private String rooms;
	@Column(name = "mq", nullable = false)
	private String mq;
	@Column(name = "wcs")
	private String wcs;
	@Column(name = "floor")
	private String floor;
	@Column(name = "agency")
	private String agency;
	@Column(name = "phone_numbers_agency", nullable = false)
	private String phoneNumbersAgency;
	@Column(name = "ad_rif", nullable = false)
	private String adRif;
	@Column(name = "ad_date", nullable = false)
	private String adDate;
	@Column(name = "note")
	private String note;
	@Column(name = "resub_counter", nullable = false)
	private Integer resubCounter;
	@Column(name = "house_status", nullable = false)
	@Enumerated(EnumType.STRING)
    private HouseStatus houseStatus;
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "house")
	private List<HouseImage> houseImages;
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "house")
	private List<HouseHistory> houseHistories;
	
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
	public String getPriceHistory() {
		return priceHistory;
	}
	public void setPriceHistory(String priceHistory) {
		this.priceHistory = priceHistory;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getResubCounter() {
		return resubCounter;
	}
	public void setResubCounter(Integer resubCounter) {
		this.resubCounter = resubCounter;
	}
	public HouseStatus getHouseStatus() {
		return houseStatus;
	}
	public void setHouseStatus(HouseStatus houseStatus) {
		this.houseStatus = houseStatus;
	}
	public List<HouseHistory> getHouseHistories() {
		return houseHistories;
	}
	public void setHouseHistories(List<HouseHistory> houseHistories) {
		this.houseHistories = houseHistories;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("House [id=");
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
		builder.append(", priceHistory=");
		builder.append(priceHistory);
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
		builder.append(houseImages.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append(", resubCounter=");
		builder.append(resubCounter);
		builder.append(", houseStatus=");
		builder.append(houseStatus);
		builder.append(", houseHistories=");
		builder.append(houseHistories);
		builder.append("]");
		return builder.toString();
	}
	
}
