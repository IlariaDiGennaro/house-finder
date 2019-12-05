package com.house.finder.housefinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.house.finder.housefinder.bean.SelectedHouse;
import com.house.finder.housefinder.service.SelectedHouseService;

@Controller
public class FEController {

	@Autowired
	private SelectedHouseService selectedHouseService;
	
	
	/**
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	public String viewHomePage(Model model) {
	    List<SelectedHouse> selectedHouseList = selectedHouseService.getAllCase();
	    model.addAttribute("selectedHouseList", selectedHouseList);
	    return "index";
	}
}
