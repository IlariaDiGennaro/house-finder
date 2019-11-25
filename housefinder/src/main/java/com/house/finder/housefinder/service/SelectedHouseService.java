package com.house.finder.housefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.SelectedHouse;
import com.house.finder.housefinder.bean.ZCasa;
import com.house.finder.housefinder.dao.CasaRepository;
import com.house.finder.housefinder.dao.SelectedHouseRepository;
import com.house.finder.housefinder.dao.ZCasaRepository;

@Service
public class SelectedHouseService {

	@Autowired
	public CasaRepository casaRepository;
	@Autowired
	public ZCasaRepository zCasaRepository;
	@Autowired
	public SelectedHouseRepository selectedHouseRepository;

	public Object getAllCase() {
		return selectedHouseRepository.findAll();
	}

	public void createSelectedHouse(String idAnnuncio) {
		
		Casa casaFound = casaRepository.findByIdAnnuncio(idAnnuncio);
		ZCasa zCasaFound = zCasaRepository.findByIdAnnuncio(idAnnuncio);
		
		if(casaFound != null && zCasaFound == null) {
			SelectedHouse selectedHouse = new SelectedHouse();
				selectedHouse.setIdAnnuncio(casaFound.getIdAnnuncio());
				selectedHouse.setTitolo(casaFound.getTitolo());
				selectedHouse.setLink(casaFound.getLink());
				selectedHouse.setDescrizione(casaFound.getDescrizione());
				selectedHouse.setPrezzo(casaFound.getPrezzo());
				selectedHouse.setNumLocali(casaFound.getNumLocali());
				selectedHouse.setMetriQuadri(casaFound.getMetriQuadri());
				selectedHouse.setNumBagni(casaFound.getNumBagni());
				selectedHouse.setPiano(casaFound.getPiano());
				selectedHouse.setGarantito(casaFound.isGarantito());
				selectedHouse.setAgenzia(casaFound.getAgenzia());
				
				selectedHouseRepository.saveAndFlush(selectedHouse);
				casaRepository.delete(casaFound);
		}
		
	}
	
	
}
