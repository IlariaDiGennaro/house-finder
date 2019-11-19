package com.house.finder.housefinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.dao.CasaRepository;

@Service
public class CasaServiceImpl implements CasaService {

	@Autowired
	public CasaRepository casaRepository;
	
	@Override
	public List<Casa> getAllCase() {
		return casaRepository.findAll();
	}

}
