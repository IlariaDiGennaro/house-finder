package com.house.finder.housefinder.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.House;
import com.house.finder.housefinder.bean.HouseHistory;
import com.house.finder.housefinder.bean.HouseImage;
import com.house.finder.housefinder.bean.HouseStatus;
import com.house.finder.housefinder.bean.HouseTmp;
import com.house.finder.housefinder.bean.HouseTmpImage;
import com.house.finder.housefinder.repository.HouseHistoryRepository;
import com.house.finder.housefinder.repository.HouseImageRepository;
import com.house.finder.housefinder.repository.HouseRepository;
import com.house.finder.housefinder.repository.HouseTmpImageRepository;
import com.house.finder.housefinder.repository.HouseTmpRepository;
import com.house.finder.housefinder.rest.data.RejectHouseRequest;
import com.house.finder.housefinder.rest.data.SelectHouseRequest;

@Service
public class HouseManager {

	@Autowired
	public HouseTmpRepository houseTmpRepository;
	@Autowired
	public HouseRepository houseRepository;
	@Autowired
	public HouseImageRepository houseImageRepository;
	@Autowired
	public HouseTmpImageRepository houseTmpImageRepository;
	@Autowired
	public HouseHistoryRepository houseHistoryRepository;

	
	//TODO retrieve house by status
	
	private void changeHouseStatus(String adId, String note, HouseStatus newStatus) {
		
		HouseTmp houseTmp = houseTmpRepository.findByAdId(adId);

		if(houseTmp != null) {

			House house = buildHouse(houseTmp);
			house.setHouseStatus(newStatus);
			house.setNote(note);
			house.setResubCounter(0);

			House houseToUpdate = houseRepository.save(house);
			
			List<HouseTmpImage> houseTmpImageList = houseTmp.getHouseTmpImages();
			for (HouseTmpImage houseTmpImage : houseTmpImageList) {
				
				HouseImage houseImage = new HouseImage();
				houseImage.setUrl(houseTmpImage.getUrl());
				houseImage.setHouseImageType(houseTmpImage.getHouseImageType());
				houseImage.setHouse(houseToUpdate);
				houseImageRepository.save(houseImage);
			}
			
			HouseHistory houseHistory = new HouseHistory();
			houseHistory.setHouse(houseToUpdate);
			houseHistory.setOldStatus(HouseStatus.NEW);
			houseHistory.setNewStatus(newStatus);
			houseHistory.setChangeDatetime(new Date());
			houseHistoryRepository.save(houseHistory);

			houseTmpImageRepository.delete(houseTmp.getHouseTmpImages());
			houseTmpRepository.delete(houseTmp);

		} else {
			House house = houseRepository.findByAdId(adId);

			if(house != null && isNewStatusAlloewd(house.getHouseStatus(), newStatus)) {

				String houseNote = house.getNote();
				if(houseNote != null) {
					houseNote = newStatus.name().concat(note).concat("\n").concat(houseNote);
				} else {
					houseNote = newStatus.name().concat(note);
				}
				house.setNote(houseNote);
				house.setHouseStatus(newStatus);
				houseRepository.save(house);

				HouseHistory houseHistory = new HouseHistory();
				houseHistory.setHouse(house);
				houseHistory.setOldStatus(house.getHouseStatus());
				houseHistory.setNewStatus(newStatus);
				houseHistory.setChangeDatetime(new Date());
				houseHistoryRepository.save(houseHistory);

			}else {
				//TODO eccezione
			}
		}
	}
	
	public void rejectHouse(RejectHouseRequest rejectHouseRequest) {
		changeHouseStatus(rejectHouseRequest.getAdId(), rejectHouseRequest.getNote(), HouseStatus.REJECTED);
	}
	
	public void selectHouse(SelectHouseRequest selectHouseRequest) {
		changeHouseStatus(selectHouseRequest.getAdId(), selectHouseRequest.getNote(), HouseStatus.SELECTED);
	}

	private boolean isNewStatusAlloewd(HouseStatus oldStatus, HouseStatus newStatus ) {
		
		if(oldStatus.equals(newStatus))
			return false;
		
//		if(oldStatus.equals(HouseStatus.NEW))
//			return true;
//		else if(oldStatus.equals(HouseStatus.REJECTED))
//			return true;
//		else if(oldStatus.equals(HouseStatus.SELECTED) && !newStatus.equals(HouseStatus.NEW))
//			return true;
			
		return true;
	}
	
	private House buildHouse(HouseTmp houseTmp) {
		
		House house = new House();
		house.setAdId(houseTmp.getAdId());
		house.setTitle(houseTmp.getTitle());
		house.setLink(houseTmp.getLink());
		house.setDescription(houseTmp.getDescription());
		house.setPrice(houseTmp.getPrice());
		house.setRooms(houseTmp.getRooms());
		house.setMq(houseTmp.getMq());
		house.setWcs(houseTmp.getWcs());
		house.setFloor(houseTmp.getFloor());
		house.setAgency(houseTmp.getAgency());
		house.setPhoneNumbersAgency(houseTmp.getPhoneNumbersAgency());
		house.setAdRif(houseTmp.getAdRif());
		house.setAdDate(houseTmp.getAdDate());

		return house;
	}
}
