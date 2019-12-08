package com.house.finder.housefinder.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.house.finder.housefinder.repository.HouseRepository;
import com.house.finder.housefinder.site.bean.ImmobiliareIt;

public class AdsAnalyzer {
	
	@Autowired
	public HouseRepository houseRepository;

	public void adsAnalyzer() throws IOException {
		
		List<String> houseIdsRejected = houseRepository.findAdIdsByHouseStatus("REJECTED");
		
		Integer maxPage = 9999;
		String immobiliareItUrl = ImmobiliareIt.getImmobilareItUrl();
		
		for (int pageNum = 1; pageNum <= maxPage; pageNum++) {
			
			Document adsListHtmlPage = null;
			if(pageNum == 1) {
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl).get();
				maxPage = getLastPageNumber(adsListHtmlPage);
			} else {
				String page = String.valueOf(pageNum);
				adsListHtmlPage = Jsoup.connect(immobiliareItUrl.concat("&pag=").concat(page)).get();
			}
			
			Elements adsIdLIst = adsListHtmlPage.select("p.titolo text-primary");
			for (Element adsId : adsIdLIst) {
				String adId = getAdId(adsId);
				
				if(!houseIdsRejected.contains(adId)) {
					
				}
			}
			
		}
	}
	
	private Integer getLastPageNumber(Document adsListHtmlPage) {
		Elements ul = adsListHtmlPage.select("ul.pagination__number");
		return Integer.valueOf(ul.select("li").last().text());
	}
	
	private String getAdId(Element adsId) {
		Element href = adsId.select("a[href]").first();
		String[] adUrl = href.attr("href").split("/");
		int adUrlLength = adUrl.length;
		return adUrl[adUrlLength-1];
	}
}
