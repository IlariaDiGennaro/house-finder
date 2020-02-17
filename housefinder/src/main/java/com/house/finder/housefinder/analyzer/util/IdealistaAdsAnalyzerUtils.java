package com.house.finder.housefinder.analyzer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.house.finder.housefinder.bean.HouseTmp;
import com.house.finder.housefinder.site.bean.Idealista;

public class IdealistaAdsAnalyzerUtils {

	private static final int SEC_15 = 15000;


	public static List<HouseTmp> buildHouseTmpList() throws IOException, InterruptedException {
		
		List<HouseTmp> houseFromSite_toBeEvaluated = new ArrayList<HouseTmp>();
		
		String idealistaUrl = Idealista.getIdealistaUrl_zone_colliAlbani();
		houseFromSite_toBeEvaluated.addAll(generateHouseTmpList(idealistaUrl));
		
		idealistaUrl = Idealista.getIdealistaUrl_zone_caffarella();
		houseFromSite_toBeEvaluated.addAll(generateHouseTmpList(idealistaUrl));
		
		return houseFromSite_toBeEvaluated;
	}
	
	private static List<HouseTmp> generateHouseTmpList(String idealistaUrl) throws IOException, InterruptedException{
		List<HouseTmp> houseFromSite_toBeEvaluated = new ArrayList<HouseTmp>();
		Integer maxPage = 9999;
		
		for (int pageNum = 1; pageNum <= maxPage; pageNum++) {
			
			Document adsListHtmlPage = null;
			if(pageNum == 1) {
				adsListHtmlPage = Jsoup.connect(
											idealistaUrl
										).headers(createHeadersMap())
									.get();
				
				maxPage = getLastPageNumber(adsListHtmlPage);
				System.out.println("Numero di pagine: " + maxPage);
				Thread.sleep(SEC_15);
			} else {
				
				System.out.println(idealistaUrl.concat(Idealista.getPageString(pageNum)));
				adsListHtmlPage = Jsoup.connect(
											idealistaUrl
											.concat(Idealista.getPageString(pageNum))
										).headers(createHeadersMap())
									.get();
				Thread.sleep(SEC_15);
			}
			System.out.print("Analisi pagina: " + pageNum);
			
			
			Elements adsIdList = adsListHtmlPage.select("div.item-info-container");
			for (Element adsIdElem : adsIdList) {
				HouseTmp houseTmp = new HouseTmp();
				
				Element href = adsIdElem.select("a.item-link").first();
				String adLink = href.attr("href");
				houseTmp.setLink(adLink);
				
				String[] adUrl = adLink.split("/");
				int adUrlLength = adUrl.length;
				String adId = adUrl[adUrlLength-1];
				houseTmp.setAdId(adId);
				
				System.out.println(Idealista.getDetailsIdealistaUrl(adId));
				Document adHtmlPage = Jsoup.connect(Idealista.getDetailsIdealistaUrl(adId)).headers(createHeadersMap()).get();
				String title = adHtmlPage.select("span.main-info__title-main").text();
				houseTmp.setTitle(title);
				
				Thread.sleep(SEC_15);
				
				String description = adHtmlPage.select("div.adCommentsLanguage.expandable").text();
				houseTmp.setDescription(description);
				
				String price = adHtmlPage.select("span.info-data-price").text();
				houseTmp.setPrice(price);
				
				
				Elements infoFeatures = adHtmlPage.select("div.info-features");
				Elements spans = infoFeatures.select("span");

				for (int j = 0; j < spans.size(); j++) {
					String homeDataToDefine = spans.get(j).text();
					if(homeDataToDefine.contains("local")) {
						houseTmp.setRooms(homeDataToDefine);
						continue;
					}else if(homeDataToDefine.contains("m2")) {
						houseTmp.setMq(homeDataToDefine);
						continue;
					}else if(homeDataToDefine.contains("pian")) {
						String beautifyFloor = homeDataToDefine.replaceAll(" senza ascensore", "").replaceAll(" con ascensore", "");
						houseTmp.setFloor(beautifyFloor);
						continue;
					}
				}
				
				String agency = adHtmlPage.select("a.about-advertiser-name").text();
				houseTmp.setAgency(agency);
				String phoneNumbersAgency = adHtmlPage.select("p.txt-bold._browserPhone.icon-phone").text();
				houseTmp.setPhoneNumbersAgency(phoneNumbersAgency);
				
				String adRif = adHtmlPage.select("p.txt-ref").text();
				houseTmp.setAdRif(adRif);
				
				Elements adDateSector = adHtmlPage.select("div.ide-box-detail.overlay-box.mb-jumbo");
				String adDateRough = adDateSector.first().getElementsByTag("p").text();
				houseTmp.setAdDate(adDateRough.split("il ")[1]);

				
				System.out.println(houseTmp.toString());
				System.out.println("########################################");
			}
		}
		return houseFromSite_toBeEvaluated;
	}
	
	
	private static Integer getLastPageNumber(Document adsListHtmlPage) {
		Elements ul = adsListHtmlPage.select("div.pagination");
		int numElem = ul.select("li").size();
		return Integer.valueOf(ul.select("li").get(numElem-2).text());
	}

	
	private static Map<String,String> createHeadersMap() {
		Map<String,String> headers = new HashMap<String, String>();
		
		headers.put("Content-Type", "application/json");
		headers.put("accept", "text/html");
		headers.put("accept-encoding", "gzip");
		headers.put("accept-language", "it-IT");
//		headers.put("cookie", "userUUID=96055ca4-8506-4350-9814-dd22647f1d55; cookieSearch-1=\"/vendita-case/roma/appio-latino/colli-albani-furio-camillo/con-prezzo_400000,dimensione_60,appartamenti,trilocali-3,quadrilocali-4,5-locali-o-piu/:1581876832778\"; contactc4654093-a195-4ed8-be89-0b30e77a1ebd=\"{'email':null,'phone':null,'phonePrefix':null,'friendEmails':null,'name':null,'message':null,'message2Friends':null,'maxNumberContactsAllow':10,'defaultMessage':true}\"; SESSION=c4654093-a195-4ed8-be89-0b30e77a1ebd; WID=286b865b6f78b149|XkmFx|XkmFx; _pxhd=e122349371a0861fe5d55a818bc2999a75b37dd8447fac8990f9260a65b49981:167061c1-50e8-11ea-9805-6da5ea601ff9; xtvrn=$402916$; xtan402916=2-anonymous; xtant402916=1; cto_lwid=6c98950a-8df7-4989-bcec-449287508358; ABTasty=uid=qgw2cc96s28dw1an&fst=1581876822488&pst=-1&cst=1581876822488&ns=1&pvt=1&pvis=1&th=; cookieDirectiveClosed=true; atuserid=%7B%22name%22%3A%22atuserid%22%2C%22val%22%3A%226ff1cda6-dba3-4a29-a238-499bf317a4de%22%2C%22options%22%3A%7B%22end%22%3A%222021-03-19T18%3A13%3A43.312Z%22%2C%22path%22%3A%22%2F%22%7D%7D; atidvisitor=%7B%22name%22%3A%22atidvisitor%22%2C%22val%22%3A%7B%22vrn%22%3A%22-582070-%22%7D%2C%22options%22%3A%7B%22path%22%3A%22%2F%22%2C%22session%22%3A15724800%2C%22end%22%3A15724800%7D%7D; _hjid=05801c1b-f725-4c1f-9d3e-94e2713829b1; _pxvid=167061c1-50e8-11ea-9805-6da5ea601ff9; utag_main=v_id:01704f34c1a80001af59c50ad9cf0307300a906b0086e$_sn:1$_ss:0$_st:1581878831802$ses_id:1581876822442%3Bexp-session$_pn:2%3Bexp-session$dc_visit:1$dc_event:2%3Bexp-session$dc_region:eu-central-1%3Bexp-session");
		
		headers.put("cookie", "atuserid=%7B%22name%22%3A%22atuserid%22%2C%22val%22%3A%22d0d13716-8366-4158-a99f-0caaa7622589%22%2C%22options%22%3A%7B%22end%22%3A%222021-03-20T06%3A33%3A54.144Z%22%2C%22path%22%3A%22%2F%22%7D%7D; userUUID=8f839763-ebe9-4a80-800a-7790a33ae3d7; SESSION=b9bceefc-50d5-477d-bd05-9982396d7828; xtvrn=$402916$; xtant402916=1; cto_lwid=6c98950a-8df7-4989-bcec-449287508358; cookieDirectiveClosed=true; _hjid=05801c1b-f725-4c1f-9d3e-94e2713829b1; _pxvid=80601141-514f-11ea-aa63-0242ac120005; uc=UZpwxKGoMho/TH8/x1meXMg7gOBP6xYY1VVof/Ozg4nQep7eKUCI0g9tUDvTQoOxavpjY6KsLm8fa/EvJtIO9Za1WOlYcMqWJulCWVpAcyyLRKFfrn5cw6dXpFsdp1bm; cc=eyJhbGciOiJIUzI1NiJ9.eyJjdCI6NDkwNjc3OSwiZXhwIjoxNTgyMDA3Njc1fQ.lcBsBIQ99C11_-J-MXMloFR25ydGE3Wx1VojtNKamKs; nl=YtMpaGHeYxGlx6rvhCzF8Xe3vhEsUiJrJJfsEKQBGEscPX49p9uPOF26chNTQbZEVJKypkM4RiEfu/L2IuT6hIeCHKpwW42jTN1Q3xIBu/sie9bojCPdPqZFWwoDWWCv; WID=41909237c2ff1341|XkozY|XkozQ; ABTasty=uid=px7vw0cdqxp6z82q&fst=1581921234753&pst=-1&cst=1581921234753&ns=0&pvt=2&pvis=2&th=; xtan402916=1-81652922; IQ_userIdCookie=IT0081652922; atidvisitor=%7B%22name%22%3A%22atidvisitor%22%2C%22val%22%3A%7B%22vrn%22%3A%22-582070-%22%2C%22at%22%3A%22IT0081652922%22%2C%22ac%22%3A%221%22%7D%2C%22options%22%3A%7B%22path%22%3A%22%2F%22%2C%22session%22%3A15724800%2C%22end%22%3A15724800%7D%7D; utag_main=_sn:1$_ss:0$_st:1581923064479$ses_id:1581921234023%3Bexp-session$_pn:3%3Bexp-session$v_id:017051da6ec50050f2052c14049003073001e06b0086e$dc_visit:1$dc_event:3%3Bexp-session$dc_region:eu-central-1%3Bexp-session; _px2=eyJ1IjoiODlmNWI5MzAtNTE0Zi0xMWVhLThiMjUtNGZjYzlmZTA4NTU0IiwidiI6IjgwNjAxMTQxLTUxNGYtMTFlYS1hYTYzLTAyNDJhYzEyMDAwNSIsInQiOjE1ODE5MjE1Nzc5NzUsImgiOiJkZDViNGZlNTk5ZTcxZjhjMzU5NjE1MjNmN2JmYmZiN2JlYjdmZTZhODJjZWQ5MDAxZmRlNzBjNzAyYjNlNzAzIn0=; ABTastySession=mrasn=&referrer=&lp=https://www.idealista.it/vendita-case/roma/appio-latino/colli-albani-furio-camillo/con-prezzo_400000%2Cdimensione_60%2Cappartamenti%2Ctrilocali-3%2Cquadrilocali-4%2C5-locali-o-piu&sen=15");

		headers.put("sec-fetch-mode", "navigate");
		headers.put("sec-fetch-site", "none");
		headers.put("sec-fetch-user", "?1");
		headers.put("upgrade-insecure-requests", "1");
		headers.put("user-agent", "Chrome/79.0.3945.130");
		
		return headers;
	}
}
