package com.house.finder.housefinder.bean;

import java.util.Date;

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
@Table(name = "house_history")
public class HouseHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
//	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "house_id")
	public House house;
	@Enumerated(EnumType.STRING)
	@Column(name = "house_status_old", nullable = false)
	public HouseStatus oldStatus;
	@Enumerated(EnumType.STRING)
	@Column(name = "house_status_new", nullable = false)
	public HouseStatus newStatus;
	@Column(name = "change_datetime", nullable = false)
	public Date changeDatetime;
	
	
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
	public Date getChangeDatetime() {
		return changeDatetime;
	}
	public void setChangeDatetime(Date changeDatetime) {
		this.changeDatetime = changeDatetime;
	}
	
}
