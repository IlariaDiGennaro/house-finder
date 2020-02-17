package com.house.finder.housefinder.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.house.finder.housefinder.analyzer.util.ImmobiliareItAdsAnalyzerUtils;
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

@Service
public class ImmobiliareItAdsAnalyzer {
	
	@Autowired
	public HouseRepository houseRepository;
	@Autowired
	public HouseTmpRepository houseTmpRepository;
	@Autowired
	public HouseHistoryRepository houseHistoryRepository;
	@Autowired
	public HouseImageRepository houseImageRepository;
	@Autowired
	public HouseTmpImageRepository houseTmpImageRepository;

	

	/*
	 * Questa lista contiene gli annunci che vengono letti dal sito
	 */
	public static List<HouseTmp> houseFromSite_toBeEvaluated;
	

	
	@Transactional
	public void adsAnalyzer() throws IOException {
		
		houseFromSite_toBeEvaluated = new ArrayList<>();
		houseFromSite_toBeEvaluated = ImmobiliareItAdsAnalyzerUtils.buildHouseTmpList();
		System.out.println("Analisi IMMOBILIARE.IT finita");
		
		List<String> adIdsEvaluated = new ArrayList<>();
		
		for (HouseTmp houseTmp : houseFromSite_toBeEvaluated) {
			
			House houseFromDb = houseRepository.findByAdId(houseTmp.getAdId());
			
			if(houseFromDb != null) {
				// la casa è già presente nella parte house del DB
				
				adIdsEvaluated.add(houseFromDb.getAdId());
				
				String houseTmpPrice = houseTmp.getPrice();
				String houseFromDbPrice = houseFromDb.getPrice();
					
				if(houseFromDb.getHouseStatus().equals(HouseStatus.SELECTED)) {
					// se è selected --> controllare il prezzo --> se cambiato va aggiornato lo stato "discounted" --> update
					//				 --> update
					
					if(!houseTmpPrice.equalsIgnoreCase(houseFromDbPrice)) {
						System.out.println("> DIFFERENT PRICE - HOUSE SELECTED "+ houseFromDb.getAdId() +" "+houseFromDb.getTitle()+" | before:" + houseFromDbPrice + " after: " + houseTmpPrice);
						
						saveHouseHistory(houseFromDb, HouseStatus.DISCOUNTED);

						String priceHistory = houseFromDb.getPriceHistory();
						priceHistory = priceHistory != null ? priceHistory.concat("|").concat(houseFromDbPrice) : houseFromDbPrice;
						houseFromDb.setPriceHistory(priceHistory);
						houseFromDb.setHouseStatus(HouseStatus.DISCOUNTED);
						updateHouse(houseTmp, houseFromDb);
						continue;
					
					} else {
						System.out.println("UPDATE SELECTED HOUSE "+ houseFromDb.getTitle());
						updateHouse(houseTmp, houseFromDb);
						continue;
					}
				}
				
				if(houseFromDb.getHouseStatus().equals(HouseStatus.REJECTED)) {
					// se è REJECTED --> controllare il prezzo --> se cambiato va aggiornato lo stato "discounted" --> update
					
					if(!houseTmpPrice.equalsIgnoreCase(houseFromDbPrice)) {
						System.out.println("> DIFFERENT PRICE - HOUSE REJECTED  "+ houseFromDb.getAdId() +" "+houseFromDb.getTitle()+" | before:" + houseFromDbPrice + " after: " + houseTmpPrice);
						
						saveHouseHistory(houseFromDb, HouseStatus.DISCOUNTED);

						String priceHistory = houseFromDb.getPriceHistory();
						priceHistory = priceHistory != null ? priceHistory.concat("|").concat(houseFromDbPrice) : houseFromDbPrice;
						houseFromDb.setPriceHistory(priceHistory);
						houseFromDb.setHouseStatus(HouseStatus.DISCOUNTED);
						updateHouse(houseTmp, houseFromDb);
						continue;
					}
					continue;
				}
				
				if(houseFromDb.getHouseStatus().equals(HouseStatus.DELETED)) {
					// se è deleted --> va aggiornato lo stato e messo in "republished"
					
					System.out.println("> REPUBLISHED HOUSE - "+ houseFromDb.getAdId() +" "+houseFromDb.getTitle());
					
					saveHouseHistory(houseFromDb, HouseStatus.REPUBLISHED);

					if(!houseTmpPrice.equalsIgnoreCase(houseFromDbPrice)) {
						System.out.println("	DIFFERENT PRICE | before:" + houseFromDbPrice + " after: " + houseTmpPrice);
						String priceHistory = houseFromDb.getPriceHistory();
						priceHistory = priceHistory != null ? priceHistory.concat("|").concat(houseFromDbPrice) : houseFromDbPrice;
						houseFromDb.setPriceHistory(priceHistory);
					}
					houseFromDb.setHouseStatus(HouseStatus.REPUBLISHED);
					updateHouse(houseTmp, houseFromDb);
					continue;
				}
				
				if(houseFromDb.getHouseStatus().equals(HouseStatus.REPUBLISHED) ||
						houseFromDb.getHouseStatus().equals(HouseStatus.DISCOUNTED) ) {
					System.out.println("CHECK THIS AD "+houseFromDb.getHouseStatus()+" "+houseFromDb.getAdId()+" "+houseFromDb.getTitle());
					updateHouse(houseTmp, houseFromDb);
					continue;
				}
			}
			
			HouseTmp houseTmpFromDb = houseTmpRepository.findByAdId(houseTmp.getAdId());

			if(houseTmpFromDb != null) {
				// la casa è già presente nella parte house TMP del DB
				// bisogna aggiornare i dati
				
				updateHouseTmp(houseTmp, houseTmpFromDb);
				continue;
			
			} else {
				// CREA TUTTO L'AMBARADAM
				System.out.println("> NUOVO ANNUNCIO ! "+houseTmp.getTitle()+" "+houseTmp.getPrice()+" "+houseTmp.getMq()+" "+houseTmp.getFloor());
				houseTmpRepository.save(houseTmp);
			}
//			System.out.println("NO OPTION EVALUATED FOR THIS AD: "+ houseFromDb.getAdId() +" "+houseFromDb.getTitle());
		}
		System.out.println("Riconciliazione con DB finita");
		
		
		//SELECT HOUSE WHERE ID NOT IN houseFromSite_toBeEvaluated PER VERIFICARE LA CANCELLAZIONE DI ALCUNI ANNUNCI
		List<House> houseNotEvaluatedList = houseRepository.findByAdIdNotIn(adIdsEvaluated);
		if(houseNotEvaluatedList != null && !houseNotEvaluatedList.isEmpty()) {
			for (House house : houseNotEvaluatedList) {
				House houseDeleted = houseRepository.findFirstByHouseStatusAndAdId(HouseStatus.DELETED, house.getAdId());
				if(houseDeleted == null) {
					ImmobiliareItAdsAnalyzerUtils.checkAdAvailability(house, houseRepository, houseHistoryRepository);
				}
			}
		}
		System.out.println("Cancellazione logica con DB finita");
	}

