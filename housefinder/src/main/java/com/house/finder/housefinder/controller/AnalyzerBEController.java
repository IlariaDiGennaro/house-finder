package com.house.finder.housefinder.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.house.finder.housefinder.analyzer.IdealistaAdsAnalyzer;
import com.house.finder.housefinder.analyzer.ImmobiliareItAdsAnalyzer;

@Controller
public class AnalyzerBEController {

	@Autowired
	private ImmobiliareItAdsAnalyzer adsAnalyzer;
	
	@Autowired
	private IdealistaAdsAnalyzer idealistaAdsAnalyzer;
	
	@GetMapping(value = "/v2/analyze")
	public ResponseEntity<Void> analyze() throws IOException, InterruptedException {
		idealistaAdsAnalyzer.idealistaAdsAnalyzer();
//		adsAnalyzer.adsAnalyzer();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
