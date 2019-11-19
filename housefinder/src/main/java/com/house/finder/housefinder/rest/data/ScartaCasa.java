package com.house.finder.housefinder.rest.data;

import java.io.Serializable;

public class ScartaCasa implements Serializable{

	private static final long serialVersionUID = 5288176174788230438L;

	private String idAnnuncio;
	private String motivazione;
	
	public String getIdAnnuncio() {
		return idAnnuncio;
	}
	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}
	public String getMotivazione() {
		return motivazione;
	}
	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScartaCasa [idAnnunncio=");
		builder.append(idAnnuncio);
		builder.append(", motivazione=");
		builder.append(motivazione);
		builder.append("]");
		return builder.toString();
	}
	
}
