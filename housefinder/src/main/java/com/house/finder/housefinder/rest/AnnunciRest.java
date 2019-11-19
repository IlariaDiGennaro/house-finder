package com.house.finder.housefinder.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.house.finder.housefinder.service.AnalyzeService;
import com.house.finder.housefinder.service.CasaService;

@Controller
public class AnnunciRest {

	@Autowired
	CasaService casaService;
	
	@Autowired
	AnalyzeService analyzeService;

	
	@RequestMapping(value = "/case")
	public ResponseEntity<Object> getAll() { 
		return new ResponseEntity<>(casaService.getAllCase(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analyze")
	public ResponseEntity<Object> analyze() throws IOException { 
		//TODO GESTIONE ECCEZIONE
		analyzeService.analyze();
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
