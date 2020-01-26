package com.house.finder.housefinder.analyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.house.finder.housefinder.bean.House;
import com.house.finder.housefinder.bean.HouseHistory;
import com.house.finder.housefinder.bean.HouseImageType;
import com.house.finder.housefinder.bean.HouseStatus;
import com.house.finder.housefinder.bean.HouseTmp;
import com.house.finder.housefinder.bean.HouseTmpImage;
import com.house.finder.housefinder.repository.HouseHistoryRepository;
import com.house.finder.housefinder.repository.HouseRepository;
import com.house.finder.housefinder.site.bean.ImmobiliareIt;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class AdsAnalyzerUtils {

	public static List<HouseTmp> buildHouseTmpList() throws IOException {
		
		List<HouseTmp> houseFromSite_toBeEvaluated = new ArrayList<HouseTmp>();
		
		Integer maxPage = 9999;
		String immobiliareItUrl = ImmobiliareIt.getImmobilareItUrl();
		
		for (int pageNum = 1; pageNum <= maxPage; pageNum++) {

			Document adsListHtmlPage = null;
			if(pageNum == 1) {
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl).get();
				maxPage = getLastPageNumber(adsListHtmlPage);
				System.out.println("Numero di pagine: " + maxPage);
			} else {
				String page = String.valueOf(pageNum);
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl.concat("&pag=").concat(page)).get();
			}
			System.out.println("Analisi pagina: " + pageNum);

			Elements adsIdLIst = adsListHtmlPage.select("p.titolo.text-primary");
			for (Element adsIdElem : adsIdLIst) {
				
				HouseTmp houseTmp = new HouseTmp();
				
				Element href = adsIdElem.select("a[href]").first();
				String adLink = href.attr("href");
				houseTmp.setLink(adLink);
				
				String[] adUrl = adLink.split("/");
				int adUrlLength = adUrl.length;
				String adId = adUrl[adUrlLength-1];
				houseTmp.setAdId(adId);
				
				if(adId.startsWith("p-")) {
					//nouva costruzione
					continue;
				}

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
				
				houseFromSite_toBeEvaluated.add(houseTmp);
			}
		}
		
		return houseFromSite_toBeEvaluated;
	}
	
	private static Integer getLastPageNumber(Document adsListHtmlPage) {
		Elements ul = adsListHtmlPage.select("ul.pagination__number");
		return Integer.valueOf(ul.select("li").last().text());
	}
	
	public static void checkAdAvailability(House house, HouseRepository houseRepository, HouseHistoryRepository houseHistoryRepository) {
		try {
			Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(house.getAdId())).get();
		} catch (HttpStatusException e) {
			System.out.println("This AD is no more available: "+house.getAdId()+" "+house.getTitle());
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
}
