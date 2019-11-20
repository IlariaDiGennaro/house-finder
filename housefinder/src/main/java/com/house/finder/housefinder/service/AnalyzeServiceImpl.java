package com.house.finder.housefinder.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.ZCasa;
import com.house.finder.housefinder.dao.CasaRepository;
import com.house.finder.housefinder.dao.ZCasaRepository;
import com.house.finder.housefinder.site.bean.ImmobiliareIt;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {
	
	@Autowired
	public CasaRepository casaRepository;
	@Autowired
	public ZCasaRepository zCasaRepository;
	
	public static List<Casa> annunciList = new ArrayList<>();
	public static List<Casa> nuoveCostruzioniList = new ArrayList<>();
//	public static HashMap<Casa, List<Casa>> duplicatiMap = new HashMap<Casa, List<Casa>>();
	
	@Override
	public void analyze() throws IOException {
		
		analyzePage();
		System.out.println("annunci rozzi trovati: "+annunciList.size());

		firstCleaningAnnunnci();
		System.out.println("annunci normalizzati trovati: "+annunciList.size());
		
		Date lastAnalyze = new Date();
		
		System.out.println("create or update start ... ");
		for (int i = 0; i < annunciList.size(); i++) {
			
 			Casa casaFromWeb = annunciList.get(i);
 			if(i%50 == 0) System.out.println("... continuing ...");
 			
 			if(zCasaRepository.findByIdAnnuncio(casaFromWeb.getIdAnnuncio()) == null) {
 			
	 			Casa casaFound = casaRepository.findByIdAnnuncio(casaFromWeb.getIdAnnuncio());
	 			if(casaFound != null) {
	 				casaFound.setTitolo(casaFromWeb.getTitolo());
	 				casaFound.setDescrizione(casaFromWeb.getDescrizione());
	 				casaFound.setPrezzo(casaFromWeb.getPrezzo());
	 				casaFound.setNumLocali(casaFromWeb.getNumLocali());
	 				casaFound.setMetriQuadri(casaFromWeb.getMetriQuadri());
	 				casaFound.setNumBagni(casaFromWeb.getNumBagni());
	 				casaFound.setPiano(casaFromWeb.getPiano());
	 				casaFound.setAgenzia(casaFromWeb.getAgenzia());
	 				
	 				if(casaFound.isNewAnnuncio()) {
	 					//un annuncio è nuovo per 2gg dalla data di last analyze
	 					long nDay = ((lastAnalyze.getTime()-casaFound.getNewDatetime().getTime())/(3600*24*1000));
	 					
	 					if(nDay	> 2) {
	 						casaFound.setNewAnnuncio(false);
	 						casaFound.setNewDatetime(null);
	 					}
	 					
	 				}else {
	 					casaFound.setNewDatetime(lastAnalyze);
	 				}
	 				
	 				casaFound.setLastAnalyze(lastAnalyze);
	 				casaRepository.saveAndFlush(casaFound);
	 				
	 			}else {
	 				System.out.println("there's a ne annuncio");
	 				casaFromWeb.setLastAnalyze(lastAnalyze);
	 				casaFromWeb.setNewDatetime(lastAnalyze);
	 				casaFromWeb.setNewAnnuncio(true);
	 				casaRepository.saveAndFlush(casaFromWeb);
	 			}
 			}
		}
 		
 		System.out.println("... end.");
 		
 		System.out.println("check for delete start ... ");
 		List<Casa> casaListToDelete = casaRepository.findByLastAnalyze(lastAnalyze);
 		for (Casa casa : casaListToDelete) {
			
 			if(analyzeDetailsPage(casa.getIdAnnuncio())) {
 				
 				ZCasa casaToDelete = new ZCasa();
 				casaToDelete.setCancellato(true);
 				casaToDelete.setIdAnnuncio(casa.getIdAnnuncio());
 				casaToDelete.setTitolo(casa.getTitolo());
 				casaToDelete.setLink(casa.getLink());
 				casaToDelete.setDescrizione(casa.getDescrizione());
 				casaToDelete.setPrezzo(casa.getPrezzo());
 				casaToDelete.setNumLocali(casa.getNumLocali());
 				casaToDelete.setMetriQuadri(casa.getMetriQuadri());
 				casaToDelete.setNumBagni(casa.getNumBagni());
 				casaToDelete.setPiano(casa.getPiano());
 				casaToDelete.setGarantito(casa.isGarantito());
 				casaToDelete.setAgenzia(casa.getAgenzia());
 				casaToDelete.setLastAnalyze(casa.getLastAnalyze());
 				
 				zCasaRepository.saveAndFlush(casaToDelete);
 				casaRepository.delete(casa);
 				
 				System.out.println("a delete");
 			
 			} else {
 				
 				casa.setLastAnalyze(lastAnalyze);
 				casaRepository.saveAndFlush(casa);
 			}
		}
 		System.out.println("... end.");
		
		List<Casa> casaList = casaRepository.findAll();
		createCsvFile(casaList);
	}
	
	private boolean analyzeDetailsPage(String idAnnuncio) throws IOException{
		
		try {
			Jsoup.connect(ImmobiliareIt.getDetailsImmobilareItUrl(idAnnuncio)).get();
			return false;
		} catch (HttpStatusException e) {
			// da eliminare
			return true;
		}
	}
	
	private void analyzePage() throws IOException {
		Integer maxPage = 999;
		String immobiliareItUrl = ImmobiliareIt.getImmobilareItUrl();
		
		for (int i = 1; i <= maxPage; i++) {

			Document doc = Jsoup.connect(immobiliareItUrl.concat(String.valueOf(i))).get();

			if (i==1) {
				Elements ul = doc.select("ul.pagination__number");
				maxPage = Integer.valueOf(ul.select("li").last().text());
//				System.out.println("PAGINE DA ANALIZZARE: "+maxPage);
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
	}

	private void firstCleaningAnnunnci() {
		
		List<Casa> annunciList2 = new ArrayList<>();
		
		for (Casa casa : annunciList) {
			annunciList2.add(casa);
		}

		System.out.println("analyze start");

//		List<Casa> duplicatiList = new ArrayList<>();
//		Set<Casa> duplicatiDaEscludereSet = new HashSet<>();
		
		for (Casa casa1 : annunciList2) {

			if(casa1.getIdAnnuncio().startsWith("p-")) {
				//rimozione case "nuove costruzioni"
				annunciList.remove(casa1);
				nuoveCostruzioniList.add(casa1);
				continue;
			}

//			for (Casa casa2 : annunciList2) {
//
//				if(!casa1.getIdAnnuncio().equals(casa2.getIdAnnuncio()) 
//						&& !duplicatiDaEscludereSet.contains(casa2)
//						&& CasaComparator.equals(casa1, casa2)) {
//					duplicatiList.add(casa2);
//				}
//			}
//
//			if(!duplicatiList.isEmpty()) {
//				duplicatiMap.put(casa1, duplicatiList);
//				duplicatiDaEscludereSet.add(casa1);
//				duplicatiDaEscludereSet.addAll(duplicatiList);
//				annunciList.removeAll(duplicatiList);
//			}
//
//			duplicatiList = new ArrayList<>();
		}

		//TODO remove in future
//		for (Map.Entry<Casa, List<Casa>> entry : duplicatiMap.entrySet()) {
//			System.out.println(
//					entry.getKey().toString() + "::" +
//					"num duplicati "+entry.getValue().size() + "::" +
//					entry.getValue().toString());
//		}
	}

	private void createCsvFile(List<Casa> casaList) throws IOException	{

		List<List<String>> rows = new ArrayList<List<String>>();
		for (Casa casa : casaList) {
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
