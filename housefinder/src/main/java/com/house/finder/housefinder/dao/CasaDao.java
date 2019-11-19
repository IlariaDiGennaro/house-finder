package com.house.finder.housefinder.dao;

import java.util.List;

import org.hibernate.Session;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.dao.util.HibernateUtils;

public class CasaDao {

	Session sessionObjGlobal = null;
			
	public CasaDao() {
		sessionObjGlobal = HibernateUtils.buildSessionFactory().openSession();
		sessionObjGlobal.beginTransaction();
	}

	public void insertAll(List<Casa> caseList) {
		System.out.println("INSERT ALL");

		Session sessionObj = HibernateUtils.buildSessionFactory().openSession();
		sessionObj.beginTransaction();

		for (Casa casa : caseList) {
			sessionObj.save(casa);
		}

		sessionObj.getTransaction().commit();
		sessionObj.close();
	}
	
	public void createOrUpdate(Casa casa, boolean isLast) {
		
		if(sessionObjGlobal == null) {
			sessionObjGlobal = HibernateUtils.buildSessionFactory().openSession();
			sessionObjGlobal.beginTransaction();
		}
		
		Casa casaFound = (Casa) sessionObjGlobal.createSQLQuery("SELECT * FROM casa c WHERE c.id_annuncio="+casa.getIdAnnuncio()).addEntity(Casa.class).uniqueResult();
		
		if(casaFound != null) {
			
			casaFound.setTitolo(casa.getTitolo());
			casaFound.setDescrizione(casa.getDescrizione());
			casaFound.setPrezzo(casa.getPrezzo());
			casaFound.setNumLocali(casa.getNumLocali());
			casaFound.setMetriQuadri(casa.getMetriQuadri());
			casaFound.setNumBagni(casa.getNumBagni());
			casaFound.setPiano(casa.getPiano());
			casaFound.setAgenzia(casa.getAgenzia());
			
			sessionObjGlobal.update(casaFound);
			sessionObjGlobal.getTransaction().commit();
		}else {
			sessionObjGlobal.save(casa);
			sessionObjGlobal.getTransaction().commit();
		}
		
		if(isLast) {
			sessionObjGlobal.close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Casa> getAll() {
		System.out.println("GET ALL");

		Session sessionObj = HibernateUtils.buildSessionFactory().openSession();
		sessionObj.beginTransaction();
		
		List<Casa> resultList = sessionObj.createSQLQuery("SELECT * FROM casa").addEntity(Casa.class).list();

		sessionObj.close();
		return resultList;
	}
}
