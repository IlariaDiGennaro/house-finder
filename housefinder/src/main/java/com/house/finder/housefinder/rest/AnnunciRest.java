package com.house.finder.housefinder.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.house.finder.housefinder.rest.data.CreateSelectedHouseRequest;
import com.house.finder.housefinder.rest.data.ScartaCasaRequest;
import com.house.finder.housefinder.service.AnalyzeService;
import com.house.finder.housefinder.service.CasaService;
import com.house.finder.housefinder.service.SelectedHouseService;

@Controller
public class AnnunciRest {

	@Autowired
	public CasaService casaService;
	@Autowired
	public AnalyzeService analyzeService;
	@Autowired
	public SelectedHouseService selectedHouseService;

	
	@GetMapping(value = "/case")
	public ResponseEntity<Object> getAll() { 
		return new ResponseEntity<>(casaService.getAllCase(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/analyze")
	public ResponseEntity<Void> analyze() throws IOException { 
		//TODO GESTIONE ECCEZIONE
		analyzeService.analyze();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PostMapping("/scarta")
	public ResponseEntity<Void> scartaCase(@RequestBody ScartaCasaRequest scartaCasaRequest) {
		casaService.scartaCase(scartaCasaRequest.getScarta());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	//SELECTED HOUSE
	
	@GetMapping(value = "/selected-house")
	public ResponseEntity<Object> getAllPreferredHouse() { 
		return new ResponseEntity<>(selectedHouseService.getAllCase(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/selected-house")
	public ResponseEntity<Void> createSelectedHouse(@RequestBody CreateSelectedHouseRequest createSelectedHouseRequest) {
		selectedHouseService.createSelectedHouse(createSelectedHouseRequest.getIdAnnuncio());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
