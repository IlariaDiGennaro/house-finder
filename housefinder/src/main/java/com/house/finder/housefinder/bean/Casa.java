package com.house.finder.housefinder.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Deprecated
@Entity
@Table(name = "casa")
public class Casa implements Serializable{

	private static final long serialVersionUID = 7769189875610703602L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "id_annuncio", nullable = false)
	private String idAnnuncio;
	
	@Column(name = "titolo", nullable = false)
	private String titolo;
	
	@Column(name = "link", nullable = false)
	private String link;
	
	@Column(name = "descrizione", nullable = false)
	private String descrizione;
	
	@Column(name = "prezzo", nullable = false)
	private String prezzo;
	
	@Column(name = "num_locali", nullable = false)
	private String numLocali;
	
	@Column(name = "metri_quadri", nullable = false)
	private String metriQuadri;
	
	@Column(name = "num_bagni")
	private String numBagni;
	
	@Column(name = "piano")
	private String piano;
	
	@Column(name = "garantito", nullable = false)
	private boolean garantito;
	
	@Column(name = "agenzia")
	private String agenzia;
	
	@Column(name = "last_analyze")
	private Date lastAnalyze;
	
	@Column(name = "new_annuncio")
	private boolean newAnnuncio;
	
	@Column(name = "new_datetime")
	private Date newDatetime;
	
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
	public Date getLastAnalyze() {
		return lastAnalyze;
	}
	public void setLastAnalyze(Date lastAnalyze) {
		this.lastAnalyze = lastAnalyze;
	}
	public boolean isNewAnnuncio() {
		return newAnnuncio;
	}
	public void setNewAnnuncio(boolean newAnnuncio) {
		this.newAnnuncio = newAnnuncio;
	}
	public Date getNewDatetime() {
		return newDatetime;
	}
	public void setNewDatetime(Date newDatetime) {
		this.newDatetime = newDatetime;
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
		builder.append(", lastAnalyze=");
		builder.append(lastAnalyze);
		builder.append(", newAnnuncio=");
		builder.append(newAnnuncio);
		builder.append(", newDatetime=");
		builder.append(newDatetime);
		builder.append("]");
		return builder.toString();
	}
	
}
