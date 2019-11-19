package com.house.finder.housefinder.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.Casa;

@Repository
public interface CasaRepository extends JpaRepository<Casa, Integer> {

	@Query("SELECT c FROM Casa c WHERE c.idAnnuncio=(:idAnnuncio)")
    Casa findByIdAnnuncio(@Param("idAnnuncio") String idAnnuncio);
	
	@Query("SELECT c FROM Casa c WHERE c.lastAnalyze<(:lastAnalyze) OR c.lastAnalyze IS NULL")
    List<Casa> findByLastAnalyze(@Param("lastAnalyze") Date lastAnalyze);
}
