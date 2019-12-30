package com.house.finder.housefinder.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house_tmp_image")
public class HouseTmpImage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
//	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name = "house_tmp_id")
	public HouseTmp houseTmp;
	@Column(name = "url", nullable = false)
	public String url;
	@Enumerated(EnumType.STRING)
	@Column(name = "house_image_type", nullable = false)
    private HouseImageType houseImageType;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HouseImageType getHouseImageType() {
		return houseImageType;
	}
	public void setHouseImageType(HouseImageType houseImageType) {
		this.houseImageType = houseImageType;
	}
}
