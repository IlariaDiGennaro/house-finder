package com.house.finder.housefinder.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.house.finder.housefinder.service.AdsAnalyzer;

@Controller
public class AnalyzerBEController {

	@Autowired
	private AdsAnalyzer adsAnalyzer;
	
	@GetMapping(value = "/v2/analyze")
	public ResponseEntity<Void> analyze() throws IOException { 
		adsAnalyzer.adsAnalyzer();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
