package com.house.finder.housefinder.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.house.finder.housefinder.bean.House;
import com.house.finder.housefinder.bean.HouseHistory;
import com.house.finder.housefinder.bean.HouseImage;
import com.house.finder.housefinder.bean.HouseImageType;
import com.house.finder.housefinder.bean.HouseStatus;
import com.house.finder.housefinder.bean.HouseTmp;
import com.house.finder.housefinder.bean.HouseTmpImage;
import com.house.finder.housefinder.repository.HouseHistoryRepository;
import com.house.finder.housefinder.repository.HouseImageRepository;
import com.house.finder.housefinder.repository.HouseRepository;
import com.house.finder.housefinder.repository.HouseTmpImageRepository;
import com.house.finder.housefinder.repository.HouseTmpRepository;
import com.house.finder.housefinder.site.bean.ImmobiliareIt;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

//@Service
public class AdsAnalyzer {

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


	public static List<HouseTmp> houseTmpList_toBeEvaluated;

	private House checkChangedPrice(List<House> houseRejectedList, HouseTmp houseTmp_toBeEvaluated) {
		for (House house : houseRejectedList) {

			if(house.getAdId().equalsIgnoreCase(houseTmp_toBeEvaluated.getAdId()) &&
					!house.getPrice().equalsIgnoreCase(houseTmp_toBeEvaluated.getPrice()) ) {
				return house;
			}
		}
		return null;
	}

	private boolean checkAlreadyRejected(List<House> houseRejectedList, HouseTmp houseTmp_toBeEvaluated) {
		for (House house : houseRejectedList) {

			if(house.getAdId().equalsIgnoreCase(houseTmp_toBeEvaluated.getAdId())) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	public void adsAnalyzer() throws IOException {

		houseTmpList_toBeEvaluated = new ArrayList<>();
		List<House> houseRejectedList = houseRepository.findByHouseStatus(HouseStatus.REJECTED);

		Integer maxPage = 9999;
		String immobiliareItUrl = ImmobiliareIt.getImmobilareItUrl();

		for (int pageNum = 1; pageNum <= maxPage; pageNum++) {

			Document adsListHtmlPage = null;
			if(pageNum == 1) {
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl).get();
//				maxPage = getLastPageNumber(adsListHtmlPage);
				System.out.println("Numero di pagine: " + maxPage);
			} else {
				String page = String.valueOf(pageNum);
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl.concat("&pag=").concat(page)).get();
			}
			System.out.println("Analisi pagina: " + pageNum);

			Elements adsIdLIst = adsListHtmlPage.select("p.titolo.text-primary");
			for (Element adsIdElem : adsIdLIst) {

				String adLink = null;//getAdLink(adsIdElem);
				String adId = null;//getAdId(adLink);

				if(!adId.startsWith("p-")) {

					HouseTmp houseTmp_toBeEvaluated = buildHouseTmp(adLink, adId);
					House housePreviuslyRejected = checkChangedPrice(houseRejectedList, houseTmp_toBeEvaluated);
					if(housePreviuslyRejected != null) {

						System.out.println("DIFFERENT HOUSE PRICE (BEFORE WAS REJECTED)");
						
						List<HouseHistory> houseHistoryListPreviuslyRejected = housePreviuslyRejected.getHouseHistories();
						int size = houseHistoryListPreviuslyRejected.size();
						
						HouseHistory houseHistory = new HouseHistory();
						houseHistory.setHouse(housePreviuslyRejected);
						houseHistory.setOldStatus(houseHistoryListPreviuslyRejected.get(size-1).getNewStatus());
						houseHistory.setNewStatus(HouseStatus.NEW);
						houseHistory.setChangeDatetime(new Date());
						houseHistoryRepository.save(houseHistory);
						
						houseTmp_toBeEvaluated.setHouseTmpImages(null);
						
						houseTmpRepository.save(houseTmp_toBeEvaluated);
												
					} else if (!checkAlreadyRejected(houseRejectedList, houseTmp_toBeEvaluated)) {

						House houseSelected = houseRepository.findFirstByHouseStatusAndAdId(HouseStatus.SELECTED, adId);
						HouseTmp houseTmp = houseTmpRepository.findFirstByAdId(adId);

						if(houseTmp != null || houseSelected != null) {
							if(houseTmp != null) {
								System.out.println("update house tmp: "+houseTmp.getAdId());
								updateHouseTmp(houseTmp_toBeEvaluated, houseTmp);
							} else if(houseSelected != null) {
								System.out.println("update house selected: "+houseSelected.getAdId());
								updateHouseSelected(houseTmp_toBeEvaluated, houseSelected);
							}
						} else {

							System.out.println("NEW HOUSE ads!!!");

							House houseOriginalResub = checkAdResubmission(houseTmp_toBeEvaluated);
							if( houseOriginalResub != null ) {

								List<HouseHistory> oldHistory = houseOriginalResub.getHouseHistories();
								List<HouseImage> oldImage = houseOriginalResub.getHouseImages();

								HouseHistory houseHistory = new HouseHistory();
								houseHistory.setHouse(houseOriginalResub);
								houseHistory.setOldStatus(houseOriginalResub.getHouseStatus());
								houseHistory.setNewStatus(HouseStatus.DELETED);
								houseHistory.setChangeDatetime(new Date());
								houseHistoryRepository.save(houseHistory);

								houseOriginalResub.setHouseStatus(HouseStatus.DELETED);
								String resubNote = houseOriginalResub.getNote();
								resubNote.concat(" RESUB: ").concat(houseOriginalResub.getAdId());

								int originalResubCounter = houseOriginalResub.getResubCounter();
								House houseResub = buildHouse(houseTmp_toBeEvaluated, houseOriginalResub);
								houseResub.setResubCounter(originalResubCounter++);
								houseResub.setAdDate(houseOriginalResub.getAdDate());
								houseResub.setNote(resubNote);

								for (HouseHistory oldhouseHistory : oldHistory) {
									oldhouseHistory.setHouse(houseResub);
								}
								houseResub.setHouseHistories(oldHistory);

								for (HouseImage oldHouseImage : oldImage) {
									oldHouseImage.setHouse(houseResub);
								}
								houseResub.setHouseImages(oldImage);

								houseRepository.saveAndFlush(houseOriginalResub);
								houseRepository.saveAndFlush(houseResub);

							} else {
								houseTmpRepository.save(houseTmp_toBeEvaluated);
							}
						}
					}
				} else {
					//					System.out.println("L'annuncio riguarda una casa già rigettata od è una nuova costruzione");
				}
			}
		}

		List<House> houseSelectedList = houseRepository.findByHouseStatus(HouseStatus.SELECTED);

		for (House house : houseSelectedList) {
			checkAdAvailability(house);
		}

		//check availability tmp
	}

