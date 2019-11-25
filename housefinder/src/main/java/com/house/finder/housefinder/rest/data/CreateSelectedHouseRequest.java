package com.house.finder.housefinder.rest.data;

import java.io.Serializable;

public class CreateSelectedHouseRequest  implements Serializable{

	private static final long serialVersionUID = 1548537005897108639L;
	
	private String idAnnuncio;

	public String getIdAnnuncio() {
		return idAnnuncio;
	}

	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}
	
}
