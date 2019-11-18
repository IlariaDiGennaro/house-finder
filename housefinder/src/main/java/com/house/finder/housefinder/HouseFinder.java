package com.house.finder.housefinder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.util.CasaComparator;
import com.house.finder.housefinder.dao.CasaDao;

@SpringBootApplication
public class HouseFinder {

	public static List<Casa> annunciList = new ArrayList<>();
	public static List<Casa> nuoveCostruzioniList = new ArrayList<>();
	public static HashMap<Casa, List<Casa>> duplicatiMap = new HashMap<Casa, List<Casa>>();
	
	public static void main(String[] args) {
		SpringApplication.run(HouseFinder.class, args);
    }
	
	
	
	public static void main_2(String[] args) throws IOException {

		// COMPLETE URL
		//https://www.immobiliare.it/vendita-case/roma/?criterio=rilevanza&prezzoMassimo=400000&tipoProprieta=1&idMZona[]=10151&idMZona[]=10152&noAste=1&localiMinimo=3&tipoProprieta=1&fasciaPiano[]=20&pag=
		
		String basicImmobiliareItUrl = "https://www.immobiliare.it/vendita-case/roma/?criterio=rilevanza";
		String priceParam = "&prezzoMassimo=400000";
		String propertyTypeParam = "&tipoProprieta=1";
		String zoneParam = "&idMZona[]=10151&idMZona[]=10152";
		String noAste = "&noAste=1";
		String localiMin = "&localiMinimo=3";
		String tipoProp = "&tipoProprieta=1";
		String fasciaPiano = "&fasciaPiano[]=20";
		String genericPageParam = "&pag=";

		Integer maxPage = 999;

		String immobiliareItUrl = basicImmobiliareItUrl
										.concat(priceParam)
										.concat(propertyTypeParam)
										.concat(zoneParam)
										.concat(noAste)
										.concat(localiMin)
										.concat(tipoProp)
										.concat(fasciaPiano)
										.concat(genericPageParam);

		
		for (int i = 1; i <= maxPage; i++) {

			Document doc = Jsoup.connect(immobiliareItUrl.concat(String.valueOf(i))).get();

			if (i==1) {
				Elements ul = doc.select("ul.pagination__number");
				maxPage = Integer.valueOf(ul.select("li").last().text());
				System.out.println("PAGINE DA ANALIZZARE: "+maxPage);
			}

			Elements adlist = doc.select("div.listing-item_body");
			
			for (Element ad : adlist) {

				Casa casa = new Casa();

				Elements ps = ad.getElementsByTag("p");
				
				for (Element p : ps) {

					Element href = p.select("a[href]").first();
					
					if(href != null) {
						
						casa.setTitolo(href.text());
						casa.setLink(href.attr("href"));
						
						String[] adUrl = href.attr("href").split("/");
						int id = adUrl.length;
						casa.setIdAnnuncio(adUrl[id-1]);
						
					}else {
						casa.setDescrizione(p.text());
					}
				}

				Elements uls = ad.select("ul.list-piped");
				
				for (Element ul : uls) {
					
					Elements lis = ul.select("li.lif__item");

					for (int j = 0; j < lis.size(); j++) {

						// DEFINE HOUSE INFO
						String homeDatatoDefine = lis.get(j).text();

						if(homeDatatoDefine.contains("€")) {
							if(homeDatatoDefine.lastIndexOf("€")!=0) {
								//remove old price
								casa.setPrezzo(homeDatatoDefine.substring(0, homeDatatoDefine.lastIndexOf("€")-1));
							} else {
								casa.setPrezzo(homeDatatoDefine);
							}
							continue;
						}else if(homeDatatoDefine.contains("local")) {
							casa.setNumLocali(homeDatatoDefine);
							continue;
						}else if(homeDatatoDefine.contains("m2")) {
							String m2Beautify = homeDatatoDefine.replaceAll("superficie", "").trim();
							casa.setMetriQuadri(m2Beautify);
							continue;
						}else if(homeDatatoDefine.contains("bagn")) {
							casa.setNumBagni(homeDatatoDefine);
							continue;
						}else if(homeDatatoDefine.contains("pian")) {
							casa.setPiano(homeDatatoDefine);
							continue;
						}else if(homeDatatoDefine.contains("garantito")) {
							casa.setGarantito(true);
							continue;
						}else {
							System.out.println("UNDEFINED DATA: "+homeDatatoDefine+" PER ANNUNCIO "+casa.getLink());
						}
					}

					Elements agenzie = ad.select("img.logo-agency");
					for (Element agenzia : agenzie) {
						casa.setAgenzia(agenzia.attr("alt"));
					}
				}
				annunciList.add(casa);
			}
		}

		System.out.println("retrieve data from web ended.");
		System.out.println("annunci rozzi trovati: "+annunciList.size());

		analyzeAnnunnci();

		System.out.println("annunci normalizzati trovati: "+annunciList.size());

		createCsvFile();
		
		
		System.out.println("DB TEST");
		CasaDao casaDao = new CasaDao();
		casaDao.insertAll(annunciList);
		
		System.out.println("finito");
	}

