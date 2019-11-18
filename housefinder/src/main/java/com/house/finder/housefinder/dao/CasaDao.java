package com.house.finder.housefinder.dao;

import java.util.List;

import org.hibernate.Session;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.dao.util.HibernateUtils;

public class CasaDao {

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
}
