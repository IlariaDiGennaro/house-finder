package com.house.finder.housefinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.ZCasa;
import com.house.finder.housefinder.dao.CasaRepository;
import com.house.finder.housefinder.dao.ZCasaRepository;
import com.house.finder.housefinder.rest.data.ScartaCasa;

@Service
public class CasaServiceImpl implements CasaService {

	@Autowired
	public CasaRepository casaRepository;
	@Autowired
	public ZCasaRepository zCasaRepository;
	
	@Override
	public List<Casa> getAllCase() {
		return casaRepository.findAllByOrderByNewAnnuncioDesc();
	}
	
	@Override
	public void scartaCase(List<ScartaCasa> scartaCasaList) {
		
		for (ScartaCasa scartaCasa : scartaCasaList) {
			Casa casaFound = casaRepository.findByIdAnnuncio(scartaCasa.getIdAnnuncio());
			
			if(casaFound != null) {
				ZCasa casaToDelete = new ZCasa();
 				casaToDelete.setScartato(true);
 				casaToDelete.setMotivazione(scartaCasa.getMotivazione());
 				casaToDelete.setIdAnnuncio(casaFound.getIdAnnuncio());
 				casaToDelete.setTitolo(casaFound.getTitolo());
 				casaToDelete.setLink(casaFound.getLink());
 				casaToDelete.setDescrizione(casaFound.getDescrizione());
 				casaToDelete.setPrezzo(casaFound.getPrezzo());
 				casaToDelete.setNumLocali(casaFound.getNumLocali());
 				casaToDelete.setMetriQuadri(casaFound.getMetriQuadri());
 				casaToDelete.setNumBagni(casaFound.getNumBagni());
 				casaToDelete.setPiano(casaFound.getPiano());
 				casaToDelete.setGarantito(casaFound.isGarantito());
 				casaToDelete.setAgenzia(casaFound.getAgenzia());
 				casaToDelete.setLastAnalyze(casaFound.getLastAnalyze());
 				
 				zCasaRepository.saveAndFlush(casaToDelete);
 				casaRepository.delete(casaFound);
			}
		}
	}

}
