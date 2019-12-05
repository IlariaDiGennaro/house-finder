package com.house.finder.housefinder.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.SelectedHouse;
import com.house.finder.housefinder.bean.ZCasa;
import com.house.finder.housefinder.bean.ZSelectedHouse;
import com.house.finder.housefinder.dao.CasaRepository;
import com.house.finder.housefinder.dao.SelectedHouseRepository;
import com.house.finder.housefinder.dao.ZCasaRepository;
import com.house.finder.housefinder.dao.ZSelectedHouseRepository;
import com.house.finder.housefinder.site.bean.ImmobiliareIt;

@Service
public class SelectedHouseService {

	@Autowired
	public CasaRepository casaRepository;
	@Autowired
	public ZCasaRepository zCasaRepository;
	@Autowired
	public SelectedHouseRepository selectedHouseRepository;
	@Autowired
	public ZSelectedHouseRepository zSelectedHouseRepository;

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
	
	public void analyzeSelectedHouse() throws IOException {
		
		List<SelectedHouse> selectedHouseList = selectedHouseRepository.findAll();
		
		for (SelectedHouse selectedHouse : selectedHouseList) {
			
			try {
				
				Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(selectedHouse.getIdAnnuncio())).get();
				
				System.out.println(selectedHouse.getIdAnnuncio() + " is still available");
			
			} catch (HttpStatusException e) {
				
				System.out.println(selectedHouse.getIdAnnuncio() + " is no more available");
				
				ZSelectedHouse zSelectedHouse = new ZSelectedHouse();
				zSelectedHouse.setIdAnnuncio(selectedHouse.getIdAnnuncio());
				zSelectedHouse.setTitolo(selectedHouse.getTitolo());
				zSelectedHouse.setLink(selectedHouse.getLink());
				zSelectedHouse.setDescrizione(selectedHouse.getDescrizione());
				zSelectedHouse.setPrezzo(selectedHouse.getPrezzo());
				zSelectedHouse.setNumLocali(selectedHouse.getNumLocali());
				zSelectedHouse.setMetriQuadri(selectedHouse.getMetriQuadri());
				zSelectedHouse.setNumBagni(selectedHouse.getNumBagni());
				zSelectedHouse.setPiano(selectedHouse.getPiano());
				zSelectedHouse.setGarantito(selectedHouse.isGarantito());
				zSelectedHouse.setAgenzia(selectedHouse.getAgenzia());
				
				zSelectedHouseRepository.saveAndFlush(zSelectedHouse);
				selectedHouseRepository.delete(selectedHouse);
			}
		}
		
	}

	public List<SelectedHouse> getAllCase() {
		return selectedHouseRepository.findAll();
	}
}
