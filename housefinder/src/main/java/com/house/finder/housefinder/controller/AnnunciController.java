package com.house.finder.housefinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.service.AnalyzeService;
import com.house.finder.housefinder.service.CasaService;

@Controller
public class AnnunciController {

	@Autowired
	private CasaService casaService;
	@Autowired
	private AnalyzeService analyzeService;
	
	
	@RequestMapping("/")
	public String viewHomePage(Model model) {
	    List<Casa> listCasa = casaService.getAllCase();
	    model.addAttribute("listCasa", listCasa);
	    return "index3";
	}
}
