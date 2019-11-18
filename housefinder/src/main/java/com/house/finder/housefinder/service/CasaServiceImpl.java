package com.house.finder.housefinder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.dao.CasaDao;

@Service
public class CasaServiceImpl implements CasaService {

	@Override
	public List<Casa> getAllCase() {
		CasaDao casaDao = new CasaDao();
		return casaDao.getAll();
	}

}
