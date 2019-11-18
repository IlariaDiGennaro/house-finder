package com.house.finder.housefinder.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.house.finder.housefinder.service.CasaService;

@Controller
public class AnnunciRest {

	@Autowired
	CasaService casaService;

	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello Spring Boot";
	}

	//	@RequestMapping("/")
	//	@ResponseBody
	//	public List<Casa> getAll() {
	//		return "Hello Spring Boot";
	//	}

	@RequestMapping(value = "/case")
	public ResponseEntity<Object> getAll() { 
		return new ResponseEntity<>( casaService.getAllCase(), HttpStatus.OK);
	}

}
