package com.house.finder.housefinder.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.house.finder.housefinder.analyzer.util.IdealistaAdsAnalyzerUtils;
import com.house.finder.housefinder.analyzer.util.ImmobiliareItAdsAnalyzerUtils;
import com.house.finder.housefinder.bean.HouseTmp;

@Service
public class IdealistaAdsAnalyzer {

	
	/*
	 * Questa lista contiene gli annunci che vengono letti dal sito
	 */
	public static List<HouseTmp> houseFromSite_toBeEvaluated;
	
	@Transactional
	public void idealistaAdsAnalyzer() throws IOException, InterruptedException {
		
		houseFromSite_toBeEvaluated = new ArrayList<>();
		houseFromSite_toBeEvaluated = IdealistaAdsAnalyzerUtils.buildHouseTmpList();
		System.out.println("Analisi IDEALISTA finita");
	}
}
