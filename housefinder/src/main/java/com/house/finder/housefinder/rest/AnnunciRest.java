package com.house.finder.housefinder.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.house.finder.housefinder.rest.data.ScartaCasaRequest;
import com.house.finder.housefinder.service.AnalyzeService;
import com.house.finder.housefinder.service.CasaService;

@Controller
public class AnnunciRest {

	@Autowired
	CasaService casaService;
	
	@Autowired
	AnalyzeService analyzeService;

	
	@GetMapping(value = "/case")
	public ResponseEntity<Object> getAll() { 
		return new ResponseEntity<>(casaService.getAllCase(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/analyze")
	public ResponseEntity<Object> analyze() throws IOException { 
		//TODO GESTIONE ECCEZIONE
		analyzeService.analyze();
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/scarta")
	public ResponseEntity<Object> scartaCase(@RequestBody ScartaCasaRequest scartaCasaRequest) {
		casaService.scartaCase(scartaCasaRequest.getScarta());
	    return new ResponseEntity<>(null, HttpStatus.OK);
	  }
}