	private static void analyzeAnnunnci() {
		
		List<Casa> annunciList2 = new ArrayList<>();
		
		for (Casa casa : annunciList) {
			annunciList2.add(casa);
		}

		System.out.println("analyze start");

		List<Casa> duplicatiList = new ArrayList<>();
		Set<Casa> duplicatiDaEscludereSet = new HashSet<>();
		
		for (Casa casa1 : annunciList2) {

			if(casa1.getIdAnnuncio().startsWith("p-")) {
				//rimozione case "nuove costruzioni"
				annunciList.remove(casa1);
				nuoveCostruzioniList.add(casa1);
				continue;
			}

			for (Casa casa2 : annunciList2) {

				if(!casa1.getIdAnnuncio().equals(casa2.getIdAnnuncio()) 
						&& !duplicatiDaEscludereSet.contains(casa2)
						&& CasaComparator.equals(casa1, casa2)) {
					duplicatiList.add(casa2);
				}
			}

			if(!duplicatiList.isEmpty()) {
				duplicatiMap.put(casa1, duplicatiList);
				duplicatiDaEscludereSet.add(casa1);
				duplicatiDaEscludereSet.addAll(duplicatiList);
				annunciList.removeAll(duplicatiList);
			}

			duplicatiList = new ArrayList<>();
		}

		//TODO remove in future
		for (Map.Entry<Casa, List<Casa>> entry : duplicatiMap.entrySet()) {
			System.out.println(
					entry.getKey().toString() + "::" +
					"num duplicati "+entry.getValue().size() + "::" +
					entry.getValue().toString());
		}
	}


	private static void createCsvFile() throws IOException	{

		List<List<String>> rows = new ArrayList<List<String>>();
		for (Casa casa : annunciList) {
			List<String> casaRow = Arrays.asList(
					casa.getIdAnnuncio().replaceAll(",", " "), 
					casa.getTitolo().replaceAll(",", " "), 
					casa.getPrezzo().replaceAll(",", " "),
					casa.getNumLocali()!=null ? casa.getNumLocali().replaceAll(",", " ") : null,
					casa.getMetriQuadri()!=null ? casa.getMetriQuadri().replaceAll(",", " ") : null,
					casa.getNumBagni()!=null ? casa.getNumBagni().replaceAll(",", " ") : null,
					casa.getPiano()!=null ? casa.getPiano().replaceAll(",", " ") : null,
					casa.getDescrizione().replaceAll(",", ";"),
					casa.getLink().replaceAll(",", " "),
					casa.getAgenzia()!=null ? casa.getAgenzia().replaceAll(",", " ") : null);
			rows.add(casaRow);
		}

		String CSVpathFile = System.getProperty("user.home") + "/Desktop/" + "case.csv";
		FileWriter csvWriter = new FileWriter(CSVpathFile);
		
		csvWriter.append("id").append(",");
		csvWriter.append("titolo").append(",");
		csvWriter.append("prezzo").append(",");
		csvWriter.append("num locali").append(",");
		csvWriter.append("m2").append(",");
		csvWriter.append("num bagni").append(",");
		csvWriter.append("piano").append(",");
		csvWriter.append("descrizione").append(",");
		csvWriter.append("link").append(",");
		csvWriter.append("agenzia");
		csvWriter.append("\n");

		for (List<String> rowData : rows) {
			csvWriter.append(String.join(",", rowData));
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	}
}