	private void checkAdAvailability(House house) {
		try {
			Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(house.getAdId())).get();
		} catch (HttpStatusException e) {
			HouseHistory houseHistory = new HouseHistory();
			houseHistory.setHouse(house);
			houseHistory.setOldStatus(house.getHouseStatus());
			houseHistory.setNewStatus(HouseStatus.DELETED);
			houseHistory.setChangeDatetime(new Date());
			houseHistoryRepository.save(houseHistory);

			house.setHouseStatus(HouseStatus.DELETED);
			houseRepository.save(house);
		} catch (Exception e) {
			//DO NOTHING
			System.out.println("Connection failure");
		}
	}

	private House checkAdResubmission(HouseTmp houseTmp_toBeEvaluated) {
		List<House> houseSelectedList = houseRepository.findByHouseStatus(HouseStatus.SELECTED);
		Integer equalsCounter = 0;
		for (House houseSelected : houseSelectedList) {

			if(houseTmp_toBeEvaluated.getTitle().trim().equals(houseSelected.getTitle().trim())) {
				equalsCounter++;
			}
			//			if(houseTmp_toBeEvaluated.getDescription().trim().equals(houseSelected.getDescription().trim())) {
			//				equalsCounter++;
			//			}
			if(houseTmp_toBeEvaluated.getPrice().trim().equals(houseSelected.getPrice().trim())) {
				equalsCounter++;
			}
			if(houseTmp_toBeEvaluated.getRooms().trim().equals(houseSelected.getRooms().trim())) {
				equalsCounter++;
			}
			if(houseTmp_toBeEvaluated.getMq().trim().equals(houseSelected.getMq().trim())) {
				equalsCounter++;
			}
			if(houseTmp_toBeEvaluated.getWcs().trim().equals(houseSelected.getWcs().trim())) {
				equalsCounter++;
			}
			//			if(houseTmp_toBeEvaluated.getFloor().trim().equals(houseSelected.getFloor().trim())) {
			//				equalsCounter++;
			//			}
			//			if(houseTmp_toBeEvaluated.getAgency().trim().equals(houseSelected.getAgency().trim())) {
			//				equalsCounter++;
			//			}
			if(houseTmp_toBeEvaluated.getAdRif().trim().equals(houseSelected.getAdRif().trim())) {
				equalsCounter++;
			}

			if(equalsCounter>=5) {
				System.out.println("\n\nANNUNCIO SOSPETTO");
				System.out.println("houseTmp_tBE\t\thouseSelected");
				System.out.println(houseTmp_toBeEvaluated.getTitle()+"\t\t"+houseSelected.getTitle());
				//				System.out.println(houseTmp_toBeEvaluated.getDescription()+"\t\t"+houseSelected.getDescription());
				System.out.println(houseTmp_toBeEvaluated.getPrice()+"\t\t"+houseSelected.getPrice());
				System.out.println(houseTmp_toBeEvaluated.getRooms()+"\t\t"+houseSelected.getRooms());
				System.out.println(houseTmp_toBeEvaluated.getMq()+"\t\t"+houseSelected.getMq());
				System.out.println(houseTmp_toBeEvaluated.getWcs()+"\t\t"+houseSelected.getWcs());
				//				System.out.println(houseTmp_toBeEvaluated.getFloor()+"\t\t"+houseSelected.getFloor());
				//				System.out.println(houseTmp_toBeEvaluated.getAgency()+"\t\t"+houseSelected.getAgency());
				System.out.println(houseTmp_toBeEvaluated.getAdRif()+"\t\t"+houseSelected.getAdRif());

				try {
					Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(houseSelected.getAdId())).get();
				} catch (HttpStatusException e) {
					System.out.println("There's a resub");
					return houseSelected;
				} catch (IOException e) {
					//DO NOTHING
					System.out.println("Connection failure");
				}
			}
			equalsCounter = 0;
		}
		return null;
	}

	private House buildHouse(HouseTmp houseTmp_toBeEvaluated, House houseSelected) {

		houseSelected.setAdId(houseTmp_toBeEvaluated.getAdId());
		houseSelected.setTitle(houseTmp_toBeEvaluated.getTitle());
		houseSelected.setLink(houseTmp_toBeEvaluated.getLink());
		houseSelected.setDescription(houseTmp_toBeEvaluated.getDescription());
		houseSelected.setPrice(houseTmp_toBeEvaluated.getPrice());
		houseSelected.setRooms(houseTmp_toBeEvaluated.getRooms());
		houseSelected.setMq(houseTmp_toBeEvaluated.getMq());
		houseSelected.setWcs(houseTmp_toBeEvaluated.getWcs());
		houseSelected.setFloor(houseTmp_toBeEvaluated.getFloor());
		houseSelected.setAgency(houseTmp_toBeEvaluated.getAgency());
		houseSelected.setPhoneNumbersAgency(houseTmp_toBeEvaluated.getPhoneNumbersAgency());
		houseSelected.setAdRif(houseTmp_toBeEvaluated.getAdRif());

		return houseSelected;
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

	private void updateHouseSelected(HouseTmp houseTmp_toBeEvaluated, House houseSelected) {

		List<HouseTmpImage> houseTmpImageList = houseTmp_toBeEvaluated.getHouseTmpImages();
		for (HouseTmpImage houseTmpImage : houseTmpImageList) {
			HouseImage houseImageFound = houseImageRepository.findByUrl(houseTmpImage.getUrl());
			if(houseImageFound == null) {
				HouseImage houseImage = new HouseImage();
				houseImage.setHouse(houseSelected);
				houseImage.setHouseImageType(houseTmpImage.getHouseImageType());
				houseImage.setUrl(houseTmpImage.getUrl());
				houseImageRepository.save(houseImage);
			}
		}

		houseSelected.setAdId(houseTmp_toBeEvaluated.getAdId());
		houseSelected.setTitle(houseTmp_toBeEvaluated.getTitle());
		houseSelected.setLink(houseTmp_toBeEvaluated.getLink());
		houseSelected.setDescription(houseTmp_toBeEvaluated.getDescription());
		houseSelected.setPrice(houseTmp_toBeEvaluated.getPrice());
		houseSelected.setRooms(houseTmp_toBeEvaluated.getRooms());
		houseSelected.setMq(houseTmp_toBeEvaluated.getMq());
		houseSelected.setWcs(houseTmp_toBeEvaluated.getWcs());
		houseSelected.setFloor(houseTmp_toBeEvaluated.getFloor());
		houseSelected.setAgency(houseTmp_toBeEvaluated.getAgency());
		houseSelected.setPhoneNumbersAgency(houseTmp_toBeEvaluated.getPhoneNumbersAgency());
		houseSelected.setAdRif(houseTmp_toBeEvaluated.getAdRif());
		//		houseSelected.setAdDate(houseTmp_toBeEvaluated.getAdDate());
		//		houseSelected.setHouseImages(houseTmp_toBeEvaluated.getHouseImages());
		houseRepository.save(houseSelected);
	}

	private HouseTmp buildHouseTmp(String adLink, String adId) throws IOException {
		// build a house_tmp bean
		HouseTmp houseTmp = new HouseTmp();
		houseTmp.setAdId(adId);
		houseTmp.setLink(adLink);
		//		System.out.println(adLink);

		// oppure direttamente 'adLink'
		Document adHtmlPage = Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(adId)).get();

		String title = adHtmlPage.select("h1.raleway.title-detail").text();
		houseTmp.setTitle(title);

		String description = adHtmlPage.select("div.col-xs-12.description-text.text-compressed").text();
		houseTmp.setDescription(description);

		String priceRough = adHtmlPage.select("li.features__price").text();
		String priceBeautify = null;
		if(priceRough.lastIndexOf("€")!=0) {
			//remove old price
			priceBeautify = priceRough.substring(0, priceRough.indexOf("€", 1)-1);
		} else {
			priceBeautify = priceRough;
		}
		houseTmp.setPrice(priceBeautify);

		Elements ul = adHtmlPage.select("ul.list-inline.list-piped.features__list");
		Elements lis = ul.select("li"); //ul.select("li.lif__item");

		for (int j = 0; j < lis.size(); j++) {
			String homeDataToDefine = lis.get(j).text();
			if(homeDataToDefine.contains("local")) {
				houseTmp.setRooms(homeDataToDefine);
				continue;
			}else if(homeDataToDefine.contains("m2")) {
				String m2Beautify = homeDataToDefine.replaceAll("superficie", "").trim();
				houseTmp.setMq(m2Beautify);
				continue;
			}else if(homeDataToDefine.contains("bagn")) {
				houseTmp.setWcs(homeDataToDefine);
				continue;
			}else if(homeDataToDefine.contains("pian")) {
				houseTmp.setFloor(homeDataToDefine);
				continue;
			}
		}


		if(adHtmlPage.select("p.contact-data__name").first() != null) {
			String agency = adHtmlPage.select("p.contact-data__name").first().text();
			houseTmp.setAgency(agency);

			String phoneNumbersAgency = "";
			Elements spans = adHtmlPage.select("span.btn.btn-white.col-xs-12.info-agenzia.hidden");
			int spansLength = spans.size()/2;
			for (int i = 0; i < spansLength; i++) {
				if(spansLength == i+1) {
					phoneNumbersAgency = phoneNumbersAgency.concat(spans.get(i).text());
				}else {
					phoneNumbersAgency = phoneNumbersAgency.concat(spans.get(i).text()).concat(",");
				}
			}
			houseTmp.setPhoneNumbersAgency(phoneNumbersAgency);

		}else if(adHtmlPage.select("div.contact-data--private").first() != null) {
			Element divPrivate = adHtmlPage.select("div.contact-data--private").first();
			String agency = divPrivate.select("p.profile__name").text();
			houseTmp.setAgency(agency);

			String phoneNumbersAgency = "";
			Elements btns = adHtmlPage.select("span.btn.btn-white.info-private");
			int btnsLength = btns.size()/2;
			for (int i = 0; i < btnsLength; i++) {
				Element src = btns.get(i).select("img[src]").first();
				if(btnsLength == i+1) {
					phoneNumbersAgency = phoneNumbersAgency.concat(src.attr("src"));
				}else {
					phoneNumbersAgency = phoneNumbersAgency.concat(src.attr("src")).concat(",");
				}
			}
			houseTmp.setPhoneNumbersAgency(phoneNumbersAgency);

		}else if(adHtmlPage.select("a.thumbnail.link-no-decoration").first() != null) {
			Element aBank = adHtmlPage.select("a.thumbnail.link-no-decoration").first();
			Element imgBank = aBank.select("img[alt]").first();
			houseTmp.setAgency(imgBank.attr("alt"));

			String phoneNumbersAgency = "";
			Elements ps = adHtmlPage.select("p.text-success.custom-orari");
			int psLength = ps.size()/2;
			for (int i = 0; i < psLength; i++) {
				phoneNumbersAgency = ps.get(i).text();
			}
			houseTmp.setPhoneNumbersAgency(phoneNumbersAgency);
		}

		Elements dls = adHtmlPage.select("dl.col-xs-12");
		String adRifAdDate = dls.select("dd.col-xs-12.col-sm-7").first().text();
		int adRifSeparatorIndex = adRifAdDate.lastIndexOf("-");
		houseTmp.setAdRif(adRifAdDate.substring(0, adRifSeparatorIndex-1).trim());
		houseTmp.setAdDate(adRifAdDate.substring(adRifSeparatorIndex+1, adRifAdDate.length()).trim());


		String imageDataJson = adHtmlPage.getElementById("js-hydration").toString()
				.replace("<script type=\"application/json\" id=\"js-hydration\">", "")
				.replace("</script>", "");

		DocumentContext documentContext = JsonPath.parse(imageDataJson);
		List<String> plans = documentContext.read("$.multimedia.planimetrie.list.[*].srcSet.large");

		List<HouseTmpImage> houseImages = new ArrayList<>();
		for (String plan : plans) {
			HouseTmpImage houseTmpImage = new HouseTmpImage();

			houseTmpImage.setHouseTmp(houseTmp);
			houseTmpImage.setUrl(plan.replace("\\", ""));
			houseTmpImage.setHouseImageType(HouseImageType.PLANIMETRY);
			houseImages.add(houseTmpImage);
		}

		List<String> images = documentContext.read("$.multimedia.immagini.list.[*].srcSet.large");
		for (String image : images) {
			HouseTmpImage houseImage = new HouseTmpImage();

			houseImage.setHouseTmp(houseTmp);
			houseImage.setUrl(image.replace("\\", ""));
			houseImage.setHouseImageType(HouseImageType.HOUSE);
			houseImages.add(houseImage);
		}
		houseTmp.setHouseTmpImages(houseImages);

		return houseTmp;
	}

//	private Integer getLastPageNumber(Document adsListHtmlPage) {
//		Elements ul = adsListHtmlPage.select("ul.pagination__number");
//		return Integer.valueOf(ul.select("li").last().text());
//	}

//	private String getAdLink(Element adsId) {
//		Element href = adsId.select("a[href]").first();
//		return href.attr("href");
//	}
//
//	private String getAdId(String adLink) {
//		String[] adUrl = adLink.split("/");
//		int adUrlLength = adUrl.length;
//		return adUrl[adUrlLength-1];
//	}

	/*
	private String getAdId(Element adsId) {
		Element href = adsId.select("a[href]").first();
		String[] adUrl = href.attr("href").split("/");
		int adUrlLength = adUrl.length;
		return adUrl[adUrlLength-1];
	}
	 */
}
