package com.house.finder.housefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.house.finder.housefinder.bean.HouseStatus;
import com.house.finder.housefinder.repository.HouseRepository;
import com.house.finder.housefinder.repository.HouseTmpRepository;
import com.house.finder.housefinder.rest.data.RejectHouseRequest;
import com.house.finder.housefinder.rest.data.SelectHouseRequest;
import com.house.finder.housefinder.service.HouseManager;

@Controller
public class HouseBEController {

	@Autowired
	public HouseManager houseManager;
	@Autowired
	public HouseRepository houseRepository;
	@Autowired
	public HouseTmpRepository houseTmpRepository;
	
	@PostMapping(value = "/v2/reject")
	public ResponseEntity<Void> reject(@RequestBody RejectHouseRequest rejectHouseRequest) { 
		houseManager.rejectHouse(rejectHouseRequest);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/v2/select")
	public ResponseEntity<Void> select(@RequestBody SelectHouseRequest selectHouseRequest) { 
		houseManager.selectHouse(selectHouseRequest);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/v2/house")
	public ResponseEntity<Object> getHouse(
			@RequestParam("status") HouseStatus status) { 
		return new ResponseEntity<>(houseRepository.findByHouseStatus(status), HttpStatus.OK);
	}
	
	@GetMapping(value = "/v2/house-tmp")
	public ResponseEntity<Object> getHouseTmp() {
//		GsonMessageBodyHandler.setSkipDataProtection(true);
		return new ResponseEntity<>(houseTmpRepository.findAll(), HttpStatus.OK);
//		HouseResponse response;
//        List<HouseTmp> houseTmpList = houseTmpRepository.findAll();
//        response = HouseResponse.fromHouseTmp(houseTmpList);
//        return new ResponseEntity<HouseResponse>(response);
	}
}