	private void saveHouseHistory(House houseFromDb, HouseStatus houseStatus) {
		HouseHistory houseHistory = new HouseHistory();
		houseHistory.setHouse(houseFromDb);
		houseHistory.setOldStatus(houseFromDb.getHouseStatus());
		houseHistory.setNewStatus(HouseStatus.DISCOUNTED);
		houseHistory.setChangeDatetime(new Date());
		houseHistoryRepository.save(houseHistory);
	}
	
	private void updateHouse(HouseTmp houseTmp_toBeEvaluated, House house) {

		List<HouseTmpImage> houseTmpImageList = houseTmp_toBeEvaluated.getHouseTmpImages();
		for (HouseTmpImage houseTmpImage : houseTmpImageList) {
			HouseImage houseImageFound = houseImageRepository.findByUrl(houseTmpImage.getUrl());
			if(houseImageFound == null) {
				HouseImage houseImage = new HouseImage();
				houseImage.setHouse(house);
				houseImage.setHouseImageType(houseTmpImage.getHouseImageType());
				houseImage.setUrl(houseTmpImage.getUrl());
				houseImageRepository.save(houseImage);
			}
		}

		house.setAdId(houseTmp_toBeEvaluated.getAdId());
		house.setTitle(houseTmp_toBeEvaluated.getTitle());
		house.setLink(houseTmp_toBeEvaluated.getLink());
		house.setDescription(houseTmp_toBeEvaluated.getDescription());
		house.setPrice(houseTmp_toBeEvaluated.getPrice());
		house.setRooms(houseTmp_toBeEvaluated.getRooms());
		house.setMq(houseTmp_toBeEvaluated.getMq());
		house.setWcs(houseTmp_toBeEvaluated.getWcs());
		house.setFloor(houseTmp_toBeEvaluated.getFloor());
		house.setAgency(houseTmp_toBeEvaluated.getAgency());
		house.setPhoneNumbersAgency(houseTmp_toBeEvaluated.getPhoneNumbersAgency());
		house.setAdRif(houseTmp_toBeEvaluated.getAdRif());
		houseRepository.save(house);
	}
	
	private void updateHouseTmp(HouseTmp houseTmp_toBeEvaluated, HouseTmp houseTmp) {

		List<HouseTmpImage> houseTmpImageList = houseTmp_toBeEvaluated.getHouseTmpImages();
		for (HouseTmpImage houseTmpImage : houseTmpImageList) {
			//			System.out.println(houseTmpImage.getUrl());
			List<HouseTmpImage> houseImageFoundList = houseTmpImageRepository.findByUrlAndHouseTmp(houseTmpImage.getUrl(), houseTmp);
			if(houseImageFoundList == null || houseImageFoundList.isEmpty() )  {
				houseTmpImage.setHouseTmp(houseTmp);
				houseTmpImageRepository.save(houseTmpImage);
			}
		}

		houseTmp.setAdId(houseTmp_toBeEvaluated.getAdId());
		houseTmp.setTitle(houseTmp_toBeEvaluated.getTitle());
		houseTmp.setLink(houseTmp_toBeEvaluated.getLink());
		houseTmp.setDescription(houseTmp_toBeEvaluated.getDescription());
		houseTmp.setPrice(houseTmp_toBeEvaluated.getPrice());
		houseTmp.setRooms(houseTmp_toBeEvaluated.getRooms());
		houseTmp.setMq(houseTmp_toBeEvaluated.getMq());
		houseTmp.setWcs(houseTmp_toBeEvaluated.getWcs());
		houseTmp.setFloor(houseTmp_toBeEvaluated.getFloor());
		houseTmp.setAgency(houseTmp_toBeEvaluated.getAgency());
		houseTmp.setPhoneNumbersAgency(houseTmp_toBeEvaluated.getPhoneNumbersAgency());
		houseTmp.setAdRif(houseTmp_toBeEvaluated.getAdRif());
		//		houseTmp.setAdDate(houseTmp_toBeEvaluated.getAdDate());
		//		houseTmp.setHouseImages(houseTmp_toBeEvaluated.getHouseImages());
		houseTmpRepository.save(houseTmp);
	}
}
