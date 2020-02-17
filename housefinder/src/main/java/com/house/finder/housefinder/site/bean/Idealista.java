package com.house.finder.housefinder.site.bean;

public class Idealista {

	// COMPLETE URL
	//https://www.idealista.it/vendita-case/roma/appio-latino/colli-albani-furio-camillo/con-prezzo_400000,dimensione_60,appartamenti,trilocali-3,quadrilocali-4,5-locali-o-piu
	//https://www.idealista.it/vendita-case/roma/appio-latino/caffarella/con-prezzo_400000,appartamenti,trilocali-3,quadrilocali-4,5-locali-o-piu/
	
	//PAGINAZIONE
	//lista-2.htm
	
	private static final String PAGE_URL = "lista-<?>.htm"; 
	
	private static String basicIdealistaUrl_zone_colliAlbani = "https://www.idealista.it/vendita-case/roma/appio-latino/colli-albani-furio-camillo/";
	private static String basicIdealistaUrl_zone_caffarella = "https://www.idealista.it/vendita-case/roma/appio-latino/caffarella/";
	private static String priceParam = "con-prezzo_400000,";
	private static String dimMin = "dimensione_60,";
	private static String tipoProp = "appartamenti,";
	private static String localiMin = "trilocali-3,quadrilocali-4,5-locali-o-piu";
	
	public static String getIdealistaUrl_zone_colliAlbani(){
		return basicIdealistaUrl_zone_colliAlbani
				.concat(priceParam)
				.concat(dimMin)
				.concat(tipoProp)
				.concat(localiMin)
		;
	}
	
	public static String getIdealistaUrl_zone_caffarella(){
		return basicIdealistaUrl_zone_caffarella
				.concat(priceParam)
				.concat(dimMin)
				.concat(localiMin)
				.concat(tipoProp)
		;
	}
	
	public static String getPageString(int page) {
		return PAGE_URL.replace("<?>", String.valueOf(page));
	}
	
	//ADS DETAILS
	
	private static String basicDetailsIdealistaUrl = "https://www.idealista.it/immobile/";
	
	public static String getDetailsIdealistaUrl(String idAnnuncio){
		return basicDetailsIdealistaUrl
				.concat(idAnnuncio)
				.concat("/");
	}
}
