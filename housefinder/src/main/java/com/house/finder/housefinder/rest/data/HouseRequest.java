package com.house.finder.housefinder.rest.data;

import java.io.Serializable;

public class HouseRequest implements Serializable {

	private static final long serialVersionUID = 733428205019517850L;
	
	private String adId;
	private String note;
	
	public String getAdId() {
		return adId;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
