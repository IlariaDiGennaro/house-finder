package com.house.finder.housefinder.site.bean;

public class ImmobiliareIt {

	// COMPLETE URL
	//https://www.immobiliare.it/vendita-case/roma/?criterio=rilevanza&prezzoMassimo=400000&tipoProprieta=1&idMZona[]=10151&idMZona[]=10152&noAste=1&localiMinimo=3&tipoProprieta=1&fasciaPiano[]=20&pag=
	
	private static String basicImmobiliareItUrl = "https://www.immobiliare.it/vendita-case/roma/?criterio=rilevanza";
	private static String priceParam = "&prezzoMassimo=400000";
	private static String propertyTypeParam = "&tipoProprieta=1";
	private static String zoneParam = "&idMZona[]=10151&idMZona[]=10152";
	private static String noAste = "&noAste=1";
	private static String localiMin = "&localiMinimo=3";
	private static String tipoProp = "&tipoProprieta=1";
	private static String fasciaPiano = "&fasciaPiano[]=20";
	private static String genericPageParam = "&pag=";
	
	public static String getImmobilareItUrl(){
		return basicImmobiliareItUrl
				.concat(priceParam)
				.concat(propertyTypeParam)
				.concat(zoneParam)
				.concat(noAste)
				.concat(localiMin)
				.concat(tipoProp)
				.concat(fasciaPiano)
				.concat(genericPageParam);
	}
	
	private static String basicDetailsImmobiliareItUrl = "https://www.immobiliare.it/annunci/";
	
	public static String getDetailsImmobilareItUrl(String idAnnuncio){
		return basicDetailsImmobiliareItUrl
				.concat(idAnnuncio);
	}
			
}
