package com.house.finder.housefinder.dao.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

	public static SessionFactory buildSessionFactory() {
		return new Configuration().configure().buildSessionFactory();
    }
}
