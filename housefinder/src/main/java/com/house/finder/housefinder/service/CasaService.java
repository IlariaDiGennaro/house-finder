package com.house.finder.housefinder.service;

import java.util.List;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.rest.data.ScartaCasa;

public interface CasaService {

	//CRUD
	public abstract List<Casa> getAllCase();

	void scartaCase(List<ScartaCasa> scartaCasaList);

}
