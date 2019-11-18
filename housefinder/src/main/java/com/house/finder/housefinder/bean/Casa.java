package com.house.finder.housefinder.bean;

public class Casa {

	private String idAnnuncio;
	private String titolo;
	private String link;
	private String descrizione;
	private String prezzo;
	private String numLocali;
	private String metriQuadri;
	private String numBagni;
	private String piano;
	private boolean garantito;
	private String agenzia;
	
	public String getIdAnnuncio() {
		return idAnnuncio;
	}
	public void setIdAnnuncio(String idAnnuncio) {
		this.idAnnuncio = idAnnuncio;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}
	public String getNumLocali() {
		return numLocali;
	}
	public void setNumLocali(String numLocali) {
		this.numLocali = numLocali;
	}
	public String getMetriQuadri() {
		return metriQuadri;
	}
	public void setMetriQuadri(String metriQuadri) {
		this.metriQuadri = metriQuadri;
	}
	public String getNumBagni() {
		return numBagni;
	}
	public void setNumBagni(String numBagni) {
		this.numBagni = numBagni;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
	}
	public boolean isGarantito() {
		return garantito;
	}
	public void setGarantito(boolean garantito) {
		this.garantito = garantito;
	}
	public String getAgenzia() {
		return agenzia;
	}
	public void setAgenzia(String agenzia) {
		this.agenzia = agenzia;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Casa [idAnnuncio=");
		builder.append(idAnnuncio);
		builder.append(", titolo=");
		builder.append(titolo);
		builder.append(", link=");
		builder.append(link);
		builder.append(", descrizione=");
		builder.append(descrizione);
		builder.append(", prezzo=");
		builder.append(prezzo);
		builder.append(", numLocali=");
		builder.append(numLocali);
		builder.append(", metriQuadri=");
		builder.append(metriQuadri);
		builder.append(", numBagni=");
		builder.append(numBagni);
		builder.append(", piano=");
		builder.append(piano);
		builder.append(", garantito=");
		builder.append(garantito);
		builder.append(", agenzia=");
		builder.append(agenzia);
		builder.append("]");
		return builder.toString();
	}
	
}
