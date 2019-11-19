package com.house.finder.housefinder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.house.finder.housefinder.bean.Casa;
import com.house.finder.housefinder.bean.ZCasa;

@Repository
public interface ZCasaRepository extends JpaRepository<ZCasa, Integer> {

	@Query("SELECT zc FROM ZCasa zc WHERE zc.idAnnuncio=(:idAnnuncio)")
    Casa findByIdAnnuncio(@Param("idAnnuncio") String idAnnuncio);
}
